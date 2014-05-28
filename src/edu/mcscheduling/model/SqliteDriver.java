package edu.mcscheduling.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqldroid.SqldroidConnection;
import org.sqldroid.SqldroidResultSet;
import org.sqldroid.SqldroidStatement;

import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.utilities.StringUtility;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

public class SqliteDriver extends DatabaseDriver {

	private final String dbPath;
	private Connection con = null;
	
	public SqliteDriver(final String dbPath) {
		this.dbPath = dbPath;
	}
	
	@Override
	public int onConnect() {
		try {
			Class.forName("org.sqldroid.SqldroidDriver");
			
			File dir = new File(StringUtility.getDirectory(dbPath));
			
			if ( !dir.exists() && !dir.mkdirs() )
				return StatusCode.ERR_OPEN_DIR(StringUtility.getDirectory(dbPath));
			
			con = (SqldroidConnection) DriverManager
					.getConnection(
					"jdbc:sqldroid:" + this.dbPath	
						);
		} catch (ClassNotFoundException e1) {
			
			System.out.println(e1.getMessage());
			//return StatusCode.ERR_JTDS_ERROR();
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			//return StatusCode.ERR_MSSQL_CONNECT_ERROR();
		}
		return StatusCode.success;
	}
	
	private ResultSet executeQuery(String sql) throws SQLException {
		Statement stat = con.createStatement();
		ResultSet result = stat.executeQuery(sql);
		stat.close();
		return result;
	}
	
	private int executeUpdate(String sql) throws SQLException {
		int result;
		Statement stat = con.createStatement();
		result = stat.executeUpdate(sql);
		stat.close();
		return result;
	} 
	
	@Override
	public int createTable(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		try {
			executeUpdate(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return StatusCode.success;
	}
	
	@Override
	public int inset(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		int result = 0;
		try {
			result = executeUpdate(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		
		if ( result == 0 ) {
			return -1;
		} 
		return StatusCode.success;
	}
	
	@Override
	public int inset(String tblName, String colsName, String colsValue) {
		String sql = String.format("INSERT INTO '%s' (%s) VALUES (%s)", 
				tblName, colsName, colsValue);
		return inset(sql);
	}	
	
	@Override
	public int update(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		int result = 0;
		try {
			executeUpdate(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		
		if ( result == 0 ) {
			return -1;
		} 
		return StatusCode.success;
	}
	
	@Override
	public int update(String table, String repValues, String whereExpr) {
		String sql = String.format("UPDATE '%s' SET %s WHERE %s", table, repValues, whereExpr);
		return update(sql);
	} 
	
	@Override
	public ResultSet select(String sql) {
		if (con == null) {
			StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
			return null;
		} else if (sql.isEmpty()) {
			StatusCode.ERR_SQL_SYNTAX_IS_NULL();
			return null;
		}
		
		ResultSet result = null;
		try {
			result = executeQuery(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
			return null;
		}
		return result;
	}
	
	@Override
	public ResultSet select(String tblName, String cols, String whereExpr) {
		String sql = String.format("SELECT %s FROM %s", cols, tblName);
		
		if ( whereExpr != null ) {
			sql += " WHERE " + whereExpr;
		}
		
		return select( sql );
	}
	
	@Override
	public int delete(String sql) {
		if ( con == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();
		
		int result = 0;
		try {
			result = executeUpdate(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		
		if ( result == 0 ) {
			return -1;
		} 
		return StatusCode.success;
	}


	

	@Override
	public void beginTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransactionSuccessful() {
		// TODO Auto-generated method stub
	}
}
