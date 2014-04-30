package edu.mcscheduling.model;

import android.content.ContentValues;
import android.database.Cursor;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Hospital;

public class Doctor {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public Doctor(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}
	
	public int addDoctor(String userid, String depName, String dorName, String telephone, String jobTitle, 
							String history,String subject, String desc, String picPath) {
		
		Cursor cursor = null;
		int ret = 0;
		
		try {
			db.beginTransaction();
			Hospital hospital = new Hospital(db);
			String hospitalNo = hospital.getHospitalNo(userid);
			String updateID = hospitalNo;
			
			if ( hospitalNo == null ) {
				if ( cursor != null || !cursor.isClosed() )
					cursor.close();
				return -1;
			}
			
			String sql = String.format("SELECT %s,%s FROM %s WHERE %s='%s' ORDER BY %s DESC", 
					DatabaseTable.Doctor.colDorNo,
					DatabaseTable.Doctor.colHospitalNo,
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo,
					hospitalNo,
					DatabaseTable.Doctor.colDorNo);
		
			cursor = db.select(sql); 
		
			if ( cursor == null ) {
				return -1;
			} 
			
			String dorNo = null;
			cursor.moveToFirst(); 
			if ( cursor.getCount() == 0 ) {
				dorNo = String.format("%05d", Integer.parseInt("0") + 1);
			} else {
				dorNo = String.format("%05d", Integer.parseInt(cursor.getString(0)) + 1);
			}
		
			
			sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) " +
					"VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
					// Columns
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo,
					DatabaseTable.Doctor.colDepName,
					DatabaseTable.Doctor.colDorNo,
					DatabaseTable.Doctor.colDorName,
					DatabaseTable.Doctor.colTelephone,
					DatabaseTable.Doctor.colJobTitle,
					DatabaseTable.Doctor.colHistory,
					DatabaseTable.Doctor.colSubject,
					DatabaseTable.Doctor.colValid,
					DatabaseTable.Doctor.colUpdateID,
					DatabaseTable.Doctor.colUpdateDate,
					DatabaseTable.Doctor.colDesc,
					DatabaseTable.Doctor.colPicPath,
					// Values
					hospitalNo,
					depName,
					dorNo,
					dorName,
					telephone,
					jobTitle,
					history,
					subject,
					"NULL",
					updateID,
					new DateTime().getDateTime(),
					desc,
					picPath);
		
			if ( db.inset(sql) < 0)  
				ret = -1;
			else 
				db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			
			if ( cursor != null || !cursor.isClosed() )
				cursor.close();
		}
		
		return StatusCode.success;
	}
	
	public int updateDoctor(String userid, String dorNo, String depName, String dorName, String telephone, 
							String jobTitle, String history,String subject, String desc, String picPath) {
		
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;

		String sql = String.format("UPDATE %s SET %s='%s',%s='%s',%s='%s'," +
				"%s='%s',%s='%s',%s='%s',%s='%s',%s='%s'," +
				"%s='%s',%s='%s'"+
				"WHERE %s='%s' AND %s='%s'" ,
			// Table
			DatabaseTable.Doctor.name, 
			// Value
			DatabaseTable.Doctor.colDepName, depName,
			DatabaseTable.Doctor.colDorName, dorName,
			DatabaseTable.Doctor.colTelephone, telephone,
			DatabaseTable.Doctor.colJobTitle, jobTitle,
			DatabaseTable.Doctor.colHistory, history,
			DatabaseTable.Doctor.colSubject, subject,
			DatabaseTable.Doctor.colValid, "NULL",
			DatabaseTable.Doctor.colUpdateDate, new DateTime().getDateTime(),
			DatabaseTable.Doctor.colDesc, desc,
			DatabaseTable.Doctor.colPicPath, picPath,
			// WHERE
			DatabaseTable.Doctor.colUpdateID, updateID,
			DatabaseTable.Doctor.colDorNo, dorNo);
		
		if ( db.update(sql) < 0 ) 
			return StatusCode.WAR_REGISTER_FAIL();
		return StatusCode.success;
	}
	
	public int deleteDoctor(String userid, String dorNo) {
		
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		
		String sql = String.format("DELETE FROM %s WHERE %s='%s' AND %s='%s'", 
						DatabaseTable.Doctor.name,
						DatabaseTable.Doctor.colHospitalNo, hospitalNo,
						DatabaseTable.Doctor.colDorNo, dorNo);
		
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	public int deleteDoctorAll(String userid) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
	
		String sql = String.format("DELETE FROM %s WHERE %s='%s'", 
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, hospitalNo);
		
		sql = String.format("DELETE FROM %s",DatabaseTable.Doctor.name);
		
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	public ContentValues[]  getDoctor(String userid) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.Doctor.name,
							DatabaseTable.Doctor.colUpdateID, updateID);
		
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

	public ContentValues[]  getDoctorByDepName(String userid,String depName) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s' AND %s='%s'", DatabaseTable.Doctor.name,
							DatabaseTable.Doctor.colUpdateID, updateID,
							DatabaseTable.Doctor.colDepName, depName);
		
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
