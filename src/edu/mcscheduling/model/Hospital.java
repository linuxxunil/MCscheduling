package edu.mcscheduling.model;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import edu.mcscheduling.common.Logger;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.MsResultSet;
import edu.mcscheduling.database.Transation;

public class Hospital {
	protected static final int Integer = 0;
	private DatabaseDriver db;

	public Hospital(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
		} 
	}
	
	private boolean existsUpdateID(String userid) {
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s'", 
				DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,
				userid);
		
		MsResultSet rsVal = new MsResultSet();
		rsVal = db.select(sql);

		try {
			if ( rsVal.rs == null || !rsVal.rs.next() || rsVal.rs.getInt(1) != 1 ) {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				rsVal.rs.close();
			} catch ( Exception e ) {
				// nothing
			}
		}
		return true;
	}
	
	private int calcuteHospitalNumber() {
		String sql = String.format("SELECT %s FROM %s ORDER BY %s DESC", 
				DatabaseTable.Hospital.colHospitalNo,
				DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colHospitalNo);

		MsResultSet rsVal = null;
		rsVal = db.select(sql);
		
		int number = 1;
		if ( rsVal.rs == null ) {
			return number;
		} else {
			try {
				rsVal.rs.next();
				number =  rsVal.rs.getInt(DatabaseTable.Hospital.colHospitalNo) + 1;
			} catch ( Exception e ) {
				return number;
			}
		}
		return number;
	}
	
	private int createHospitalNo(final String userid) {
		return db.excuteTransation(new Transation() {				
			@Override
			public 	Integer execute(Object retValue) throws Exception {
				int number = calcuteHospitalNumber();
				String hospitalNo =  String.format("%05d", number);
				
				String sql = String.format("INSERT INTO %s (%s,%s,%s) VALUES ('%s','%s','%s')",
						DatabaseTable.Hospital.name,
						DatabaseTable.Hospital.colHospitalNo,
						DatabaseTable.Hospital.colHospitalName,
						DatabaseTable.Hospital.colUpdateID,
						hospitalNo, "NULL", userid);

				int status = db.insert(sql);
				if ( status != StatusCode.success )
					return status;
				return StatusCode.success;
			}
		}, null);
	}
	
	private int updateHospital(String userid, String hospitalName, String areaID,
					String hospitalPhone, String hospitalAddress, 
					String contactName, String contactPhone, String depName,
					String opdSt1, String opdEt1, 
					String opdSt2, String opdEt2,
					String opdSt3, String opdEt3,
					String hispitalSchedule,String hospitalState,
					String picPath) {
		
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
							Locale.getDefault());
		
		
		String sql = String.format("UPDATE %s SET %s='%s',%s='%s',%s='%s'," +
									"%s='%s',%s='%s',%s='%s',%s='%s',%s='%s'," +
									"%s='%s',%s='%s',%s='%s',%s='%s',%s='%s'," +
									"%s='%s',%s='%s',%s='%s',%s='%s' WHERE %s='%s'" ,
				// Table
				DatabaseTable.Hospital.name, 
				// Value
				DatabaseTable.Hospital.colHospitalName, hospitalName,
				DatabaseTable.Hospital.colAreaID, areaID,
				DatabaseTable.Hospital.colHospitalPhone, hospitalPhone,
				DatabaseTable.Hospital.colHospitalAddress, hospitalAddress,
				DatabaseTable.Hospital.colContactName, contactName,
				DatabaseTable.Hospital.colContactPhone, contactPhone,
				DatabaseTable.Hospital.colUpdateDate, formatter.format(time),
				DatabaseTable.Hospital.colDepName, depName,
				DatabaseTable.Hospital.colOPD_ST1, opdSt1,
				DatabaseTable.Hospital.colOPD_ET1, opdEt1,
				DatabaseTable.Hospital.colOPD_ST2, opdSt2,
				DatabaseTable.Hospital.colOPD_ET2, opdEt2,
				DatabaseTable.Hospital.colOPD_ST3, opdSt3,
				DatabaseTable.Hospital.colOPD_ET3, opdEt3,
				DatabaseTable.Hospital.colHospitalschedule, hispitalSchedule,
				DatabaseTable.Hospital.colhospitalstate, hospitalState,
				DatabaseTable.Hospital.colPicPath, picPath,
				// WHERE
				DatabaseTable.Hospital.colUpdateID,userid
				);
		
		return db.update(sql);
	}
	
	public int setHospital(String userid, String hospitalName, String areaID, 
					String hospitalPhone, String hospitalAddress, 
					String contactName, String contactPhone, String depName,
					String opdSt1, String opdEt1, 
					String opdSt2, String opdEt2,
					String opdSt3, String opdEt3,
					String hispitalSchedule,String hospitalState,
					String picPath) {
		if ( userid == null || userid.isEmpty() )
			return Logger.e(this, StatusCode.PARM_USERID_ERROR);
		else if ( !existsUpdateID(userid) ) {
			createHospitalNo(userid);
		} 
		
		int status = updateHospital(userid, hospitalName, areaID, 
						hospitalPhone, hospitalAddress,
						contactName, contactPhone, depName,
						opdSt1, opdEt1,  
						opdSt2, opdEt2,
						opdSt3, opdEt3,
						hispitalSchedule, hospitalState,
						picPath);
		
		return  (status < 0)?status:StatusCode.success;
	}

	private int getHospitalCount(String sql) {
		MsResultSet rsVal = new MsResultSet();
		rsVal = db.select(sql);
		
		int rowCount = 0;
		if ( rsVal.status != StatusCode.success || rsVal.rs == null ) {
			return rsVal.status;
		} else {
			try {
				rsVal.rs.next();
				rowCount = rsVal.rs.getInt(1);
			} catch ( Exception e ) {
				return Logger.e(this, StatusCode.ERR_GET_RESULTSET_FAIL);
			}
		}
		return rowCount;
	}
	
	private MsContentValues getHospitalContentValues(int rowCount, String sql) {
		
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
	
	/**
	 * 函數名稱 : getHospital </br>
	 * 函數說明 : 取得Hospital的所有資訊 </br>
	 * 函數範例 : None </br>
	 *
	 * @return ContentValues[]
	 */
	public MsContentValues getHospital(String userid){
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		
		String sql = String.format("SELECT count(*) FROM %s,%s WHERE %s=%s AND %s='%s'", 
				DatabaseTable.User.name,DatabaseTable.Hospital.name,
				DatabaseTable.User.colUserid, DatabaseTable.Hospital.colUpdateID,
				DatabaseTable.User.colUserid, userid);
				
		int rowCount = getHospitalCount(sql);
		
		if ( rowCount < 0 )
			return new MsContentValues(rowCount);
		else if ( rowCount == 0 )
			return new MsContentValues(Logger.e(this, StatusCode.WAR_HOSPITAL_NOT_SETTING));
		
		sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,userid);
		
		return getHospitalContentValues(rowCount, sql);
	}

}
