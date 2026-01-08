package in.co.greenwave.assetapi.utility;

public class JdbcUrlUtil {

	public static String buildJdbcUrl(String dbDriver, String dbIp, String dbName) {
		StringBuilder url = new StringBuilder();

		if (dbDriver.toLowerCase().contains("sqlserver")) {
			url.append("jdbc:sqlserver://")
			.append(dbIp)
			.append(";encrypt=true;trustServerCertificate=true;databaseName=")
			.append(dbName);
		} else if (dbDriver.toLowerCase().contains("postgres")) {
			url.append("jdbc:postgresql://")
			.append(dbIp)
			.append("/")
			.append(dbName);
		} else {
			throw new IllegalArgumentException("Unsupported driver type: " + dbDriver);
		}

		return url.toString();
	}

	
}
