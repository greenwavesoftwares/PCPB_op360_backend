package in.co.greenwave.dashboardapi.model;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Transactiondata implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transactionid;
	private String jobid;
	private String activityid;
	private String formname;
	private int formversion;
	private Map<String,TrData> jsoninfo=new LinkedHashMap<String,TrData>();
	private String remarks;
	private String userid;
	private String role;
	private String transactionTimeStamp;
	private String cellAliasId;
	private String userRemaks;
	
	public Transactiondata(String transactionid, String jobid, String activityid, String formname, int formversion,
			Map<String, TrData> jsoninfo, String remarks, String userid, String role) {
		super();
		this.transactionid = transactionid;
		this.jobid = jobid;
		this.activityid = activityid;
		this.formname = formname;
		this.formversion = formversion;
		this.jsoninfo = jsoninfo;
		this.remarks = remarks;
		this.userid = userid;
		this.role = role;
	}
	
	public Transactiondata() {
		
		// TODO Auto-generated constructor stub
	}
	public static class TrData{
		private String value=null;
		private String remarks="NA";
		
		@Override
		public String toString() {
			return "TrData [value=" + value + ", remarks=" + remarks + "]";
		}
		public TrData() {}
		public TrData(String value, String remarks) {
			this.value = value;
			this.remarks = remarks;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		
	}
	public String getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	public String getJobid() {
		return jobid;
	}
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}
	public String getActivityid() {
		return activityid;
	}
	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
	public int getFormversion() {
		return formversion;
	}
	public void setFormversion(int formversion) {
		this.formversion = formversion;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public Map<String, TrData> getJsoninfo() {
		return jsoninfo;
	}

	public void setJsoninfo(Map<String, TrData> jsoninfo) {
		this.jsoninfo = jsoninfo;
	}

	public String getCellAliasId() {
		return cellAliasId;
	}

	public void setCellAliasId(String cellAliasId) {
		this.cellAliasId = cellAliasId;
	}

	public String getUserRemaks() {
		return userRemaks;
	}

	public void setUserRemaks(String userRemaks) {
		this.userRemaks = userRemaks;
	}

	public String getTransactionTimeStamp() {
		return transactionTimeStamp;
	}

	public void setTransactionTimeStamp(String transactionTimeStamp) {
		this.transactionTimeStamp = transactionTimeStamp;
	}

	@Override
	public String toString() {
		return "Transactiondata [transactionid=" + transactionid + ", jobid=" + jobid + ", activityid=" + activityid
				+ ", formname=" + formname + ", formversion=" + formversion + ", jsoninfo=" + jsoninfo + ", remarks="
				+ remarks + ", userid=" + userid + ", role=" + role + ", transactionTimeStamp=" + transactionTimeStamp
				+ ", cellAliasId=" + cellAliasId + ", userRemaks=" + userRemaks + "]";
	}
}
