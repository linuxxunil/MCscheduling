package edu.mcscheduling.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import android.content.ContentValues;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.Transation;

public class Doctor {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public Doctor(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
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

		ResultSet rs = db.select(sql);
		int number = 1;
		if ( rs == null ) {
			return number;
		} else {
			try {
				rs.next();
				number = rs.getInt(1) + 1;
			} catch ( Exception e ) {
				return number;
			}

		}
		return number;
	}
	
	public int addDoctor(final String userid,final String depName,final String dorName,final String telephone, 
				final String jobTitle, final String history,final String subject,final String desc,final String picPath) {
		
			Object obj =
				db.excuteTransation( new Transation() {
				
				@Override
				public 	Integer execute() throws SQLException {
		
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
					if ( db.insert(sql) < 0)  
						throw new SQLException();
					return number;
				}
			});
			
			return 0;
			
	}
	
	public int updateDoctor(String userid, String dorNo, String depName, String dorName, String telephone, 
							String jobTitle, String history,String subject, String desc, String picPath) {

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
	
		if ( db.update(sql) < 0 ) 
			return StatusCode.WAR_REGISTER_FAIL();
		return StatusCode.success;
	}
	
	public int deleteDoctor(String userid, String dorNo) {
		
		String sql = String.format("DELETE FROM %s WHERE %s=%s AND %s='%s'", 
						DatabaseTable.Doctor.name,
						DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
						DatabaseTable.Doctor.colDorNo, dorNo);
		System.out.println(sql);
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	public int deleteDoctorAll(String userid) {
		String sql = String.format("DELETE FROM %s WHERE %s=%s", 
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
		
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	
	private int getDoctorCount(String sql) {
		ResultSet rs = db.select(sql);
		int rowCount = 0;
		if ( rs == null ) {
			return rowCount;
		} else {
			try {
				rs.next();
				rowCount = rs.getInt(1);
			} catch ( SQLException e ) {
				rowCount = 0;
			}
		}
		return rowCount;
	}
	
	private ContentValues[] setResultSetToContentValues(int rowCount, String sql) {
		ResultSet rs = db.select(sql);
		
		if ( rs == null ) {
			return null;
		}

		ContentValues[] content = new ContentValues[rowCount];
		try {
			int i = 0;
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				content[i] = new ContentValues();
				for ( int j=1; j<=meta.getColumnCount(); j++ ){
					content[i].put(meta.getColumnName(j),rs.getString(meta.getColumnName(j)));
				}
				i++;
			}
			rs.close();
		}  catch (SQLException e) {
			content = null;
		} catch (Exception e ) {
			content = null;
		}
		return content;
	}
	
	
	public ContentValues[]  getDoctor(String userid) {
		
		String sql =String.format("SELECT COUNT(*) FROM %s WHERE %s=%s",
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
		
		int rowCount = getDoctorCount(sql);
		
		if ( rowCount <= 0 )
			return null;
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s",
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
		
		return setResultSetToContentValues(rowCount, sql);
	}
	

	public ContentValues[]  getDoctorByDepName(String userid,String depName) {
		
		String sql =String.format("SELECT COUNT(*) FROM %s WHERE %s=%s AND %s='%s'",
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.Doctor.colDepName, depName);
		
		int rowCount = getDoctorCount(sql);
		
		if ( rowCount <= 0 )
			return null;
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s AND %s='%s'",
					DatabaseTable.Doctor.name,
					DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid),
					DatabaseTable.Doctor.colDepName, depName);
		
		return setResultSetToContentValues(rowCount, sql);
	}
}
