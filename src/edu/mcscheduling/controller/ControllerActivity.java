package edu.mcscheduling.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseDriver;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.SqliteDriver;
import edu.mcscheduling.model.MSSqlDriver;
import edu.mcscheduling.model.Databasepointer;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ControllerActivity extends Activity {
	static protected DatabaseDriver db = null ;
	static protected SqliteDriver sqlite = null;
	static protected MSSqlDriver msssql = null;
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
		case MSSQL:
			db = msssql;
		default:
			db = sqlite;
		break;
		}
		
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ( db == null ) {
			/*sqlite = new SqliteDriver(dbPath); 
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
			setAccessDriver(AccessType.SQLITE);*/
			System.out.println("test ms");
			//msssql = new MSSqlDriver(); 
			ConnectTask dt = new ConnectTask();
			dt.execute();
			setAccessDriver(AccessType.MSSQL);
		}
	}
	
	class ConnectTask extends AsyncTask<Integer, Integer, MSSqlDriver> {
		Connection con = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected MSSqlDriver doInBackground(Integer... params) {
			msssql = new MSSqlDriver(); 
			if ( msssql.onConnect() == StatusCode.success ) {
				try {
					Databasepointer dp = new Databasepointer(msssql.select("SELECT TOP 1000 [HospitalNo] FROM [cscheduling].[dbo].[Doctor]"));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*ResultSet rs = msssql.selectMS("SELECT TOP 1000 [HospitalNo] FROM [cscheduling].[dbo].[Doctor]"); 
				try {
					while (rs.next()) { 
						Log.i("Jason", "HospitalNo :" + rs.getString("HospitalNo"));
					}
					rs.close();
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
				}
				msssql.createTable(DatabaseTable.Hospital.create());
				msssql.createTable(DatabaseTable.CodeFile.create());
				msssql.createTable(DatabaseTable.Doctor.create());
				msssql.createTable(DatabaseTable.Department.create());
				msssql.createTable(DatabaseTable.DoctorSchedule.create());
				msssql.createTable(DatabaseTable.User.create());*/
				System.out.println("test connect MSS success");
			} else {
				msssql = null;
				System.out.println("test connect MSS error");
			}
			/*int b = msssql.inset("INSERT INTO [cscheduling].[dbo].[Department] ([HospitalNo],[DepName],[UpdateID],[UpdateDate],[Desc]) VALUES ('00007','憭��','b@b.com','2014-03-31 00:00:00.000','NULL')"); 
			int c = msssql.delete("DELETE FROM [cscheduling].[dbo].[Department] WHERE [DepName]='憭��'"); 
			int d = msssql.update("UPDATE [cscheduling].[dbo].[Department] SET [HospitalNo]=77777 WHERE [DepName]='蝡寧��'");
			int e = msssql.createTable("CREATE TABLE [dbo].[test]([HospitalNo] [nvarchar](10) NOT NULL,[DepName] [nvarchar](50) NULL) ON [PRIMARY]");*/
			System.out.println("MS select ok");
			//return "success";
			return msssql;
		}
	}
}
