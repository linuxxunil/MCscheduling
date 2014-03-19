package edu.mcscheduling.model;

import android.database.Cursor;

public abstract class DatabaseDriver {
	protected Cursor cursor;
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	abstract public int createTable(String sql);
	
	
	abstract public void beginTransaction();
	
	abstract public void endTransaction();

	abstract public void setTransactionSuccessful();
	/**
	 * 
	 * @param sql
	 * @return 
	 */
	abstract public int inset(String sql) ;
	
	/**
	 * 
	 * @param tblName
	 * @param colsName
	 * @param colsValue
	 * @return 
	 */
	abstract public int inset(String tblName, String colsName, String colsValue);
	
	/**
	 * 
	 * @param sql
	 * @return 
	 */
	abstract public Cursor select(String sql);
	
	/**
	 * 
	 * @param tblName
	 * @param cols
	 * @param whereExpr
	 * @return 
	 */
	abstract public Cursor select(String tblName, String cols, String whereExpr);
	
	/**
	 * 
	 * @param table
	 * @return 
	 */
	abstract public int delete(String sql);
	
	/**
	 * 
	 * @return
	 * @return 
	 */
	abstract public int onConnect();
	
	/**
	 * 
	 * @param sql
	 * @return 
	 */
	abstract public int update(String sql) ;
	
	/**
	 * 
	 * @param table
	 * @param repValues
	 * @param whereExpr
	 * @return 
	 */
	abstract public int update(String table, String repValues, String whereExpr);
		
	protected void finalize() {
		if (cursor != null && !cursor.isClosed())
			cursor.close();
	}
}
