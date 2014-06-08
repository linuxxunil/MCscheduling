package edu.mcscheduling.controller;

import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseDriver;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.SqliteDriver;
import android.app.Activity;
import android.os.Bundle;

public class ControllerActivity extends Activity {
	static protected DatabaseDriver db = null ;
	static protected SqliteDriver sqlite = null;
	static private String loginID = null;
	
	private static final String dbPath = "/sdcard/data/cscheduling/cscheduling.db";
	
	static public enum AccessType {
		SQLITE,MSSQL,CACHE
	}
	
	
	protected void setLoginID(String userid) {
		loginID = userid;
	}
	
	protected String getLoginID() {
		return loginID;
	}
	
	protected void logoutID() {
		loginID = null;
	}
	
	protected void setAccessDriver(AccessType type) {
		
		switch ( type ) {
		case SQLITE:
			db = sqlite;
		break;
		default:
			db = sqlite;
		break;
		}
		
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if ( db == null ) {
			sqlite = new SqliteDriver(dbPath); 
			if ( sqlite.onConnect() == StatusCode.success ) {
				sqlite.createTable(DatabaseTable.Hospital.create());
				sqlite.createTable(DatabaseTable.CodeFile.create());
				sqlite.createTable(DatabaseTable.Doctor.create());
				sqlite.createTable(DatabaseTable.Department.create());
				sqlite.createTable(DatabaseTable.DoctorSchedule.create());
				sqlite.createTable(DatabaseTable.User.create());
			} else {
				sqlite = null;
			}
			setAccessDriver(AccessType.SQLITE);
		}
	}
}
