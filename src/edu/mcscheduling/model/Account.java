package edu.mcscheduling.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import java.sql.ResultSet;
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
			return StatusCode.WAR_USERID_NULL_OR_EMPTY();
		else if ( username == null || username.isEmpty() )
			return StatusCode.WAR_USERNAME_NULL_OR_EMPTY();
		else if ( userpasswd == null || userpasswd.isEmpty() )
			return StatusCode.WAR_USERPASSWD_NULL_OR_EMPTY();
		
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());

		String columns = String.format("'%s','%s','%s','%s'",
					DatabaseTable.User.colUserid, DatabaseTable.User.colUsername,
					DatabaseTable.User.colUserpasswd,
					DatabaseTable.User.colUservalid);
		
		String values = String.format("'%s','%s','%s','%s'", userid, username,
					userpasswd, formatter.format(time));

		if ( db.inset(DatabaseTable.User.name, columns, values) < 0) 
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
			return StatusCode.WAR_USERID_NULL_OR_EMPTY();
		else if ( userpasswd == null || userpasswd.isEmpty() )
			return StatusCode.WAR_USERPASSWD_NULL_OR_EMPTY();
		
		String columns = String.format("'%s','%s'",
				DatabaseTable.User.colUserid, DatabaseTable.User.colUserpasswd);

		String whereExpr = String.format("%s='%s' AND %s='%s'", 
				DatabaseTable.User.colUserid, userid, 
				DatabaseTable.User.colUserpasswd, userpasswd);

		//Cursor cursor = db.select(DatabaseTable.User.name, columns, whereExpr);
		ResultSet result = db.select(DatabaseTable.User.name, columns, whereExpr);

		if (result != null && result.getCount() != 1)
			return StatusCode.WAR_LOGIN_FAIL();

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
			return StatusCode.WAR_USERID_NULL_OR_EMPTY();
		else if ( oldPasswd == null || oldPasswd.isEmpty() )
			return StatusCode.WAR_USERID_NULL_OR_EMPTY();
		else if ( newPasswd == null || newPasswd.isEmpty() )
			return StatusCode.WAR_USERPASSWD_NULL_OR_EMPTY(); 
		else if ( matchUseridPasswd(userid, oldPasswd) != StatusCode.success )
			return -1;
		
		String columns = String.format("%s='%s'",
				DatabaseTable.User.colUserpasswd, newPasswd);

		String whereExpr = String.format("%s='%s'", 
				DatabaseTable.User.colUserid, userid );

		if ( db.update(DatabaseTable.User.name, columns, whereExpr) < 0 )
			return 1;

		return StatusCode.success;
	}
	
	
	public int setMemberInformation(String userid,String username, String passwdQuestion, String  passwdAnswer) {
		
		String columns = String.format("%s='%s',%s='%s',%s='%s'",
				DatabaseTable.User.colUsername, username,
				DatabaseTable.User.colUserpwdquestion, passwdQuestion,
				DatabaseTable.User.colUserpwdans, passwdAnswer);

		String whereExpr = String.format("%s='%s'", 
				DatabaseTable.User.colUserid, userid );

		if ( db.update(DatabaseTable.User.name, columns, whereExpr) < 0 )
			return 1;

		return StatusCode.success;
	}
	
	public ContentValues[]  getMemberInformation(String userid) {
		
		String sql = String.format("SELECT %s,%s,%s,%s,%s FROM %s WHERE %s='%s'", 
								DatabaseTable.User.colUserid,
								DatabaseTable.User.colUsername,
								DatabaseTable.User.colUserpwdquestion,
								DatabaseTable.User.colUserpwdans,
								DatabaseTable.User.colUservalid,
								DatabaseTable.User.name,
								DatabaseTable.User.colUserid, userid
							);
		
		//Cursor cursor = db.select(sql);
		ResultSet result = db.select(sql);
		
		if ( result == null ) 
			return null;
		
		result.moveToFirst(); 
		int rows = result.getCount();
		if ( rows <= 0 ) {
			if ( !cursor.isClosed() )
				cursor.close();
			return null;
		}
		
		int columns = cursor.getColumnCount();
		ContentValues[] content = new ContentValues[rows];
	
		for ( int i=0; i<rows; i++ ) {
			content[i] = new ContentValues();
			for ( int j=0; j<columns; j++ ) {
				content[i].put(cursor.getColumnName(j), cursor.getString(j));	
			}
			cursor.moveToNext(); 
		}
		
		if ( !cursor.isClosed() )
			cursor.close();
		
		return content;
	}
}
