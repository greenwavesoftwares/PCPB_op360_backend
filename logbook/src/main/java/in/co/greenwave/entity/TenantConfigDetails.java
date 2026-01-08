package in.co.greenwave.entity;
public class TenantConfigDetails {
	private String tenantId;
    private String dbIp;
    private String dbName;
    private String dbPassword;
    private String driver;
    private String dbUser;

	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public TenantConfigDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TenantConfigDetails(String tenantId, String dbIp, String dbName, String dbPassword, String driver,
			String dbUser) {
		super();
		this.tenantId = tenantId;
		this.dbIp = dbIp;
		this.dbName = dbName;
		this.dbPassword = dbPassword;
		this.driver = driver;
		this.dbUser = dbUser;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbIp() {
		return dbIp;
	}
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	@Override
	public String toString() {
		return "TenantConfigDetails [dbIp=" + dbIp + ", dbName=" + dbName + ", dbPassword=" + dbPassword + "]";
	}
   
}
