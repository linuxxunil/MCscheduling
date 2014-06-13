package edu.mcscheduling.controller;

import org.json.JSONException;
import org.json.JSONObject;

import edu.mcscheduling.common.Network;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.MSSqlDriver;
import edu.mcscheduling.database.SqliteDriver;
import edu.mcscheduling.http.CsmpWebService;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Hospital;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class ControllerActivity extends Activity {
	static protected DatabaseDriver db = null ;
	static protected SqliteDriver sqlite = null;
	static protected MSSqlDriver mssql = null;
	static private String loginID = null;
	
	static protected enum AccessMode { NONE, ONLINE, OFFLINE } 
	static protected enum AccessDriver { SQLITE,MSSQL,CACHE}
	static private AccessMode accessMode = AccessMode.NONE;
	
	protected static final String dbPath = "/sdcard/data/cscheduling/cscheduling.db2";
	
	protected void setLoginID(String userid) {
		loginID = userid;
	}
	
	protected String getLoginID() {
		return loginID;
	}
	
	protected void setDatabaseMode(AccessMode value) {
		accessMode = value;
	}
	
	protected AccessMode getDatabaseMode() {
		return accessMode;
	}
	
	protected void logoutID() {
		loginID = null;
	}
	
	protected void setAccessDriver(AccessDriver type) {
		switch ( type ) {
		case SQLITE:
			initSqlite();
			db = sqlite;
		break;
		case MSSQL:
			initMSSql();
			db = mssql;
		break;
		default:
			db = sqlite;
		break;
		}
		
	}
	
	private void initSqlite() {
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
		//sqlite.close();
	}
	
	private void initMSSql() {
		mssql = new MSSqlDriver(); 
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ( accessMode == AccessMode.ONLINE )
			Network.setContext(getBaseContext());
		
	}
	

}
