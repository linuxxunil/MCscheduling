package edu.mcscheduling.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Doctor {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public Doctor(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}
	
	public void addDoctor(String depName, String dorName, String telephone, String jobTitle, String History,
			String subject, String updateID, String desc, String picPath) {
		
	}
	
	public void updateDoctor() {
		
	}
	
	public ContentValues[]  getDoctor() {
		String sql = String.format("SELECT * FROM %s", DatabaseTable.Department.name);
		
		Cursor cursor = db.select(sql);
	
		if ( cursor == null ) 
			return null;
		
		ContentValues[] content = null;
		cursor.moveToFirst(); 
		int rows = cursor.getCount();
		int columns = cursor.getColumnCount();

		if ( rows <= 0 ) 
			return null;
		
		content = new ContentValues[rows];
	
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
