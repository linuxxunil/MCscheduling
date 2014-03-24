package edu.mcscheduling.model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import edu.mcscheduling.utilities.StringUtility;
import edu.mcsheduling.common.StatusCode;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class SqliteDriver extends DatabaseDriver{
	
	private static final int version = 1;
	private final String dbPath;
	private SQLiteDatabase db = null;
	
	public SqliteDriver(final String dbPath) {
		this.dbPath = dbPath;
	}
	

	public int onConnect() {
		File dir;
		try {
			dir = new File(StringUtility.getDirectory(dbPath));
			
			if ( !dir.exists() && !dir.mkdirs() )
				return StatusCode.ERR_OPEN_DIR(StringUtility.getDirectory(dbPath));
			
			db = SQLiteDatabase.openDatabase(dbPath, null , SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE  );
		} catch ( SQLiteException  e ) {
			return StatusCode.ERR_OPEN_SQLITE_FILE(dbPath);
		} 
		return StatusCode.success;
	}
	
	@Override
	public void beginTransaction() {
		db.beginTransaction();
	}
	
	@Override
	public void setTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	
	@Override
	public void endTransaction() {
		db.endTransaction();
	}
	
	@Override
	public int createTable(String sql) {
		
		if ( db == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();
		
		try {
			db.execSQL(sql);
		} catch ( SQLiteException e ) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
	
		return StatusCode.success;
	}
	
	@Override
	public int inset(String sql) {
		if ( db == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();
		
		try {
			db.execSQL(sql);
		} catch ( SQLiteException e ) {
			System.out.println(e.getMessage());
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
	
		return StatusCode.success;
	}

	public int inset(String tblName, String colsName, String colsValue ) {
		
		String sql = String.format("INSERT INTO '%s' (%s) VALUES (%s)", 
										tblName, colsName, colsValue);
		return inset(sql);
	}

	@Override
	public Cursor select(String sql) {
		if ( db == null ) {
			StatusCode.ERR_INITIAL_DB_NOT_SUCCESS(); 
			return null;
		} else if ( sql.isEmpty() ) {
			StatusCode.ERR_SQL_SYNTAX_IS_NULL(); 
			return null;
		}
			
		try {
			System.out.println(sql);
			cursor = db.rawQuery(sql, null);
		} catch ( SQLiteException e ) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
			return null;
		}
		return cursor;
	}
	
	public Cursor select(String tblName, String cols, String whereExpr)   {
		String sql = String.format("SELECT %s FROM %s", cols, tblName);
		
		if ( whereExpr != null ) {
			sql += " WHERE " + whereExpr;
		}
		System.out.println(sql);
		return select( sql );
	}
	
	@Override
	public int delete(String sql) {
		if ( db == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();
		
		try {
			db.execSQL(sql);
		} catch ( SQLiteException e ) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		} 
		return StatusCode.success;
	}

	
	/**
	 * ㄧ计嘿 : update
	 * ㄧ计弧 : sql update command for sqlite
	 * ㄧ计絛ㄒ : 
	 * 	sql = "UPDATE Table SET userid='b@b.com' where userid='a@a.com'"
	 * 	update(sql);
	 * 
	 * @param sql
	 * @return 
	 */
	public int update(String sql) {
		if ( db == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();
		
		try {
			System.out.println(sql);
			db.execSQL(sql);
		} catch ( Exception e ) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return StatusCode.success;
	}
	
	
	/**
	 *
	 * ㄧ计嘿 : update
	 * ㄧ计弧 : sql update command for sqlite
	 * ㄧ计絛ㄒ :  
	 * 	String values = String.format("%s='%s'",Table.User.colUserid,"b@b.com");
	 *	String where = String.format("%s='", Table.User.colUserid, "a@a.com");
	 * 	update(DatabaseTable.User.name, values,where);
	 * 
	 * @param table
	 * @param repValues	
	 * @param whereExpr
	 * @return 
	 */
	@Override
	public int update(String table, String repValues, String whereExpr)  {
		String sql = String.format("UPDATE '%s' SET %s WHERE %s", table, repValues, whereExpr);
		System.out.println(sql);
		return update(sql);
	}
	
	protected void finalize() {
		
		if (db != null && db.isOpen())
			db.close();
	}
}
