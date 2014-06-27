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

public class Department {
	private DatabaseDriver db;
	
	public Department(DatabaseDriver db) {
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
	
	public int setDepartment(final String updateID,final String depName, final String desc) {
		if ( updateID == null || updateID.isEmpty() )
			return Logger.e(this, StatusCode.PARM_UPDATEID_ERROR);
		else if ( depName == null || depName.isEmpty() )
			return Logger.e(this, StatusCode.PARM_DEPART_NAME_ERROR);
		else if ( desc == null || desc.isEmpty() ) 
			return Logger.e(this, StatusCode.PARM_DESC_ERROR);
		
		return db.excuteTransation( new Transation() {
			@Override
			public 	Integer execute(Object retVale) throws Exception {
				int status = 0;
				String sql = String.format("DELETE FROM %s WHERE %s='%s'", 
							DatabaseTable.Department.name,
							DatabaseTable.Department.colUpdateID,
							updateID);
				
				status = db.delete(sql);
				if ( status != StatusCode.success ) {
					return status;
				} 
				
				String[] depart = depName.split(",");
			
				for ( int i=0; i<depart.length; i++ ) {
					sql = String.format("INSERT INTO %s (%s,%s,%s,%s) " +
						"VALUES (%s,'%s','%s','%s')",
						DatabaseTable.Department.name,
						DatabaseTable.Department.colHospitalNo,
						DatabaseTable.Department.colDepName,
						DatabaseTable.Department.colUpdateID,
						DatabaseTable.Department.colUpdateDate,
						getHospitalNoSQL(updateID),
						depart[i],
						updateID,
						new DateTime().getDateTime()
						 );
					//System.out.println(sql);
					status = db.insert(sql);
					if ( status != StatusCode.success ) 
						return status;
				}
				return StatusCode.success;
			}
		}, null);
	}

	private MsContentValues getDepartmentContentValues(int rowCount, String sql) {
		
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
		
	private int getDepartmentCount(String sql) {
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
	
	public MsContentValues getDepartment(String userid) {
		if ( userid == null || userid.isEmpty() )
			return new MsContentValues(Logger.e(this, StatusCode.PARM_USERID_ERROR));
		
		String sql = String.format("SELECT count(*) FROM %s,%s WHERE %s=%s AND %s='%s'", 
				DatabaseTable.User.name,DatabaseTable.Department.name,
				DatabaseTable.User.colUserid, DatabaseTable.Department.colUpdateID,
				DatabaseTable.User.colUserid, userid);
		
		int rowCount = getDepartmentCount(sql);
		if ( rowCount <= 0 )
			return new MsContentValues(Logger.e(this, StatusCode.WAR_DEPARTMENT_NOT_SETTING));
	
		sql = String.format("SELECT * FROM %s WHERE %s='%s'", 
					DatabaseTable.Department.name,
					DatabaseTable.Department.colUpdateID, userid);
		
		return getDepartmentContentValues(rowCount, sql);
	}
}
