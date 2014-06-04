package edu.mcscheduling.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.mcscheduling.common.StatusCode;
import android.os.AsyncTask;
import android.util.Log;

public class MSconnect extends AsyncTask<Integer, Integer, MSSqlDriver>{
	
	private MSSqlDriver msssql = null;
	private Connection con = null;
	private String UserName = "sa";
	private String Password = "ptch@RS";
	private Statement stmt = null;
	private ResultSet rs = null;

	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected MSSqlDriver doInBackground(Integer... params) {
		
		return msssql;
	}

	
	public int onConnect() {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			con = DriverManager
					.getConnection(
							"jdbc:jtds:sqlserver://175.99.86.134:1433;instance=Cscheduling_SQL;DatabaseName=cscheduling;charset=utf-8",
							UserName, Password);
		} catch (ClassNotFoundException e1) {
			return StatusCode.ERR_JTDS_ERROR();
		} catch (SQLException e) {
			return StatusCode.ERR_MSSQL_CONNECT_ERROR();
		}
		return StatusCode.success;
	}

	
	public int inset(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return StatusCode.success;
	}

	
	public int createTable(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return StatusCode.success;
	}

	
	public ResultSet select(String sql) {
		if (con == null) {
			StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
			return null;
		} else if (sql.isEmpty()) {
			StatusCode.ERR_SQL_SYNTAX_IS_NULL();
			return null;
		}

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return rs;

	}

	public int delete(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return StatusCode.success;
	}

	
	public int update(String sql) {
		if (con == null)
			return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
		else if (sql.isEmpty())
			return StatusCode.ERR_SQL_SYNTAX_IS_NULL();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(sql);
		}
		return StatusCode.success;
	}
	
}
