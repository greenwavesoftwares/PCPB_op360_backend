package in.co.greenwave.jobapi.model;

import java.time.LocalDateTime;

public class Document {
	 private Long id;
	    private String filename;
	    private String minioKey;
	    private String fileType;
	    private Integer userId;
	    private LocalDateTime createdAt;
		public Document() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Document(Long id, String filename, String minioKey, String fileType, Integer userId,
				LocalDateTime createdAt) {
			super();
			this.id = id;
			this.filename = filename;
			this.minioKey = minioKey;
			this.fileType = fileType;
			this.userId = userId;
			this.createdAt = createdAt;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		public String getMinioKey() {
			return minioKey;
		}
		public void setMinioKey(String minioKey) {
			this.minioKey = minioKey;
		}
		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		@Override
		public String toString() {
			return "Document [id=" + id + ", filename=" + filename + ", minioKey=" + minioKey + ", fileType=" + fileType
					+ ", userId=" + userId + ", createdAt=" + createdAt + "]";
		}
	    
	    
}
