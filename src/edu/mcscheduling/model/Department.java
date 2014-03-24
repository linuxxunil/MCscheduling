package edu.mcscheduling.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.mcsheduling.common.StatusCode;

import android.content.ContentValues;
import android.database.Cursor;

public class Department {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public Department(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}
	
	public int setDepartment(String updateID, String depName, String desc) {
		if ( this.exist == false ) {
			return -1;
		}else if ( updateID == null || updateID.isEmpty() )
			return -2;
		else if ( depName == null || depName.isEmpty() ) 
			return -3;
		else if ( desc == null || desc.isEmpty() ) 
			return -4;
		
		db.beginTransaction();
		try {
			String sql = String.format("DELETE FROM %s WHERE %s='%s'", 
							DatabaseTable.Department.name,
							DatabaseTable.Department.colUpdateID,
							updateID);
			
			if ( db.delete(sql) < 0 ) {
				return -1;
			} 
				
			String[] depart = depName.split(",");
			
			for ( int i=0; i<depart.length; i++ ) {
				sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s) " +
						"VALUES ('%05d','%s','%s','%s','%s')",
						DatabaseTable.Department.name,
						DatabaseTable.Department.colHospitalNo,
						DatabaseTable.Department.colDepName,
						DatabaseTable.Department.colUpdateID,
						DatabaseTable.Department.colUpdateDate,
						DatabaseTable.Department.colDesc,
						i+1,
						depart[i],
						updateID,
						new DateTime().getDateTime(),
						desc );
					if ( db.inset(sql) < 0 ) { 
						db.endTransaction();
						return -3;
					}
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		return StatusCode.success;
	}
	
	public ContentValues[] getDepartment(String userid) {
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.Department.name,
				DatabaseTable.Department.colUpdateID, userid);
		
		Cursor cursor = db.select(sql);
		
		if ( cursor == null ) 
			return null;
		
		cursor.moveToFirst(); 
		int rows = cursor.getCount();
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
