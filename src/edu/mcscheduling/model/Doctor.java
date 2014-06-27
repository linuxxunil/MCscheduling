package edu.mcscheduling.model;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import android.content.ContentValues;
import edu.mcscheduling.common.Logger;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.MsResultSet;
import edu.mcscheduling.database.Transation;

public class Doctor {
	private DatabaseDriver db;
	
	public Doctor(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
		} 
	}
	
	private String getHospitalNoSQL(String userid) {
		return String.format("(SELECT %s FROM %s WHERE %s='%s')",
		DatabaseTable.Hospital.colHospitalNo,
		DatabaseTable.Hospital.name,
		DatabaseTable.Hospital.colUpdateID, userid);
	}
	
	private int calcuteDoctorNumber(String userid) {
		String sql = String.format("SELECT %s,%s FROM %s WHERE %s=%s ORDER BY %s DESC", 
				DatabaseTable.Doctor.colDorNo,
				DatabaseTable.Doctor.colHospitalNo,
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.Doctor.colDorNo);

		MsResultSet rsVal = null;
		rsVal = db.select(sql);
		
		int number = 1;
		if ( rsVal.rs == null ) {
			return number;
		} else {
			try {
				rsVal.rs.next();
				number =  rsVal.rs.getInt(1) + 1;
			} catch ( Exception e ) {
				return number;
			}
		}
		return number;
	}
	
	public int addDoctor(final String userid,final String depName,final String dorName,final String telephone, 
				final String jobTitle, final String history,final String subject,final String desc,final String picPath) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		
		return db.excuteTransation( new Transation() {
					@Override
					public 	Integer execute(Object retValue) throws Exception {
						int number = calcuteDoctorNumber(userid);
				
						String dorNo =  String.format("%05d", number);
				
						String sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) " +
							"VALUES (%s,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
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
							//DatabaseTable.Doctor.colDesc,
							DatabaseTable.Doctor.colPicPath,
							// Values
							getHospitalNoSQL(userid),
							depName,
							dorNo,
							dorName,
							telephone,
							jobTitle,
							history,
							subject,
							"0",
							userid,
							new DateTime().getDateTime(),
							//desc,
							picPath);
						System.out.println(sql);
						return db.insert(sql);			
					}
			},null);	
	}
	
	public int updateDoctor(String userid, String dorNo, String depName, String dorName, String telephone, 
							String jobTitle, String history,String subject, String desc, String picPath) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		
		String sql = String.format("UPDATE %s SET %s='%s',%s='%s',%s='%s'," +
				"%s='%s',%s='%s',%s='%s',%s='%s',%s='%s'," +
				"%s='%s',%s='%s'"+
				"WHERE %s=%s AND %s='%s'" ,
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
			DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
			DatabaseTable.Doctor.colDorNo, dorNo);
	
		return db.update(sql);
	}
	
	public int deleteDoctor(String userid, String dorNo) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		else if ( dorNo == null || dorNo.isEmpty() )
			return Logger.e(this, StatusCode.PARM_DOCTOR_NUM_ERROR);
		
		String sql = String.format("DELETE FROM %s WHERE %s=%s AND %s='%s'", 
						DatabaseTable.Doctor.name,
						DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
						DatabaseTable.Doctor.colDorNo, dorNo);
		System.out.println(sql);
		return db.delete(sql);
	}
	
	public int deleteDoctorAll(String userid) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		
		String sql = String.format("DELETE FROM %s WHERE %s=%s", 
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
		
		return db.delete(sql);
	}
	
	private int getDoctorCount(String sql) {
		MsResultSet rsVal = new MsResultSet();
		rsVal = db.select(sql);
		
		int rowCount = 0;
		
		if ( rsVal.status != StatusCode.success) {
			return rowCount;
		} else {
			try {
				rsVal.rs.next();
				rowCount = rsVal.rs.getInt(1);
			} catch ( SQLException e ) {
				rowCount = 0;
			}
		}
		return rowCount;
	}
	
	private MsContentValues getDoctorContentValues(int rowCount, String sql) {
		MsResultSet rsVal = null;
		rsVal = db.select(sql);
		
		if ( rsVal.status != StatusCode.success )
			return new MsContentValues(rsVal.status);
		
		MsContentValues cvValue = new MsContentValues(StatusCode.success);
		cvValue.cv = new ContentValues[rowCount];
		
		try {
			int i = 0;
			ResultSetMetaData meta = rsVal.rs.getMetaData();
			while (rsVal.rs.next()) {
				cvValue.cv[i] = new ContentValues();
				for ( int j=1; j<=meta.getColumnCount(); j++ ){
					cvValue.cv[i].put(meta.getColumnName(j),rsVal.rs.getString(meta.getColumnName(j)));
				}
				i++;
			}
			rsVal.rs.close();
		} catch (SQLException e) {
			return new MsContentValues(Logger.e(this, StatusCode.ERR_GET_MEMBER_INFO_FAIL));
		} catch (Exception e ) {
			return new MsContentValues(Logger.e(this, StatusCode.ERR_UNKOWN_ERROR));
		}
		return cvValue;
	}
	
	public MsContentValues  getDoctor(String userid) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		
		String sql =String.format("SELECT COUNT(*) FROM %s WHERE %s=%s",
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
		
		int rowCount = getDoctorCount(sql);
		if ( rowCount <= 0 ) 
			return new MsContentValues(Logger.e(this, StatusCode.WAR_DOCTOR_NOT_SETTING));
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s",
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
		
		return getDoctorContentValues(rowCount, sql);
	}
	
	public MsContentValues  getDoctorByDepName(String userid, String depName) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		else if ( depName == null || depName.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		
		String sql =String.format("SELECT COUNT(*) FROM %s WHERE %s=%s AND %s='%s'",
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.Doctor.colDepName, depName);
		
		int rowCount = getDoctorCount(sql);
		if ( rowCount <= 0 ) 
			return new MsContentValues(Logger.e(this, StatusCode.WAR_DOCTOR_NOT_SETTING));
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s AND %s='%s'",
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
					DatabaseTable.Doctor.colDepName, depName);
		
		return getDoctorContentValues(rowCount, sql);
	}
}
