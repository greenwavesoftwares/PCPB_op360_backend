package in.co.greenwave.jobapi.dao.implementation.sqlserver;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import in.co.greenwave.jobapi.model.Document;

import in.co.greenwave.jobapi.model.FileTypeInfo;
import in.co.greenwave.jobapi.dao.MinIOUploadDAO;
import in.co.greenwave.jobapi.dao.implementation.sqlserver.FileTypeUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

@Service
public class MinIOUploadService implements MinIOUploadDAO{

    @Autowired
    private MinioClient minioClient;

    @Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database


    // Upload single file
    @Override
    public Document uploadFile(MultipartFile file, String tenantId,String jobId,String activityId) throws Exception {
    	JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        System.out.println("Filename:"+file.getOriginalFilename());
        String bucket=tenantId.toLowerCase();

        // Upload to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // Insert metadata into SQL Server
        String sql = "update dbo.JobActivityDetails set FileName=? where JobId=? and ActivityId=?";
        jdbcTemplate.update(sql, key, jobId, activityId);
        // Retrieve the inserted record ID
        Long id = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);

        return new Document(id, file.getOriginalFilename(), key, file.getContentType(), null, LocalDateTime.now());
    }

    @Override
    public InputStream downloadFile(String key, String tenant) throws Exception {

        String bucket = tenant.toLowerCase();

        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .build()
        );
    }
    
}
