package edu.mcscheduling.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.database.DatabaseDriver;
import edu.mcscheduling.database.Transation;

import android.content.ContentValues;

public class Department {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public Department(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}
	
	public int setDepartment(final String updateID,final String depName, final String desc) {
		if ( this.exist == false ) {
			return -1;
		}else if ( updateID == null || updateID.isEmpty() )
			return -2;
		else if ( depName == null || depName.isEmpty() ) 
			return -3;
		else if ( desc == null || desc.isEmpty() ) 
			return -4;
		
		
		Object obj =
		
		db.excuteTransation( new Transation() {
				
			@Override
			public 	Integer execute() throws SQLException {
			
				String sql = String.format("DELETE FROM %s WHERE %s='%s'", 
							DatabaseTable.Department.name,
							DatabaseTable.Department.colUpdateID,
							updateID);
			
				if ( db.delete(sql) < 0 ) {
					return -1;
				} 
				
				String[] depart = depName.split(",");
			
				for ( int i=0; i<depart.length; i++ ) {
					sql = String.format("INSERT INTO %s (%s,%s,%s,%s) " +
						"VALUES ('%05d','%s','%s','%s')",
						DatabaseTable.Department.name,
						DatabaseTable.Department.colHospitalNo,
						DatabaseTable.Department.colDepName,
						DatabaseTable.Department.colUpdateID,
						DatabaseTable.Department.colUpdateDate,
						i+1,
						depart[i],
						updateID,
						new DateTime().getDateTime()
						 );
					System.out.println(sql);
					if ( db.insert(sql) < 0 ) 
						throw new SQLException();
				}
				return 0;
			}
		});
		
		return StatusCode.success;
	}

	private int getDepartmentCount(String userid) {
		String sql = String.format("SELECT count(*) FROM %s,%s WHERE %s=%s AND %s='%s'", 
				DatabaseTable.User.name,DatabaseTable.Department.name,
				DatabaseTable.User.colUserid, DatabaseTable.Department.colUpdateID,
				DatabaseTable.User.colUserid, userid);
		
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
	
	public ContentValues[] getDepartment(String userid) {
		int rowCount = getDepartmentCount(userid);
		
		if ( rowCount <= 0 )
			return null;
		
		
		String sql = String.format("SELECT * FROM %s WHERE %s='%s'", 
					DatabaseTable.Department.name,
					DatabaseTable.Department.colUpdateID, userid);

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
}
