package edu.mcscheduling.common;

/**
 * StatusCode Definition 
 *  Status Code 是從 00000000~99999999 and -00000001~-99999999
 *  的一組數值，0:表示成功 / 正: Information / 負: Error 
 *  
 *  第 1 digital(MSB) = Log Type ( Developer Log (0) / User Log(1) )
 *  第 2 digital(MSB) = Package Number
 *  第 3~4 digital(MSB) = Class Number
 *  第 5~6 digital(MSB) = Function Number
 *  第 7~8 digital(MSB) = Status Code
 *  
 *  0x0 0 0 00 00 00
 *  1 2  3  4  5
 *  
 *  
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
		return log("-3001","Password change error");
	}
	
	public static int ERR_SET_MEMBER_INFO_ERROR() {
		return log("-3002","Set member information error");
	}
	
}
