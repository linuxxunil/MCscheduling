package edu.mcscheduling.common;

/**
 * StatusCode Definition 
 *  Status Code 是從 00000000~99999999 and -00000001~-99999999
 *  的一組數值，0:表示成功 / 正: Information / 負: Error 
 * @author jesse
 *
 */

public class StatusCode {
	public static final int success = 0;
	
	/**
	 * 
	 * @param errCode
	 * @param errDescription
	 * @return
	 */
	
	private static int log(String errCode, String errDescription) {
		int value = Integer.valueOf(errCode);
		if ( value > 0 ) System.out.println("WarnCode: " + errCode + ", WarnDescription: " + errDescription);
		else System.out.println("ErrCode: " + errCode + ", ErrDescription: " + errDescription);
		return value;
	}
	
	// Common Error	
	public static int ERR_JDBC_CLASS_NOT_FOUND() {
		return log("-001","JDBC class not found");
	}
	
	public static int ERR_INITIAL_DB_NOT_SUCCESS() {
		return log("-002","Inital DB don't success");
	}
	
	public static int ERR_SQL_SYNTAX_IS_ILLEGAL(String event) {
		return log("-003","SQL syntax is illegal("+event+")");
	}
	

	
	public static int ERR_NETWORK_DONT_SET_CONTEXT() {
		return log("-010","Network Context variable is null");
	}
	
	public static int ERR_NETWORK_ISNOT_AVAILABLE() {
		return log("-011","Network isn't avaiable");
	}
	
	
	
	
	// Parameter Error
	public static int ERR_PARM_SQL_SYNTAX_IS_NULL() {
		return log("-101","SQL syntax is null");
	}
	
	public static int ERR_PARM_USERID_ERROR() {
		return log("-102","PARM: userid is error");
	}
	
	public static int ERR_PARM_USERNAME_ERROR() {
		return log("-103","PARM: username is error");
	}
	
	public static int ERR_PARM_USERPASSWD_ERROR() {
		return log("-104","PARM: userpasswd is error");
	}
	
	
	// Runtime
	public static int ERR_UNKOWN_ERROR() {
		return log("-999","unkown error");
	}
	
	
	// SqliteDriver
	public static int ERR_OPEN_DIR(String path) {
		return log("-1001","Open sqlite dir error");
	}
	
	public static int ERR_OPEN_SQLITE_FILE(String path) {
		return log("-1002","Open Sqlite file error");
	}
	
	// MSSQL
	public static int ERR_MSSQL_CONNECT_ERROR() {
		return log("-2001","Cannot connect to MSSQL ");
	}
	
	// Account
	public static int WAR_REGISTER_FAIL() {
		return log("3001","Register Fail");
	}
	
	public static int WAR_LOGIN_FAIL() {
		return log("3002","Login Fail");
	}
	public static int WAR_PASSWD_NOT_CHANGE_FAIL() {
		return log("3003","Password not change");
	}
	
	public static int ERR_PASSWD_CHANGE_ERROR() {
		return log("-3004","Password change error");
	}
	
	public static int ERR_SET_MEMBER_INFO_ERROR() {
		return log("-3005","Set member information error");
	}
	
	// http
	public static int ERR_HTTP_PROTOCOL_ERR(String evt) {
		return log("-4001","HTTP Protocol error("+evt+")");
	}
	
	public static int ERR_HTTP_CONNECT_ERR() {
		return log("-4002","HTTP connect error");
	}
	
	public static int ERR_HTTP_RESPONSE_CODE_ERR(int code) {
		return log("-4003","HTTP response error("+code+")");
	}
	
	public static int ERR_HTTP_URL_ILLEGAL(String evt) {
		return log("-4004","HTTP URL is illegal("+evt+")");
	}
	
	public static int ERR_HTTP_IO_ERR(String evt) {
		return log("-4005","HTTP IO error("+evt+")");
	}
	
	// Authorize Activty
	public static int WAR_MACT_UNINSTALLED() {
		return log("10001","Mact uninstalled ");
	}
	
}
