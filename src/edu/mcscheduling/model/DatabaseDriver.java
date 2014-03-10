package edu.mcscheduling.model;

import android.database.Cursor;

public abstract class DatabaseDriver {
	protected Cursor cursor;
	
	abstract public void createTable(String sql) throws Exception;
	
	abstract public void inset(String sql) throws Exception;
	
	abstract public void inset(String tblName, String colsName, String colsValue ) throws Exception;
	
	abstract public Cursor select(String sql) throws Exception;
	
	abstract public Cursor select(String tblName, String cols, String whereExpr) throws Exception ;
	
	abstract public void delete(String table) throws Exception;
	
	abstract public void onConnect() throws Exception;
	
	abstract public void update(String table, String repValues, String whereExpr) throws Exception;
		
	protected void finalize() {
		if (cursor != null && !cursor.isClosed())
			cursor.close();
	}
}
