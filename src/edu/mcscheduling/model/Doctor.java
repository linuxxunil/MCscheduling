package edu.mcscheduling.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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
							String history,String subject, String desc, String picPath) throws NumberFormatException, SQLException {
		
		ResultSet result = null;
		int ret = 0;
		
		try {
			db.beginTransaction();
			Hospital hospital = new Hospital(db);
			String hospitalNo = hospital.getHospitalNo(userid);
			String updateID = hospitalNo;
			
			if ( hospitalNo == null ) {
				if ( result != null || !result.isClosed() )
					result.close();
				return -1;
			}
			
			String sql = String.format("SELECT %s,%s FROM %s WHERE %s='%s' ORDER BY %s DESC", 
					DatabaseTable.Doctor.colDorNo,
					DatabaseTable.Doctor.colHospitalNo,
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo,
					hospitalNo,
					DatabaseTable.Doctor.colDorNo);
		
			result = db.select(sql); 
		
			if ( result == null ) {
				return -1;
			} 
			
			String dorNo = null;
			result.beforeFirst(); 
			if ( result.getFetchSize() == 0 ) {
				dorNo = String.format("%05d", Integer.parseInt("0") + 1);
			} else {
				dorNo = String.format("%05d", Integer.parseInt(result.getString(0)) + 1);
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
			
			if ( result != null || !result.isClosed() )
				result.close();
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
	
	public ContentValues[]  getDoctor(String userid) throws SQLException {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.Doctor.name,
							DatabaseTable.Doctor.colUpdateID, updateID);
		
		ResultSet result = db.select(sql);
		ResultSetMetaData metadata = result.getMetaData();
		
		if ( result == null ) 
			return null;
		
		result.beforeFirst();; 
		int rows = result.getFetchSize();
		if ( rows <= 0 ) {
			if ( !result.isClosed() )
				result.close();
			return null;
		}
		
		int columns = metadata.getColumnCount();
		ContentValues[] content = new ContentValues[rows];
	
		for ( int i=0; i<rows; i++ ) {
			content[i] = new ContentValues();
			for ( int j=0; j<columns; j++ ) {
				content[i].put(metadata.getColumnName(j), result.getString(j));	
			}
			result.next(); 
		}
		
		if ( !result.isClosed() )
			result.close();
		
		return content;
	}

	public ContentValues[]  getDoctorByDepName(String userid,String depName) throws SQLException {
		Hospital hospital = new Hospital(db);
		String hospitalNo = hospital.getHospitalNo(userid);
		String updateID = hospitalNo;
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s' AND %s='%s'", DatabaseTable.Doctor.name,
							DatabaseTable.Doctor.colUpdateID, updateID,
							DatabaseTable.Doctor.colDepName, depName);
		
		ResultSet result = db.select(sql);
		ResultSetMetaData metadata = result.getMetaData();
		
		if ( result == null ) 
			return null;
		
		result.beforeFirst();; 
		int rows = result.getFetchSize();
		if ( rows <= 0 ) {
			if ( !result.isClosed() )
				result.close();
			return null;
		}
		
		int columns = metadata.getColumnCount();
		ContentValues[] content = new ContentValues[rows];
	
		for ( int i=0; i<rows; i++ ) {
			content[i] = new ContentValues();
			for ( int j=0; j<columns; j++ ) {
				content[i].put(metadata.getColumnName(j), result.getString(j));	
			}
			result.next(); 
		}
		
		if ( !result.isClosed() )
			result.close();
		
		return content;
	}
}
