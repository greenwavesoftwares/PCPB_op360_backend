package in.co.greenwave.UserGroup.model;

public class DashboardLogbook {

	String formname;
	String formid;
	String versionNumber;
	public DashboardLogbook() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DashboardLogbook(String formname, String formid, String versionNumber) {
		super();
		this.formname = formname;
		this.formid = formid;
		this.versionNumber = versionNumber;
	}
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	@Override
	public String toString() {
		return "DashboardLogbook [formname=" + formname + ", formid=" + formid + ", versionNumber=" + versionNumber
				+ "]";
	}
	
	
}
