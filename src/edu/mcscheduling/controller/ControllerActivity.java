package edu.mcscheduling.controller;

import edu.mcscheduling.common.Network;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.MSSqlDriver;
import edu.mcscheduling.database.SqliteDriver;
import edu.mcscheduling.model.DatabaseTable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ControllerActivity extends Activity {
	static protected DatabaseDriver db = null ;
	static protected SqliteDriver sqlite = null;
	static protected MSSqlDriver mssql = null;
	static private String loginID = null;
	static protected enum AccessMode { NONE, ONLINE, OFFLINE } 
	static protected enum AccessDriver { SQLITE,MSSQL,CACHE}
	static private AccessMode accessMode = AccessMode.NONE;
	final static protected String dbPath = "/sdcard/data/cscheduling/cscheduling.db2";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSqlite();
		setAccessDriver(AccessDriver.MSSQL);
		
		if ( accessMode == AccessMode.ONLINE )
			Network.setContext(getBaseContext());
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
	
	protected void setDatabaseMode(AccessMode value) {
		accessMode = value;
	}
	
	protected AccessMode getDatabaseMode() {
		return accessMode;
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
	
	protected AlertDialog progressDialog = null;
	
	protected void showProgessDialog(final Activity activity,final String message)
	{
		progressDialog = ProgressDialog.show(activity, "", message, true);	 		
	}
	
	protected void dismissProgresDialog() {
		if(progressDialog!=null){
			progressDialog.dismiss();	
			progressDialog = null;
		}
	}
	
	protected Handler handler = null;
	
	protected void sendMessage(int what, int statusCode, String message ) {
		if ( handler != null ) {
			Message msg = handler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("message", message);
			bundle.putInt("status", statusCode);
			msg.what = what;
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}
	
	protected void changeActivity(Context  from, Class to) {
		Intent intent = new Intent();
		intent.setClass(from, to);
		startActivity(intent);
		finish();
	}
}
