package com.random.number.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.random.number.models.Document;
import com.random.number.service.JobDetailsService;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/files")
//@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001")
public class FileController {
	@Autowired
	private MinioClient minioClient;  // inject existing bean
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
    private JobDetailsService fileService;
	 @Value("${minio.bucket}")
	    private String bucketName;
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadSingleFile(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("userId") Integer userId) {
        try {
            if (file.isEmpty()) return ResponseEntity.badRequest().build();

            Document savedDoc = fileService.uploadFile(file, userId);
            return ResponseEntity.ok(savedDoc);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserFiles(@PathVariable Integer userId) {
        String sql = """
            SELECT id, minio_key
            FROM [IDIALOGUE_2025].[dbo].[documents]
            WHERE user_id = ?
            """;

        List<Map<String, Object>> files = jdbcTemplate.queryForList(sql, userId);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/file")
    public void getFileByKey(@RequestParam String minio_key, HttpServletResponse response) {
        try {
            InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minio_key)
                    .build()
            );

            String contentType = null;

            // Try automatic detection
            try {
                contentType = java.nio.file.Files.probeContentType(
                    java.nio.file.Paths.get(minio_key)
                );
            } catch (Exception ignored) {}

            // Special handling for .eml
            if (contentType == null && minio_key.toLowerCase().endsWith(".eml")) {
                contentType = "message/rfc822";
            }

            // Fallback for unknown types
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            response.setContentType(contentType);
            response.setHeader(
                "Content-Disposition",
                "inline; filename=\"" + minio_key + "\""
            );

            try (stream; ServletOutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }



}
