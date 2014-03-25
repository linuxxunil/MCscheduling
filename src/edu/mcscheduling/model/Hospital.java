package edu.mcscheduling.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import edu.mcscheduling.common.StatusCode;

/*
class HospitalContent {
	public String hospitalNo;
	public String hospitalName;
	public String areaID;
	public String hospitalPhone;
	public String hospitalAddress;
	public String contactName;
	public String contactPhone;
	public String updateID;
	public String depName;
	public String opdSt1;
	public String opdEt1; 
	public String opdSt2;
	public String opdEt2;
	public String opdSt3;
	public String opdEt3;
	public String hispitalSchedule;
	public String hospitalState;
	public String picPath;
}
*/
public class Hospital {
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
		
		boolean ret;
		String sql = String.format("SELECT %s,%s FROM %s WHERE %s='%s'", 
				DatabaseTable.Hospital.colUpdateID,
				DatabaseTable.Hospital.colHospitalNo,
				DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,
				userid);
		
		Cursor cursor = db.select(sql);
		
		if ( cursor == null )
			return false;
		
		cursor.moveToFirst(); 
		if ( cursor.getCount() == 1 ) 
			ret = true;
		else
			ret = false;
			
		if ( !cursor.isClosed() )
			cursor.close();
		
		return ret;
	}
	
	private int createHospitalNo(String userid) {
		if ( this.exist == false ) {
			
		} else if ( userid == null || userid.isEmpty() ) {
			
		} 
		int ret = StatusCode.success;
		Cursor cursor = null;
		db.beginTransaction();
		
		try {
			String hospitalNo;
			String sql = String.format("SELECT %s FROM %s ORDER BY %s DESC", 
				DatabaseTable.Hospital.colHospitalNo,
				DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colHospitalNo);
		
			cursor = db.select(sql); 
			
			if ( cursor == null ) {
				System.out.println("AAAAAA");
			} 
			
			cursor.moveToFirst(); 
			if ( cursor.getCount() == 0 ) {
				hospitalNo = String.format("%05d", Integer.parseInt("0") + 1);
			} else {
				hospitalNo = String.format("%05d", Integer.parseInt(cursor.getString(0)) + 1);
			}

			sql = String.format("INSERT INTO %s (%s,%s,%s) VALUES ('%s','%s','%s')",
					DatabaseTable.Hospital.name,
					DatabaseTable.Hospital.colHospitalNo,
					DatabaseTable.Hospital.colHospitalName,
					DatabaseTable.Hospital.colUpdateID,
					hospitalNo, "NULL", userid);
			
			if ( db.inset(sql) < 0)  
				ret = 1;
			else 
				db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			if ( cursor != null || !cursor.isClosed() )
				cursor.close();
		}
		return ret;
		
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

		Cursor cursor = db.select(sql);
		
		if ( cursor == null )
			return null;
		
		cursor.moveToFirst(); 
		if ( cursor.getCount() == 1 ) 
			hospitalNo = cursor.getString(0);
			
		if ( !cursor.isClosed() )
			cursor.close();
		
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
			return StatusCode.WAR_USERID_NULL_OR_EMPTY();
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

	/**
	 * ㄧ计嘿 : getHospital </br>
	 * ㄧ计弧 : 眔Hospital┮Τ戈癟 </br>
	 * ㄧ计絛ㄒ : None </br>
	 *
	 * @return ContentValues[]
	 */
	public ContentValues[] getHospital(String userid){
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s'", DatabaseTable.Hospital.name,
				DatabaseTable.Hospital.colUpdateID,userid);
		
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
