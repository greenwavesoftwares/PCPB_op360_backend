package in.co.greenwave.jobapi.dao;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import in.co.greenwave.jobapi.model.Document;

public interface MinIOUploadDAO {

	Document uploadFile(MultipartFile file, String tenantId, String jobId, String activityId) throws Exception;

	InputStream downloadFile(String miniokey, String tenant) throws Exception;

}
