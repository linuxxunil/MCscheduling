package edu.mcscheduling.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import edu.mcscheduling.common.Logger;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.MsResultSet;
import edu.mcscheduling.database.Transation;
import android.content.ContentValues;

public class DoctorSchedule {
	private DatabaseDriver db;
	
	public DoctorSchedule(DatabaseDriver db) {
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

	public int addDoctorSchedule(final String userid, final String dorNo,final String depName, 
			final String schYear, final String schMonth, final String schedule,final String desc) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		
		return db.excuteTransation( new Transation() {		
					@Override
					public 	Integer execute(Object retValue) throws Exception {
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
					return db.insert(sql);
				}
			}, null);
	}
	
	public int updateDoctorSchedule(String userid, String dorNo, String depName,
		String schYear, String schMonth, String schedule, String desc) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		
		String sql = String.format("UPDATE %s SET %s=%s,%s='%s',%s='%s',%s='%02d'," +
									"%s='%s',%s='%s',%s='%s'" +
									"WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%s'" ,
									// Table
									DatabaseTable.DoctorSchedule.name, 
									// Value
									DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
									DatabaseTable.DoctorSchedule.colDepName, depName,
									DatabaseTable.DoctorSchedule.colSchYear, schYear,
									DatabaseTable.DoctorSchedule.colSchMonth, Integer.valueOf(schMonth),
									DatabaseTable.DoctorSchedule.colSchedule, schedule,
									DatabaseTable.DoctorSchedule.colUpdateID, userid,
									DatabaseTable.DoctorSchedule.colUpdateDate, new DateTime().getDateTime(),
									//DatabaseTable.DoctorSchedule.colDesc, "NULL",
									// WHERE
									DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
									DatabaseTable.DoctorSchedule.colDorNo, dorNo,
									DatabaseTable.DoctorSchedule.colSchYear, schYear,
									DatabaseTable.DoctorSchedule.colSchMonth, schMonth);
		return db.update(sql);
	}
		
	public int deleteDoctorSchedule(String userid, String dorNo) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		else if ( dorNo == null || dorNo.isEmpty() )
			return Logger.e(this, StatusCode.PARM_DOCTOR_NUM_ERROR);
		
		String sql = String.format("DELETE FROM %s WHERE %s=%s AND %s='%s'", 
						DatabaseTable.DoctorSchedule.name,
						DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
						DatabaseTable.DoctorSchedule.colDorNo, dorNo);
		
		return db.delete(sql);
	}
	
	public int deleteDoctorScheduleAll(String userid) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		
		String sql = String.format("DELETE FROM %s WHERE %s=%s", 
						DatabaseTable.DoctorSchedule.name,
						DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid));
		return db.delete(sql);
	}
	
	private int getDoctorScheduleCount(String sql) {
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
	
	private MsContentValues getDoctorScheduleContentValues(int rowCount, String sql) {
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
	
	public MsContentValues  getDoctorSchedule(String userid) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s=%s", 
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid));
		
		int rowCount = getDoctorScheduleCount(sql);
		if ( rowCount <= 0 ) 
			return new MsContentValues(Logger.e(this, StatusCode.WAR_DOCTORSCHEDULE_NOT_SETTING));
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s",
				DatabaseTable.Doctor.name,
				DatabaseTable.Doctor.colHospitalNo, getHospitalNoSQL(userid));
	
		return getDoctorScheduleContentValues(rowCount, sql);
	}

	public MsContentValues getDoctorScheduleByDepName_AND_DorNo_AND_SchYear_SchMonth(String userid, String depName, String dorNo,int year, int month) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		else if ( depName == null || depName.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_DEPART_NAME_ERROR));
		else if ( dorNo == null || dorNo.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_DOCTOR_NUM_ERROR));
		
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%d' AND %s='%02d'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDepName, depName,
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear,year,
				DatabaseTable.DoctorSchedule.colSchMonth,Integer.valueOf(month));
		
		int rowCount = getDoctorScheduleCount(sql);
		if ( rowCount <= 0 ) 
			return new MsContentValues(Logger.e(this, StatusCode.WAR_DOCTORSCHEDULE_NOT_SETTING));
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%d' AND %s='%02d'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDepName, depName,
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear,year,
				DatabaseTable.DoctorSchedule.colSchMonth,Integer.valueOf(month));
	
		return getDoctorScheduleContentValues(rowCount, sql);
	}

	public MsContentValues getDoctorScheduleByDorNo_ShcYear_ShcMonth(String userid, String dorNo, String year, String month) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		else if ( dorNo == null || dorNo.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_DOCTOR_NUM_ERROR));
		else if ( year == null || year.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_SCH_YEAR_ERROR));
		else if ( month == null || month.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_SCH_MONTH_ERROR));
		
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%02d'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear, year,
				DatabaseTable.DoctorSchedule.colSchMonth, Integer.valueOf(month));
		
		int rowCount = getDoctorScheduleCount(sql);
		
		if ( rowCount <= 0 ) 
			return new MsContentValues(Logger.e(this, StatusCode.WAR_DOCTORSCHEDULE_NOT_SETTING));
		
		sql = String.format("SELECT * FROM %s WHERE %s=%s AND %s='%s' AND %s='%s' AND %s='%02d'",
				DatabaseTable.DoctorSchedule.name,
				DatabaseTable.DoctorSchedule.colHospitalNo, getHospitalNoSQL(userid),
				DatabaseTable.DoctorSchedule.colDorNo, dorNo,
				DatabaseTable.DoctorSchedule.colSchYear, year,
				DatabaseTable.DoctorSchedule.colSchMonth, Integer.valueOf(month));
	
		return getDoctorScheduleContentValues(rowCount, sql);
	}
}
