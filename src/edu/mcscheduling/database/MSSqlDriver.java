package edu.mcscheduling.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import edu.mcscheduling.common.Network;
import edu.mcscheduling.common.StatusCode;




public class MSSqlDriver extends DatabaseDriver {

	private String UserName = "sa";
	private String Password = "ptch@RS";
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	static private enum MODE {
		TRANSACATION,NORMAL
	}
	
	private MODE mode = MODE.NORMAL;
	
	@Override
	public int onConnect() {		
		try {
			if ( mode == MODE.TRANSACATION ) 
				return StatusCode.success;
			else if ( !Network.isNetworkAvailable() ) 
				return StatusCode.ERR_NETWORK_ISNOT_AVAILABLE();
			
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			conn = DriverManager
						.getConnection(
						"jdbc:jtds:sqlserver://175.99.86.134:1433;instance=Cscheduling_SQL;DatabaseName=cscheduling;charset=utf-8",
						UserName, Password);
		} catch (ClassNotFoundException e1) {
			return StatusCode.ERR_JDBC_CLASS_NOT_FOUND();
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}

	private int normalInsert(String sql ) {
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... sql) {
				if ( onConnect() != StatusCode.success ) { 
					return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
				}
				try {
					stmt = conn.createStatement();
					stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
				}
				return StatusCode.success;
			}	
		}.execute(sql);
			
		try {
			return (int) asyncTask.get();
		} catch (InterruptedException e) {
			return -1;
		} catch (ExecutionException e) {
			return -1;
		}
	}
	
	private int tranInsert(String sql) {
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
		if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
		int retValue = 0;
		if ( mode == MODE.NORMAL ) {
			System.out.println("normal insert");
			retValue = normalInsert(sql);
		}else if ( mode == MODE.TRANSACATION) {
			System.out.println("transacation insert");
			retValue = tranInsert(sql);
		}
		return retValue;
	}


	private int normalCreateTable(String sql) {
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... sql) {
				if ( onConnect() != StatusCode.success ) { 
					return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
				}
				try {
					stmt = conn.createStatement();
					stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
				}
				return StatusCode.success;
			}	
		}.execute(sql);
		
		try {
			return (int) asyncTask.get();
		} catch (InterruptedException e) {
			return -1;
		} catch (ExecutionException e) {
			return -1;
		}
	}
	
	private int tranCreateTable(String sql) {
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}
	
	@Override
	public int createTable(String sql) {
		if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		int retValue = 0;
		if ( mode == MODE.NORMAL ) {
			System.out.println("normal create");
			retValue = normalCreateTable(sql);
		}else if ( mode == MODE.TRANSACATION) {
			System.out.println("transacation create");
			retValue = tranCreateTable(sql);
		}
		return retValue;
	}

	
	private ResultSet normalSelect(String sql) {
		AsyncTask<String, Integer, ResultSet> asyncTask = 
				new AsyncTask<String, Integer, ResultSet>() {
			@Override
			protected ResultSet doInBackground(String... sql) {
				if ( onConnect() != StatusCode.success ) { 
					StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
					return null;
				}
				try {
					stmt = conn.createStatement();
					return stmt.executeQuery(sql[0]);
				} catch (SQLException e) {
					StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
					return null;
				}
			}	
		}.execute(sql);

		try {
			return (ResultSet) asyncTask.get();
		} catch (InterruptedException e) {
			return null;
		} catch (ExecutionException e) {
			return null;
		}
	}
	
	private ResultSet tranSelect(String sql) {
		try {
			stmt = conn.createStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		}
	}
	
	@Override
	public ResultSet select(String sql) {
		if (sql.isEmpty()) {
			StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();
			return null;
		}
		
		ResultSet rs = null;
		
		if ( mode == MODE.NORMAL ) {
			System.out.println("normal select");
			rs = normalSelect(sql);
		}else if ( mode == MODE.TRANSACATION) {
			System.out.println("transacation select");
			rs = tranSelect(sql);
		}
		return rs;
	}
	
	private int normalDelete(String sql) {
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... sql) {
				if ( onConnect() != StatusCode.success ) { 
					return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
				}
				try {
					stmt = conn.createStatement();
					stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
				}
				return StatusCode.success;
			}	
		}.execute(sql);
		
		try {
			return (int) asyncTask.get();
		} catch (InterruptedException e) {
			return -1;
		} catch (ExecutionException e) {
			return -1;
		}
	}
	
	private int tranDelete(String sql) {
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
		}
		return StatusCode.success;
	}
	
	@Override
	public int delete(String sql) {
		if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		int retValue = 0;
		if ( mode == MODE.NORMAL ) {
			retValue = normalDelete(sql);
		}else if ( mode == MODE.TRANSACATION) {
			retValue = tranDelete(sql);
		}
		return retValue;
	}

	private int normalUpdate(String sql) {
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... sql) {
				if ( onConnect() != StatusCode.success ) { 
					return StatusCode.ERR_INITIAL_DB_NOT_SUCCESS();
				}
				try {
					stmt = conn.createStatement();
					stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
				}
				return StatusCode.success;
			}	
		}.execute(sql);
		
		try {
			return (int) asyncTask.get();
		} catch (InterruptedException e) {
			return -1;
		} catch (ExecutionException e) {
			return -1;
		}
	}
	
	private int tranUpdate(String sql) {
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
		if (sql.isEmpty())
			return StatusCode.ERR_PARM_SQL_SYNTAX_IS_NULL();

		int retValue = 0;
		if ( mode == MODE.NORMAL ) {
			System.out.println("normal update");
			retValue = normalUpdate(sql);
		}else if ( mode == MODE.TRANSACATION) {
			System.out.println("transacation update");
			retValue = tranUpdate(sql);
		}
		return retValue;
		
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		if ( autoCommit == true ) 
			mode = MODE.NORMAL;
		else
			mode = MODE.TRANSACATION;
		conn.setAutoCommit(autoCommit);
	}

	@Override
	public void commit() throws SQLException {
		conn.commit();
	}

	@Override
	public void rollback() throws SQLException {
		conn.rollback();
	}
		
	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	@Override
	public void close() {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
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

	@Override
	public String[] getTables() {
		String[] tables = null;
		try {
			DatabaseMetaData  meta = conn.getMetaData();
			
			ResultSet rs = meta.getTables(null, null, "%", null);
			
			int len=0;
			while ( rs.next() ) { 
				if ( rs.getString("TABLE_NAME").equals("CHECK_CONSTRAINTS") )
					break;
				len++;
			}
			
			if ( len <= 0 ) {
				rs.close();
				return null;
			}
			
			tables = new String[len];
			rs = meta.getTables(null, null, "%", null);
			
			int i = 0;
			while ( i<len && rs.next() ) {
				tables[i++] = new String(rs.getString("TABLE_NAME"));
			}
			
			rs.close();
		} catch (SQLException e) {
			StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL(e.getMessage());
			return null;
		}
		return tables;
	}

	
	public Object excuteTransation(final Transation tran) {
		
		
		AsyncTask<String, Integer, Object> asyncTask = 
				new AsyncTask<String, Integer, Object >() {
			@Override
			protected Object doInBackground(String... arg) {
				Object ret = null;
				try {
					if ( onConnect() != StatusCode.success )
						return null;
					
					setAutoCommit(false);
					ret = tran.execute();
					commit();
				} catch (SQLException e ) {
					System.out.println(e.getMessage());
					try {
						rollback();
					} catch (SQLException e1) {
						System.out.println("AAAAAAAAAAA1");
					}
			    } catch ( Exception e ) {
			    	System.out.println("FFFFFFFF");
			    }finally {
			        try {
						setAutoCommit(true);
					} catch (SQLException e) {
						System.out.println("AAAAAAAAAAA2");
					}
			    }
				return ret;
			}
			
		}.execute();
		return asyncTask;
		
		
	}
	
	
	 
}
