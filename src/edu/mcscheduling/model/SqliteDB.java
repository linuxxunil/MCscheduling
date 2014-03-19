package edu.mcscheduling.model;

import java.sql.SQLException;

import android.content.ContentValues;
import android.database.Cursor;

public class SqliteDB {
	private static final String dbPath = "/sdcard/data/cscheduling/cscheduling.db";
	private static SqliteDriver db;

	public SqliteDB() {

		try {

			db = new SqliteDriver(dbPath);
			db.onConnect();
			db.createTable(DatabaseTable.Hospital.create());
			db.createTable(DatabaseTable.CodeFile.create());
			db.createTable(DatabaseTable.Doctor.create());
			db.createTable(DatabaseTable.Department.create());
			db.createTable(DatabaseTable.DocSchedule.create());
			db.createTable(DatabaseTable.User.create());
			
			Account account = new Account(db);
			//System.out.println(account.register("c@a.com", "jesse", "1qazxcvb"));
			//System.out.println(account.login("a@a.com", "1qazxcvb"));
			//System.out.println(account.changePasswd("a@a.com","1qazxcvb", "1qazxcvb2"));
			
			Hospital hospital = new Hospital(db);
			//System.out.println(hospital.setHospital("updateID", "hospitalName", 
			//		"areaID", "0929238459", "hospitalAddress", "contactName", 
			//		"contactPhone", "depName", "opdSt1", "opdEt1", "opdSt2", "opdEt2", 
			//		"opdSt3", "opdEt3", "hispitalSchedule", "1", "picPath"));
			
			//ContentValues[] content = hospital.getHospital();
			
			//if ( content == null )
			//	return;
			
			
			//System.out.println(DatabaseTable.Hospital.colAreaID);
			//System.out.println(content[0].get(DatabaseTable.Hospital.colAreaID));
			Department depart = new Department(db);
			System.out.println(depart.setDepartment("updateID","bbb,ccc","NULL"));
			
			
			/*
			Cursor cursor = db.select("select *  from User"); 

			cursor.moveToFirst(); 
			for ( int i=0; i<cursor.getCount(); i++) {
				System.out.println("AAA" + cursor.getString(0)); 
				cursor.moveToNext(); 
			}
			cursor.close();*/
			
			
			 
			 
		} catch (Exception e) {

			 System.out.println(e.getMessage() );
		}

	}

}
