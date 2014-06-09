package edu.mcscheduling.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.Transation;
import android.content.ContentValues;

public class DoctorSchedule {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public DoctorSchedule(DatabaseDriver db) {
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

	public int addDoctorSchedule(final String userid, final String dorNo,final String depName, 
			final String schYear, final String schMonth, final String schedule,final String desc) {
		
		
		Object obj =
				db.excuteTransation( new Transation() {
				
				@Override
				public 	Integer execute() throws SQLException {
					String sql = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s) " +
							"VALUES (%s,'%s','%s','%s','%s','%s','%s','%s')",
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
							//DatabaseTable.DoctorSchedule.colDesc,
							// Values
							getHospitalNoSQL(userid),
							depName,
							dorNo,
							schYear,
							schMonth,
							schedule,
							userid,
							new DateTime().getDateTime());
							//desc);

					if ( db.insert(sql) < 0 )
						throw new SQLException();
					return StatusCode.success;
				}
			});
			
			return 0;
	}
	
	public int updateDoctorSchedule(String userid, String dorNo, String depName,
		String schYear, String schMonth, String schedule, String desc) {
		
		String sql = String.format("UPDATE %s SET %s=%s,%s='%s',%s='%s',%s='%s'," +
										"%s='%s',%s='%s',%s='%s'" +
										"WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%s'" ,
			// Table
			DatabaseTable.DoctorSchedule.name, 
			// Value
			DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
			DatabaseTable.DoctorSchedule.colDepName, depName,

			DatabaseTable.DoctorSchedule.colSchYear, schYear,
			DatabaseTable.DoctorSchedule.colSchMonth, schMonth,
			DatabaseTable.DoctorSchedule.colSchedule, schedule,
			DatabaseTable.DoctorSchedule.colUpdateID, userid,
			DatabaseTable.DoctorSchedule.colUpdateDate, new DateTime().getDateTime(),
			//DatabaseTable.DoctorSchedule.colDesc, "NULL",
			// WHERE
			DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
			DatabaseTable.DoctorSchedule.colDorNo, dorNo,
			DatabaseTable.DoctorSchedule.colSchYear, schYear,
			DatabaseTable.DoctorSchedule.colSchMonth, schMonth);

		if ( db.update(sql) < 0 ) 
			return StatusCode.WAR_REGISTER_FAIL();
		return StatusCode.success;
	}
		
	public int deleteDoctorSchedule(String userid, String dorNo) {
		String sql = String.format("DELETE FROM %s WHERE %s=%s AND %s='%s'", 
						DatabaseTable.DoctorSchedule.name,
						DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
						DatabaseTable.DoctorSchedule.colDorNo, dorNo);
		
		if ( db.delete(sql) < 0 ) 
			return -1;
		return StatusCode.success;
	}
	
	public int deleteDoctorScheduleAll(String userid) {
		String sql = String.format("DELETE FROM %s WHERE %s=%s", 
						DatabaseTable.DoctorSchedule.name,
						DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid));
		
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	
	
	
	public ContentValues[]  getDoctorSchedule(String userid) {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s=%s", 
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid));
		
		int rowCount = getDoctorCount(sql);
		
		if ( rowCount <= 0 )
			return null;
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s",
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
	
		return setResultSetToContentValues(rowCount, sql);
	}

	public ContentValues[] getDoctorScheduleByDepName_AND_DorNo_AND_SchYear_SchMonth(String userid, String depName, String dorNo,int year, int month) {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%d' AND %s='%d'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDepName, depName,
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear,year,
				DatabaseTable.DoctorSchedule.colSchMonth,month);
		
		int rowCount = getDoctorCount(sql);
		
		if ( rowCount <= 0 )
			return null;
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%d' AND %s='%d'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDepName, depName,
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear,year,
				DatabaseTable.DoctorSchedule.colSchMonth,month);
	
		return setResultSetToContentValues(rowCount, sql);
	}

	public ContentValues[] getDoctorScheduleByDorNo_ShcYear_ShcMonth(String userid, String dorNo, String year, String month) {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%s'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear, year,
				DatabaseTable.DoctorSchedule.colSchMonth, month);
		
		int rowCount = getDoctorCount(sql);
		
		if ( rowCount <= 0 )
			return null;
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%s'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear, year,
				DatabaseTable.DoctorSchedule.colSchMonth, month);
	
		return setResultSetToContentValues(rowCount, sql);
	}
	
	
}
