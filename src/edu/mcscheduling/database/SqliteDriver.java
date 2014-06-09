package edu.mcscheduling.database;

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
	private Connection conn = null;
	private Statement stmt = null;
	
	
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
			
			conn =  (SqldroidConnection)DriverManager
					.getConnection(
					"jdbc:sqldroid:" + this.dbPath
						);
		}  catch (ClassNotFoundException e) {
			return StatusCode.ERR_JDBC_CLASS_NOT_FOUND();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}
	
	@Override
	public int createTable(String sql) {
		if (conn == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}
	
	@Override
	public int insert(String sql) {
		if (conn == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		
		return StatusCode.success;
	}
	
	@Override
	public int update(String sql) {
		if (conn == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		
		return StatusCode.success;
	}
	

	
	@Override
	public ResultSet select(String sql) {
		if (conn == null) {
			StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
			return null;
		} else if (sql.isEmpty()) {
			StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
			return null;
		}
		
		ResultSet result = null;
		try {
			stmt = conn.createStatement();
			result =stmt.executeQuery(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		}
		return result;
	}
	
	@Override
	public int delete(String sql) {
		if ( conn == null )
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if ( sql.isEmpty() )
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
		
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		
		return StatusCode.success;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	@Override
	public void commit() throws SQLException {
		conn.commit();
		conn.setAutoCommit(true);
	}

	@Override
	public void rollback() throws SQLException {
		conn.rollback();
		conn.setAutoCommit(true);
	}
		
	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	@Override
	public String[] getTables() {
		String[] tables = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = null;
			String sql = "SELECT * FROM sqlite_master WHERE type='table'";
			rs = stmt.executeQuery(sql);
			
			int len=0;
			while (rs.next() ) len++;
		
			if ( len <= 0 ) {
				rs.close();
				return null;
			}
			
			tables = new String[len];
			rs = stmt.executeQuery(sql);
		
			int i = 0;
			while ( rs.next() ) {
				tables[i++] = new String(rs.getString("tbl_name"));
			}
			
			rs.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		} 
		return tables;
	}

	@Override
	public void close() {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				// nothing
			}
		}
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());          
			}
		}
	}
	
	public Object excuteTransation(Transation tran) {
		Object ret = null;
		try {
			setAutoCommit(false);
			ret = tran.execute();
			commit();
		} catch (SQLException e ) {
			try {
				rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	    } finally {
	        try {
				setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return ret;
	}
}
