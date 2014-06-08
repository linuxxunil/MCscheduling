package edu.mcscheduling.model;

import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Hospital;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class DoctorSchedule {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public DoctorSchedule(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}

	public int addDoctorSchedule(String userid, String dorNo, String depName, 
			String schYear, String schMonth, String schedule, String desc) {
		
		Cursor cursor = null;
		
		try {
			db.beginTransaction();
			
			Hospital hospital = new Hospital(db);
			String hospitalNo = hospital.getHospitalNo(userid);
			String updateID = hospitalNo;
			
			String sql = String.format("SELECT %s,%s FROM %s WHERE  %s='%s' ORDER BY %s DESC", 
					DatabaseTable.DoctorSchedule.colDorNo,
					DatabaseTable.DoctorSchedule.colHospitalNo,
					DatabaseTable.DoctorSchedule.name,
					DatabaseTable.DoctorSchedule.colHospitalNo,
					hospitalNo,
					DatabaseTable.DoctorSchedule.colDorNo);
		
			cursor = db.select(sql); 
		
			if ( cursor == null ) {
				return -1;
			} 
			
			/*String dorNo = null;
			
			cursor.moveToFirst(); 
			if ( cursor.getCount() == 0 ) {
				dorNo = String.format("%05d", Integer.parseInt("0") + 1);
			} else {
				dorNo = String.format("%05d", Integer.parseInt(cursor.getString(0)) + 1);
			}*/	
			
			sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) " +
					"VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
					// Columns
					DatabaseTable.DoctorSchedule.name,
					DatabaseTable.DoctorSchedule.colHospitalNo,
					DatabaseTable.DoctorSchedule.colDepName,
					DatabaseTable.DoctorSchedule.colDorNo,
					DatabaseTable.DoctorSchedule.colSchYear,
					DatabaseTable.DoctorSchedule.colSchMonth,
					DatabaseTable.DoctorSchedule.colSchedule,
					DatabaseTable.DoctorSchedule.colUpdateID,
					DatabaseTable.DoctorSchedule.colUpdateDate,
					DatabaseTable.DoctorSchedule.colDesc,
					// Values
					hospitalNo,
					depName,
					dorNo,
					schYear,
					schMonth,
					schedule,
					updateID,
					new DateTime().getDateTime(),
					desc);
		
			if ( db.inset(sql) < 0)  
				return -1;
			else 
				db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			
			if ( cursor != null || !cursor.isClosed() )
				cursor.close();
		}
		
		return StatusCode.success;
	}
	
	public int updateDoctorSchedule(String userid, String dorNo, String depName,
		String schYear, String schMonth, String schedule, String desc) {
		
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("UPDATE %s SET %s='%s',%s='%s',%s='%s',%s='%s'," +
										"%s='%s',%s='%s',%s='%s',%s='%s'" +
										"WHERE %s='%s' AND %s='%s'" ,
			// Table
			DatabaseTable.DoctorSchedule.name, 
			// Value
			DatabaseTable.DoctorSchedule.colHospitalNo, hospitalNo,
			DatabaseTable.DoctorSchedule.colDepName, depName,

			DatabaseTable.DoctorSchedule.colSchYear, schYear,
			DatabaseTable.DoctorSchedule.colSchMonth, schMonth,
			DatabaseTable.DoctorSchedule.colSchedule, schedule,
			DatabaseTable.DoctorSchedule.colUpdateID, updateID,
			DatabaseTable.DoctorSchedule.colUpdateDate, new DateTime().getDateTime(),
			DatabaseTable.DoctorSchedule.colDesc, "NULL",
			// WHERE
			DatabaseTable.DoctorSchedule.colHospitalNo, hospitalNo,
			DatabaseTable.DoctorSchedule.colDorNo, dorNo);

		if ( db.update(sql) < 0 ) 
			return StatusCode.WAR_REGISTER_FAIL();
		return StatusCode.success;
	}
		
	public int deleteDoctorSchedule(String userid, String dorNo) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		
		String sql = String.format("DELETE FROM %s WHERE %s='%s' AND %s='%s'", 
						DatabaseTable.DoctorSchedule.name,
						DatabaseTable.DoctorSchedule.colHospitalNo, hospitalNo,
						DatabaseTable.DoctorSchedule.colDorNo, dorNo);
		
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	public int deleteDoctorScheduleAll(String userid) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		
		String sql = String.format("DELETE FROM %s WHERE %s='%s'", 
						DatabaseTable.DoctorSchedule.name,
						DatabaseTable.DoctorSchedule.colHospitalNo, hospitalNo);
		
		sql = String.format("DELETE FROM %s",DatabaseTable.DoctorSchedule.name);
		
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	public ContentValues[]  getDoctorSchedule(String userid) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.DoctorSchedule.name,
							DatabaseTable.DoctorSchedule.colUpdateID, updateID);
		
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

	public ContentValues[] getDoctorScheduleByDepName_AND_DorNo_AND_SchYear_SchMonth(String userid, String depName, String dorNo,int year, int month) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s' AND %s='%s' AND %s='%s' AND %s='%d' AND %s='%d'",
							DatabaseTable.DoctorSchedule.name,
							DatabaseTable.DoctorSchedule.colUpdateID, updateID,
							DatabaseTable.DoctorSchedule.colDepName, depName,
							DatabaseTable.DoctorSchedule.colDorNo, dorNo,
							DatabaseTable.DoctorSchedule.colSchYear,year,
							DatabaseTable.DoctorSchedule.colSchMonth,month);
		
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

	public ContentValues[] getDoctorScheduleByDorNo_ShcYear_ShcMonth(String userid, String dorNo, String year, String month) {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s' AND %s='%s' AND %s='%s' AND %s='%s'",
							DatabaseTable.DoctorSchedule.name,
							DatabaseTable.DoctorSchedule.colUpdateID, updateID,
							DatabaseTable.DoctorSchedule.colDorNo, dorNo,
							DatabaseTable.DoctorSchedule.colSchYear, year,
							DatabaseTable.DoctorSchedule.colSchMonth, month);
		
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
