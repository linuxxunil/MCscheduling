package edu.mcscheduling.model;

import java.sql.ResultSet;

import android.database.Cursor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.IOException;

//import com.mockrunner.mock.jdbc.MockResultSet;

import edu.mcscheduling.common.StatusCode;


public class Databasepointer{

	private int wdb;
	private ResultSet rs = null; 
	private Cursor cr = null;
	private String[] arr = null;
	
	static private enum type{rs,cr};
	
	public Databasepointer(ResultSet cur) throws SQLException{
		ArrayList row = new ArrayList();
		while (cur.next()) {
		    row.add(cur.getString(1));
		    System.out.println("read data:" + cur.getString(1));
		}
		arr = (String[]) row.toArray(new String[row.size()]);
		wdb = 1;
	}

	public Databasepointer(Cursor cur){
		ArrayList row = new ArrayList();
		cur.moveToFirst();
		while (cur.moveToNext()) {
		    row.add(cur.getString(1));
		    System.out.println(cur.getString(1));
		}
		arr = (String[]) row.toArray(new String[row.size()]);
		wdb = 2;
	}
	
	/*public ResultSet getresultset()
	{
		MockResultSet mockResultSet = new MockResultSet("myResultSet");
		return rs;
	}*/
}
