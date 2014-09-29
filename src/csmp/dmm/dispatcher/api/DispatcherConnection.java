package csmp.dmm.dispatcher.api;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class DispatcherConnection implements Connection {
	public String connectionUrl = "";
	public DispatcherConnection(){
		
	}
	public void setConnUrl(String url){
		this.connectionUrl = url;
	}
	
	@Override
	public Statement createStatement() throws SQLException {
		DispStatement DisStmt = new DispStatement();
		DisStmt.setUrl(connectionUrl);
		return (Statement) DisStmt;
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return false;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void close() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void commit() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Blob createBlob() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Clob createClob() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public NClob createNClob() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public SQLXML createSQLXML() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Statement createStatement(int arg0, int arg1) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Statement createStatement(int arg0, int arg1, int arg2)
			throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public boolean getAutoCommit() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return false;
	}
	@Override
	public String getCatalog() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Properties getClientInfo() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public String getClientInfo(String arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public int getHoldability() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return 0;
	}
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return 0;
	}
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public SQLWarning getWarnings() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public boolean isClosed() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return false;
	}
	@Override
	public boolean isReadOnly() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return false;
	}
	@Override
	public boolean isValid(int arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return false;
	}
	@Override
	public String nativeSQL(String arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public CallableStatement prepareCall(String arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2)
			throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1)
			throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1)
			throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1)
			throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
			throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void rollback() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void rollback(Savepoint arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void setAutoCommit(boolean arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void setCatalog(String arg0) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void setHoldability(int holdability) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public Savepoint setSavepoint() throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}
	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		return null;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		System.err.println(" ********************* not implemented ********************* ");
		
	}
}
