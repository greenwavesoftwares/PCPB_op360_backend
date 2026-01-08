package in.co.greenwave.taskapi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import in.co.greenwave.taskapi.model.FormDetails;
import in.co.greenwave.taskapi.model.UserDepartment;

public interface LogbookDAO {

	public List<FormDetails> getAllFormNames();
//	public List<FormDetails> getAllActiveForms();
//	public List<FormDetails> getformDetailsWithFormName(String FormName);
//	public String getDataFromDepSQL(String sql);
//	public List<String> getMetaDataofResultSet(String sql);
//	public List<FormInfo> loadDLBForm(String formName, int vrsnNumber);
//	public List<DBResultModel> getTableDataManualEntry(String sql);
//	public List<DropDownItems> getDrpItemsfromSQL(String sql);
//	public int saveLogbookData(String deleteQuery,String insertQuery) ;
//	public void deleteSavedRow(String delSql);
//	public void deleteLogbook(int formId);
//	public String fetchLogbookNameforJob(String jobId,String activityId);
//	public void saveTransaction(Transactiondata objTransdata);
//	public String fetchTransactionId(String jobId, String activityId,String logbook);
//	public void saveDLBForm(String oldFormName,String formName,String tableSql,String insertSQL,String deleteSQL,
//			String user,List<FormInfo> formModel,String documentId,String selectedGrp,String department,String formatID,
//			Boolean versionNumber, Boolean editExisting,List<Integer> versionList,int vrsnNumber, String userID);
//	public String lastJsonLogData(String logbookname,int version,String jobid,String activityid);
//	public void updateLogbookStatus(String formname,int version,boolean status);
//	public List<CellEditHistory> getCellHistory(String cellid,String cellname,String jobid,String activityid);
//	public List<FormDetails> getUserIdWiseForms(String userid);
//	public Map<String,String> getQueryResultForGlobalCellUpdate(String query);

	public List<UserDepartment> getAllUserDept();
	
}
