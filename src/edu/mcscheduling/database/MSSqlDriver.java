package edu.mcscheduling.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;





import android.os.AsyncTask;
import edu.mcscheduling.common.Logger;
import edu.mcscheduling.common.Network;
import edu.mcscheduling.common.StatusCode;

public class MSSqlDriver extends DatabaseDriver {

	private String UserName = "sa";
	private String Password = "ptch@RS";
	private Connection conn = null;
	private Statement stmt = null;
	
	static private enum MODE {
		TRANSACATION,NORMAL
	}
	
	private MODE mode = MODE.NORMAL;
	
	@Override
	public int onConnect() {		
		try {
			int status = Network.isNetworkAvailable();
			if ( status != StatusCode.success ) 
				return status;
			
			if ( mode == MODE.TRANSACATION ) 
				return StatusCode.success;
		
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			conn = DriverManager
					.getConnection(
					"jdbc:jtds:sqlserver://175.99.86.134:1433;instance=Cscheduling_SQL;DatabaseName=cscheduling;charset=utf-8",
					UserName, Password);
		} catch (ClassNotFoundException e1) {
			return Logger.e(this, StatusCode.ERR_JDBC_CLASS_NOT_FOUND);
		} catch (SQLException e) {
			return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}
		return StatusCode.success;
	}

	private int normalInsert(String sql ) {
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... sql) {
				int status =  onConnect();
				if ( status != StatusCode.success ) { 
					return status;
				}
				try {
					stmt = conn.createStatement();
					return stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
				} catch (Exception e ) {
					return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
				}
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
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
	}
	
	@Override
	public int insert(String sql) {
		if (sql == null || sql.isEmpty())
			return  Logger.e(this, StatusCode.PARM_SQL_IS_ERROR);
		
		int retValue = 0;
		System.out.println(sql);
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
				int status =  onConnect();
				if ( status != StatusCode.success ) { 
					return status;
				}
				try {
					stmt = conn.createStatement();
					return stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
				} catch (Exception e ) {
					return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
				}
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
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
		}
	}
	
	@Override
	public int createTable(String sql) {
		if (sql == null || sql.isEmpty())
			return  Logger.e(this, StatusCode.PARM_SQL_IS_ERROR);

		int status = 0;
		if ( mode == MODE.NORMAL ) {
			System.out.println("normal create");
			status = normalCreateTable(sql);
		}else if ( mode == MODE.TRANSACATION) {
			System.out.println("transacation create");
			status = tranCreateTable(sql);
		}
		return status;
	}
	
	private  MsResultSet normalSelect(String sql) {
		AsyncTask<String, Integer, MsResultSet> asyncTask = 
				new AsyncTask<String, Integer, MsResultSet>() {
			@Override
			protected MsResultSet doInBackground(String... sql) {
				int status =  onConnect();
				if ( status != StatusCode.success ) { 
					return new MsResultSet(status);
				}
				
				try {
					stmt = conn.createStatement();
					return new MsResultSet(stmt.executeQuery(sql[0]));
				} catch (SQLException e) {
					return new MsResultSet(
							Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage()));
				} catch (Exception e ) {
					return new MsResultSet(
							Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage()));
				}				
			}	
		}.execute(sql);

		try {
			return (MsResultSet) asyncTask.get();
		} catch (InterruptedException e) {
			//return -2;
		} catch (ExecutionException e) {
			//return -3;
		}
		return null;
	}
	
	private MsResultSet tranSelect(String sql) { 
		try {
			stmt = conn.createStatement();
			return new MsResultSet(stmt.executeQuery(sql));
		} catch (SQLException e) {
			return new MsResultSet(Logger.e(
					this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage()));
		} catch (Exception e ) {
			return new MsResultSet(
					Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage()));
		}	
	}
	
	@Override
	public MsResultSet select(String sql) {
		if (sql == null || sql.isEmpty())
			return  new MsResultSet(Logger.e(this, StatusCode.PARM_SQL_IS_ERROR));
		
		System.out.println(sql);
		
		if ( mode == MODE.NORMAL ) {
			System.out.println("normal select");
			return normalSelect(sql);
		}else {	// mode == MODE.TRANSACATION) 
			System.out.println("transacation select");
			return tranSelect(sql);
		}
	}
	
	private int normalDelete(String sql) {
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... sql) {
				int status =  onConnect();
				if ( status != StatusCode.success ) { 
					return status;
				}
				try {
					stmt = conn.createStatement();
					return stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
				} catch (Exception e ) {
					return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
				}	
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
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
	}
	
	@Override
	public int delete(String sql) {
		if (sql == null || sql.isEmpty())
			return  Logger.e(this, StatusCode.PARM_SQL_IS_ERROR);
		
		System.out.println(sql);
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
				int status =  onConnect();
				if ( status != StatusCode.success ) { 
					return status;
				}
				try {
					stmt = conn.createStatement();
					return stmt.executeUpdate(sql[0]);
				} catch (SQLException e) {
					return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
				} catch (Exception e ) {
					return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
				}	
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
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
		}
	}
	
	@Override
	public int update(String sql) {
		if (sql.isEmpty())
			return  Logger.e(this, StatusCode.PARM_SQL_IS_ERROR);
		
		System.out.println(sql);
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
	protected int setAutoCommit(boolean autoCommit) {
		if ( autoCommit == true ) mode = MODE.NORMAL;
		else mode = MODE.TRANSACATION;
		try {
			conn.setAutoCommit(autoCommit);
		} catch ( SQLException e ) {
			return Logger.e(this, StatusCode.ERR_SET_AUTOCOMMIT_FAIL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
		return StatusCode.success;
	}

	@Override
	protected int commit() {
		try {
			conn.commit();
		} catch ( SQLException e ) {
			return Logger.e(this, StatusCode.ERR_SET_AUTOCOMMIT_FAIL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
		return StatusCode.success;
	}
	
	@Override
	protected int rollback() {
		try {
			conn.rollback();
		} catch ( SQLException e ) {
			return Logger.e(this, StatusCode.ERR_SET_AUTOCOMMIT_FAIL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
		return StatusCode.success;
		
	}
		
	@Override
	protected int getAutoCommit() {
		int status = StatusCode.success;
		try {
			if ( !conn.getAutoCommit() )
				status = Logger.e(this, StatusCode.ERR_GET_AUTOCOMMIT_FAIL);
		} catch ( SQLException e ) {
			return Logger.e(this, StatusCode.ERR_SET_AUTOCOMMIT_FAIL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
		return status;
	}

	@Override
	public int close() {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
			}
		}
		
		if(conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
			}
		}
		return StatusCode.success;
	}

	@Override
	public int getTables(String[] tables) {
		
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
				//Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
			}
			
			tables = new String[len];
			rs = meta.getTables(null, null, "%", null);
			
			int i = 0;
			while ( i<len && rs.next() ) {
				tables[i++] = new String(rs.getString("TABLE_NAME"));
			}
			
			rs.close();
		} catch (SQLException e) {
			Logger.e(this, StatusCode.ERR_SQL_SYNTAX_IS_ILLEGAL, e.getMessage());
		} catch (Exception e ) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}	
		return StatusCode.success;
	}

	public Integer excuteTransation(final Transation tran,final Object retValue) {
		AsyncTask<String, Integer, Object> asyncTask = 
				new AsyncTask<String, Integer, Object >() {
			@Override
			protected Object doInBackground(String... arg) {
				int status = StatusCode.success ; 
				try {
					status = onConnect();
					if ( status != StatusCode.success ) 
						return status;
					
					status = setAutoCommit(false);
					if ( status != StatusCode.success )
						return status;
				
					status = tran.execute(retValue);
					if ( status != StatusCode.success ) {
						int status1 = rollback();	
						status = (status1 != StatusCode.success)?status1:status;
					} else {
						status = commit();
						if ( status != StatusCode.success ) {
							int status1 = rollback();	
							status = (status1 != StatusCode.success)?status1:status;
						}
					}
				} catch ( Exception e ) {
					int status1 = rollback();	
					status = (status1 != StatusCode.success)?status1:
						Logger.e(this, StatusCode.ERR_EXE_TRANSCATION_FAIL, e.getMessage());
				} 
				
				int status1 = setAutoCommit(true);
				return ( status1 != StatusCode.success )?status1:status;
			}
		}.execute();
		
		try {
			return (Integer) asyncTask.get();
		} catch (InterruptedException e) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		} catch (ExecutionException e) {
			return Logger.e(this, StatusCode.ERR_UNKOWN_ERROR, e.getMessage());
		}
	} 
}
