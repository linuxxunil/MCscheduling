package edu.mcscheduling.controller;

import edu.mcscheduling.model.DatabaseDriver;
import edu.mcscheduling.model.SqliteDriver;
import android.app.Activity;
import android.os.Bundle;

public class ControllerActivity extends Activity {
	static protected DatabaseDriver db = null ;
	static protected SqliteDriver sqlite = null;
	
	private static final String dbPath = "/sdcard/data/cscheduling/cscheduling.db";
	
	static public enum AccessType {
		SQLITE,MSSQL,CACHE
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
			setAccessDriver(AccessType.SQLITE);
		}
	}
}
