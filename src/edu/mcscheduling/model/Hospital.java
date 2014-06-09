package edu.mcscheduling.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.Transation;

public class Hospital {
	protected static final int Integer = 0;
	private DatabaseDriver db;
	private boolean exist = false;

	public Hospital(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}
	
	private boolean existsUpdateID(String userid) {
		if ( this.exist == false ) {
			return false;
		} else if ( userid == null || userid.isEmpty() ) {
			return false;
		}
		
		String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s='%s'", 
				DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,
				userid);
		
		
		ResultSet rs = null;
		
		try {
			rs = db.select(sql);
			if ( rs == null || !rs.next() || rs.getInt(1) != 1 ) {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			try {
				rs.close();
			} catch ( SQLException e ) {
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

		ResultSet rs = db.select(sql);
		int number = 1;
		if ( rs == null ) {
			return number;
		} else {
			try {
				rs.next();
				 number =  rs.getInt(DatabaseTable.Hospital.colHospitalNo) + 1;
			} catch ( Exception e ) {
				return number;
			}

		}
		return number;
	}
	
	private int createHospitalNo(final String userid) {
		if ( this.exist == false ) {
			
		} else if ( userid == null || userid.isEmpty() ) {
			
		} 
		
		Object obj =
			db.excuteTransation( new Transation() {
			
			@Override
			public 	Integer execute() throws SQLException {
				int number = calcuteHospitalNumber();
				String hospitalNo =  String.format("%05d", number);
				
				String sql = String.format("INSERT INTO %s (%s,%s,%s) VALUES ('%s','%s','%s')",
						DatabaseTable.Hospital.name,
						DatabaseTable.Hospital.colHospitalNo,
						DatabaseTable.Hospital.colHospitalName,
						DatabaseTable.Hospital.colUpdateID,
						hospitalNo, "NULL", userid);

				if ( db.insert(sql) < 0 )
					throw new SQLException();
				return StatusCode.success;
			}
		});
		
		return 0;
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
	
		System.out.println(sql);
		if ( db.update(sql) < 0 ) 
			return StatusCode.WAR_REGISTER_FAIL();
		return StatusCode.success;
	}
		
	public String getHospitalNo(String userid) {
		
		if ( this.exist == false ) {
		} else if ( userid == null || userid.isEmpty() )
			return null;

		String hospitalNo = null;
		String sql = String.format("SELECT %s,%s FROM %s WHERE %s='%s'", 
				DatabaseTable.Hospital.colHospitalNo,
				DatabaseTable.Hospital.colUpdateID,
				DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,
				userid);

		ResultSet rs = db.select(sql);
		
		if ( rs == null ) {
			return null;
		}
		
		try {
			rs.next();
			hospitalNo = rs.getString(DatabaseTable.Hospital.colHospitalNo);
			rs.close();
		} catch (SQLException e) {
			return null;
		} 
		
		return hospitalNo;
	}
	
	/**
	 * ㄧ计嘿 : setHospital </br>
	 * ㄧ计弧 : set Hospital ┮Τ戈癟 </br>
	 * ㄧ计絛ㄒ : None </br>
	 * 
	 * @param updateID
	 * @param hospitalName
	 * @param areaID
	 * @param hospitalPhone
	 * @param hospitalAddress
	 * @param contactName
	 * @param contactPhone
	 * @param depName
	 * @param opdSt1
	 * @param opdEt1
	 * @param opdSt2
	 * @param opdEt2
	 * @param opdSt3
	 * @param opdEt3
	 * @param hispitalSchedule
	 * @param hospitalState
	 * @param picPath
	 * @return
	 */
	public int setHospital(String userid, String hospitalName, String areaID, 
					String hospitalPhone, String hospitalAddress, 
					String contactName, String contactPhone, String depName,
					String opdSt1, String opdEt1, 
					String opdSt2, String opdEt2,
					String opdSt3, String opdEt3,
					String hispitalSchedule,String hospitalState,
					String picPath) {
		
		if ( userid == null || userid.isEmpty() )
			return StatusCode.ERR_PARM_USERID_ERROR();
		else if ( !existsUpdateID(userid) ) {
			createHospitalNo(userid);
		} 
		
		return updateHospital(userid, hospitalName, areaID, 
						hospitalPhone, hospitalAddress,
						contactName, contactPhone, depName,
						opdSt1, opdEt1,  
						opdSt2, opdEt2,
						opdSt3, opdEt3,
						hispitalSchedule, hospitalState,
						picPath);
	}

	private int getHospitalCount(String sql) {
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
	
	/**
	 * ㄧ计嘿 : getHospital </br>
	 * ㄧ计弧 : 眔Hospital┮Τ戈癟 </br>
	 * ㄧ计絛ㄒ : None </br>
	 *
	 * @return ContentValues[]
	 */
	public ContentValues[] getHospital(String userid){
		
		String sql = String.format("SELECT count(*) FROM %s,%s WHERE %s=%s AND %s='%s'", 
				DatabaseTable.User.name,DatabaseTable.Hospital.name,
				DatabaseTable.User.colUserid, DatabaseTable.Hospital.colUpdateID,
				DatabaseTable.User.colUserid, userid);
		
		int rowCount = getHospitalCount(sql);
		
		if ( rowCount <= 0 )
			return null;
		
		sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,userid);
		
		return setResultSetToContentValues(rowCount, sql);
	}

}
