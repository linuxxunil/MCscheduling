package edu.mcscheduling.model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import edu.mcscheduling.utilities.StringUtility;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class SqliteDriver extends DatabaseDriver{
	
	private static final int version = 1;
	private final String dbPath;
	private SQLiteDatabase db = null;
	
	public SqliteDriver(final String dbPath) throws Exception{
		this.dbPath = dbPath;

		onConnect();
	}
	
	public void onConnect() throws Exception{
		System.out.println("Initial Database: Path = " + dbPath);
		File dir;
		try {
			dir = new File(StringUtility.getDirectory(dbPath));
			
			if ( !dir.exists() && !dir.mkdirs() )
				throw new IOException("Error: mkdir fail");
			
			db = SQLiteDatabase.openDatabase(dbPath, null , SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE  );
		} catch ( IOException e ) {
			throw e;
		} catch ( SQLiteException  e ) {
			throw e;
		} catch ( Exception e ) {
			throw e;
		}
	}
	
	@Override
	@SuppressLint("NewApi")	
	public void createTable(String sql) throws Exception {
		
		if ( db == null || sql.isEmpty() ) return ;
		try {
			db.execSQL(sql);
		} catch ( Exception e ) {
			throw e;
		}
	}
	
	@Override
	@SuppressLint("NewApi")
	public void inset(String sql) throws Exception {
		if ( db == null && sql.isEmpty() ) return;
		try {
			System.out.println(sql);
			db.execSQL(sql);
		} catch ( Exception e ) {
			throw e;
		}
	}

	public void inset(String tblName, String colsName, 
							String colsValue ) throws Exception {
		
		String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", 
										tblName, colsName, colsValue);
	
		this.inset(sql);
	}

	@Override
	public Cursor select(String sql) throws Exception {
		if ( db == null ) return null;
		try {
			cursor = db.rawQuery(sql, null);
		} catch ( Exception e ) {
			throw e;
		}
		return cursor;
	}
	
	public Cursor select(String tblName, String cols, String whereExpr) throws Exception  {
		String sql = String.format("SELECT %s FROM %s", cols, tblName);
		if ( whereExpr != null ) {
			sql += " WHERE " + whereExpr;
		}
		return select( sql );
	}
	
	@Override
	public void delete(String tblName) throws Exception {
		if ( db == null ) return ;
		String sql = "DELETE FROM `"+ tblName+"`";
		try {
			db.execSQL(sql);
		} catch ( Exception e ) {
			throw e;
		} 
	}
	
	protected void finalize() {
		
		if (db != null && db.isOpen())
			db.close();
	}

	@Override
	public void update(String table, String repValues, String whereExpr) throws Exception {
		if ( db == null ) return ;
		
		String sql = String.format("UPDATE %s SET %s WHERE %s");
		
		try {
			db.execSQL(sql);
		} catch ( Exception e ) {
			throw e;
		}
	}
}
