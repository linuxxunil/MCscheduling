package edu.mcscheduling.model;

import android.content.ContentValues;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.mcscheduling.common.Logger;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.MsResultSet;
import edu.mcscheduling.database.Transation;

public class Account {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public Account(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}

	/**
	 * 函數名稱 : register </br>
	 * 函數說明 : 註冊使用者帳號 </br>
	 * 函數範例 : None </br>
	 * @param userid		"此欄位為PK，並且採用mail address做為紀錄"
	 * @param username		"使用者名稱"
	 * @param userpasswd	"使用者密碼"
	 * @return 
	 */
	public int register(String userid, String username, String userpasswd){	
		if ( userid == null || userid.isEmpty() )
			return  Logger.e(this, StatusCode.PARM_USERID_ERROR);
		else if ( username == null || username.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERNAME_ERROR);
		else if ( userpasswd == null || userpasswd.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERPASSWD_ERROR);
		
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());

		int status = StatusCode.success;
		String sql = String.format("INSERT INTO %s (%s,%s,%s,%s) " +
				"VALUES ('%s','%s','%s','%s')",
				// Columns
				DatabaseTable.User.name,
				DatabaseTable.User.colUserid, 
				DatabaseTable.User.colUsername,
				DatabaseTable.User.colUserpasswd,
				DatabaseTable.User.colUservalid,
				// Values
				 userid, 
				 username,
				userpasswd,
				formatter.format(time));
		
		status = db.insert(sql);
		if ( status != StatusCode.success )
			return status;
		return StatusCode.success;
	}
	
	/**
	 * 函數名稱 : matchUseridPasswd </br>
	 * 函數說明 : 檢查使用者帳號與密碼是否正確 </br>
	 * 函數範例 : None </br>
	 * @param userid
	 * @param userpasswd
	 * @return
	 */
	private int matchUseridPasswd(String userid, String userpasswd) {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s' AND %s='%s'",
				DatabaseTable.User.name,
				DatabaseTable.User.colUserid, userid, 
				DatabaseTable.User.colUserpasswd, userpasswd
				);

		MsResultSet rtVal = new MsResultSet();
		try {
			rtVal = db.select(sql);

			if ( rtVal.status != StatusCode.success )
				return rtVal.status;
			if ( rtVal.rs == null || !rtVal.rs.next() ) {
				return Logger.e(this, StatusCode.ERR_GET_RESULTSET_FAIL);
			} else if ( rtVal.rs.getInt(1) != 1 ) {
				return Logger.e(this, StatusCode.ERR_LOGIN_FAIL);
			}
		} catch (Exception e) {
			Logger.e(this, StatusCode.ERR_UNKOWN_ERROR,e.getMessage());
		} finally {
			try {
				rtVal.rs.close();
			} catch ( Exception e ) {
				// nothing
			}
		}

		return StatusCode.success;
	}
	
	/**
	 * 函數名稱 : login </br>
	 * 函數說明 : 使用者登入 </br>
	 * 函數範例 : None </br>
	 * @param userid		"使用者ID"
	 * @param userpasswd	"使用者密碼"
	 * @return 
	 */
	public int login(String userid, String userpasswd)  {
		return matchUseridPasswd(userid, userpasswd);
	}
	
	/**
	 * 函數名稱 : changePasswd </br>
	 * 函數說明 : 修改使用者密碼</br>
	 * 函數範例 : None </br>
	 * @param userid
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 */
	public int changePasswd(final String userid,final String oldPasswd,final String newPasswd)  {
		if ( userid == null || userid.isEmpty() )
			return  Logger.e(this, StatusCode.PARM_USERID_ERROR);
		else if ( oldPasswd == null || oldPasswd.isEmpty() )
			return Logger.e(this, StatusCode.PARM_OLDPASSWD_ERROR);
		else if ( newPasswd == null || newPasswd.isEmpty() )
			return Logger.e(this, StatusCode.PARM_NEWPASSWD_ERROR);
		
		return db.excuteTransation(new Transation() {				
			@Override
			public 	Integer execute(Object retValue) throws Exception {
				int status = StatusCode.success; 
				String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s' AND %s='%s'",
						DatabaseTable.User.name,
						DatabaseTable.User.colUserid, userid, 
						DatabaseTable.User.colUserpasswd, oldPasswd
						);
				
				MsResultSet rtVal = new MsResultSet();
				rtVal = db.select(sql);
				if ( rtVal.status != StatusCode.success || rtVal.rs == null || !rtVal.rs.next() ) {
					return rtVal.status;
				} else if ( rtVal.rs.getInt(1) != 1 ) {
					return Logger.e(this, StatusCode.ERR_MEMBER_NOT_EXIST);
				}
				
				sql = String.format("UPDATE %s SET %s='%s' WHERE %s='%s'" ,
						// Table
						DatabaseTable.User.name, 
						// Value
						DatabaseTable.User.colUserpasswd, newPasswd,
						// WHERE
						DatabaseTable.User.colUserid, userid);
						
				status = db.update(sql);
				if (  status != StatusCode.success ) {
					return status;
				}
				return StatusCode.success;
			}
		}, null);
	}
	
	
	public int setMemberInformation(String userid,String username, String passwdQuestion, String  passwdAnswer) {
		
		String sql = String.format("UPDATE %s SET %s='%s',%s='%s',%s='%s' WHERE %s='%s'" ,
				// Table
				DatabaseTable.User.name, 
				// Value
				DatabaseTable.User.colUsername, username,
				DatabaseTable.User.colUserpwdquestion, passwdQuestion,
				DatabaseTable.User.colUserpwdans, passwdAnswer,
				// WHERE
				DatabaseTable.User.colUserid, userid);
		return db.update(sql);
	}

	
	private int getMemberCount(String sql) {
		MsResultSet rtVal = new MsResultSet();
		rtVal = db.select(sql);
		
		int rowCount = 0;
		if ( rtVal.rs == null ) {
			return rowCount;
		} else {
			try {
				rtVal.rs.next();
				rowCount = rtVal.rs.getInt(1);
			} catch ( Exception e ) {
				rowCount = 0;
			}
		}
		return rowCount;
	}
	
	public MsContentValues getMemberInformation(String userid) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		
		String sql = String.format("SELECT count(*) FROM %s WHERE %s='%s'", 
				DatabaseTable.User.name,
				DatabaseTable.User.colUserid, userid);	
		
		
		int rowCount = getMemberCount(sql);
		if ( rowCount <= 0 ) 
			return new MsContentValues(Logger.e(this, StatusCode.ERR_MEMBER_NOT_EXIST));
		
		sql = String.format("SELECT %s,%s,%s,%s,%s FROM %s WHERE %s='%s'", 
								DatabaseTable.User.colUserid,
								DatabaseTable.User.colUsername,
								DatabaseTable.User.colUserpwdquestion,
								DatabaseTable.User.colUserpwdans,
								DatabaseTable.User.colUservalid,
								DatabaseTable.User.name,
								DatabaseTable.User.colUserid, userid
							);
		
		MsResultSet rsVal = null;
		rsVal = db.select(sql);
		
		if ( rsVal.status != StatusCode.success )
			return new MsContentValues(rsVal.status);
		
		MsContentValues cvValue = new MsContentValues(StatusCode.success);
				cvValue.cv = new ContentValues[rowCount];
		try {
			int i = 0;
			ResultSetMetaData meta = rsVal.rs.getMetaData();
			while (rsVal.rs.next()) {
				cvValue.cv[i] = new ContentValues();
				for ( int j=1; j<=meta.getColumnCount(); j++ ){
					cvValue.cv[i].put(meta.getColumnName(j),rsVal.rs.getString(meta.getColumnName(j)));
				}
				i++;
			}
			rsVal.rs.close();
		} catch (SQLException e) {
			return new MsContentValues(Logger.e(this, StatusCode.ERR_GET_MEMBER_INFO_FAIL));
		}
		return cvValue;
	}
}
