package edu.mcscheduling.model;

import android.content.ContentValues;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.mcscheduling.common.StatusCode;

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
			return StatusCode.ERR_PARM_USERID_ERROR();
		else if ( username == null || username.isEmpty() )
			return StatusCode.ERR_PARM_USERNAME_ERROR();
		else if ( userpasswd == null || userpasswd.isEmpty() )
			return StatusCode.ERR_PARM_USERPASSWD_ERROR();
		
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());

		
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
		
		if ( db.insert(sql) < 0) 
			return StatusCode.WAR_REGISTER_FAIL();
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
		if ( userid == null || userid.isEmpty() )
			return StatusCode.ERR_PARM_USERID_ERROR();
		else if ( userpasswd == null || userpasswd.isEmpty() )
			return StatusCode.ERR_PARM_USERPASSWD_ERROR();
		
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s' AND %s='%s'",
				DatabaseTable.User.name,
				DatabaseTable.User.colUserid, userid, 
				DatabaseTable.User.colUserpasswd, userpasswd
				);
		
		ResultSet rs = null;
		
		try {
			rs = db.select(sql);
			if ( rs == null || !rs.next() || rs.getInt(1) != 1 ) {
				return StatusCode.WAR_LOGIN_FAIL();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch ( SQLException e ) {
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
	public int changePasswd(String userid,String oldPasswd, String newPasswd)  {
		if ( userid == null || userid.isEmpty() )
			return StatusCode.ERR_PARM_USERID_ERROR();
		else if ( oldPasswd == null || oldPasswd.isEmpty() )
			return StatusCode.ERR_PARM_USERNAME_ERROR();
		else if ( newPasswd == null || newPasswd.isEmpty() )
			return StatusCode.ERR_PARM_USERPASSWD_ERROR(); 
		
		int ret = StatusCode.success; 
		try {
			db.setAutoCommit(false);
			
			String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s' AND %s='%s'",
					DatabaseTable.User.name,
					DatabaseTable.User.colUserid, userid, 
					DatabaseTable.User.colUserpasswd, oldPasswd
					);
				
		
			ResultSet rs = db.select(sql);
			if ( rs == null || !rs.next() || rs.getInt(1) != 1 ) {
				ret = StatusCode.WAR_PASSWD_NOT_CHANGE_FAIL();
				throw new SQLException();
			}		
		
			sql = String.format("UPDATE %s SET %s='%s' WHERE %s='%s'" ,
			// Table
			DatabaseTable.User.name, 
			// Value
			DatabaseTable.User.colUserpasswd, newPasswd,
			// WHERE
			DatabaseTable.User.colUserid, userid);

			if ( db.update(sql) < 0 ) {
				StatusCode.ERR_PASSWD_CHANGE_ERROR();
				throw new SQLException();
			}
			db.commit();
		} catch ( SQLException  e ) {
			try {
				db.rollback();
			} catch ( SQLException e1 ) {
				// nothing
			}
		}
			
		return ret;
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
		
		if ( db.update(sql) < 0 )
			return StatusCode.ERR_SET_MEMBER_INFO_ERROR();

		return StatusCode.success;
	}

	
	private int getMemberCount(String userid) {
		String sql = String.format("SELECT count(*) FROM %s WHERE %s='%s'", 
				DatabaseTable.User.name,
				DatabaseTable.User.colUserid, userid);
		
		ResultSet rs = db.select(sql);
		int rowCount = 0;
		if ( rs == null ) {
			return rowCount;
		} else {
			try {
				rs.next();
				rowCount = rs.getInt(1);
			} catch ( SQLException e ) {
				rowCount = 0;
			}
		}
		return rowCount;
	}
	
	public ContentValues[]  getMemberInformation(String userid) {
		int rowCount = getMemberCount(userid);
		
		if ( rowCount <= 0 )
			return null;
		
		String sql = String.format("SELECT %s,%s,%s,%s,%s FROM %s WHERE %s='%s'", 
								DatabaseTable.User.colUserid,
								DatabaseTable.User.colUsername,
								DatabaseTable.User.colUserpwdquestion,
								DatabaseTable.User.colUserpwdans,
								DatabaseTable.User.colUservalid,
								DatabaseTable.User.name,
								DatabaseTable.User.colUserid, userid
							);
		
		ResultSet rs = db.select(sql);
		
		if ( rs == null ) {
			return null;
		}
		
		ContentValues[] content = new ContentValues[rowCount];
		try {
			int i = 0;
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				content[i] = new ContentValues();
				for ( int j=1; j<=meta.getColumnCount(); j++ ){
					content[i].put(meta.getColumnName(j),rs.getString(meta.getColumnName(j)));
				}
				i++;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content;
	}
}
