package com.random.number.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.random.number.models.Document;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

@Service
public class JobDetailsService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	 private  MinioClient minioClient;
	
	
	
	
	 @Value("${minio.bucket}")
	    private String bucketName;

	    // Upload single file
	    public Document uploadFile(MultipartFile file, Integer userId) throws Exception {
	        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

	        // Upload to MinIO
	        minioClient.putObject(
	                PutObjectArgs.builder()
	                        .bucket(bucketName)
	                        .object(key)
	                        .stream(file.getInputStream(), file.getSize(), -1)
	                        .contentType(file.getContentType())
	                        .build()
	        );

	        // Insert metadata into SQL Server
	        String sql = "INSERT INTO [IDIALOGUE_2025].[dbo].[documents]\r\n"
	        		+ " (filename, minio_key, file_type, user_id) VALUES (?, ?, ?, ?)";
	        jdbcTemplate.update(sql, file.getOriginalFilename(), key, file.getContentType(), userId);

	        // Retrieve the inserted record ID
	        Long id = jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Long.class);

	        return new Document(id, file.getOriginalFilename(), key, file.getContentType(), userId, LocalDateTime.now());
	    }

	    // Get files with presigned URLs
	    public List<Map<String, String>> getFilesWithUrls(Integer userId) throws Exception {
	        String sql = "SELECT id, filename, minio_key, file_type, user_id, created_at FROM [IDIALOGUE_2025].[dbo].[documents]\r\n"
	        		+ " WHERE user_id = ?";
	        List<Document> docs = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
	                new Document(
	                        rs.getLong("id"),
	                        rs.getString("filename"),
	                        rs.getString("minio_key"),
	                        rs.getString("file_type"),
	                        rs.getInt("user_id"),
	                        rs.getTimestamp("created_at").toLocalDateTime()
	                )
	        );

	        List<Map<String, String>> result = new ArrayList<>();
	        for (Document d : docs) {
	            Map<String, String> map = new HashMap<>();
	            map.put("filename", d.getFilename());
	            map.put("url", minioClient.getPresignedObjectUrl(
	                    GetPresignedObjectUrlArgs.builder()
	                            .method(Method.GET)
	                            .bucket(bucketName)
	                            .object(d.getMinioKey())
	                            .expiry(60 * 60)
	                            .build()
	            ));
	            result.add(map);
	        }
	        return result;
	    }
	
	

//	public String getJobDetailsFromDB() {
//		String sql = "SELECT TOP 1 JobName\r\n"
//				+ "  FROM [OP360_GWSW].[ITCITDOP360].[JobDetails]";  // Example SQL query, adjust as needed.
//		return jdbcTemplate.queryForObject(sql, String.class);
//	}
//	
	public boolean insertContractorOnboardData(String customerName, String description, String projectLeader,
			String activity, String hseQuestionnaire, String insurance,
			String poNo, String codeOfPractice, String safetyInduction,
			String physicalAndMedicalAptitude, String safetyAudit, String jobStartTime) {

//		String sql = "INSERT INTO [Vesuvius_Kolkata].[Contractors_Onboard] " +
//				"([Customer_Name], [Description], [Project_Leader], [Activity], [HSE_Questionnaire], " +
//				"[Insurance], [PO_No], [Code_of_practice], [Safety_induction], " +
//				"[Physical_and_Medical_aptitude], [Safety_Audit], [Job_Start_Time], [DateAndTime]) " +
//				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
		
		String sql2 = "IF NOT EXISTS (SELECT 1 FROM [Vesuvius_Kolkata].[Contractors_Onboard] WHERE [Description] = ?) BEGIN INSERT INTO [Vesuvius_Kolkata].[Contractors_Onboard] ([Customer_Name], [Description], [Project_Leader], [Activity], [HSE_Questionnaire], [Insurance], [PO_No], [Code_of_practice], [Safety_induction], [Physical_and_Medical_aptitude], [Safety_Audit], [Job_Start_Time], [DateAndTime]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE());END";


		int result = jdbcTemplate.update(sql2,description,
				customerName,
				description,
				projectLeader,
				activity,
				hseQuestionnaire,
				insurance,
				poNo,
				codeOfPractice,
				safetyInduction,
				physicalAndMedicalAptitude,
				safetyAudit,
				jobStartTime
				);

		return result > 0;  // returns true if the data was inserted successfully
	}

}
