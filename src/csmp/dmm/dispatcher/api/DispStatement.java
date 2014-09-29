package csmp.dmm.dispatcher.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

public class DispStatement implements Statement {
	private String SQL = null;
	// private ObjectInputStream sInput;
	private static ObjectInputStream oin;
	private static InputStream in;
	private static OutputStreamWriter oSW;
	// private static String hostname = "dmm.servehttp.com";
	// private static String hostname = "140.116.247.113:8080/CSMP_DMM";
	// private static String hostname = "localhost:8080/CSMP_DMM";

	private String status = null;
	private ResultSet rsTable = null;
	// private HttpURLConnection httpConn = null;
	private String url = "";
	private URL httpUrl = null;

	// private URL httpurl = null;
	// public String hostname = "";
	public DispStatement() {

	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ResultSet executeQuery(String SQL) {

		try {

			// URL httpurl = new
			// URL("https://140.116.82.17:8463/CSMP_DMM/backup_opportunities.do");
			// URL httpurl = new
			// URL("http://csmp_dmm_chichi.vcap.me/backup_accounts.do");
			// URL httpurl = new
			// URL("http://10.1.1.73:58002/backup_accounts.do");
			// URL httpurl = new
			// URL("http://localhost:8080/CSMP_DMM/executeQuery.do");
			// URL httpurl = new URL("http://" + hostname + "/executeQuery.do");
			// URL httpurl = new
			// URL("http://dmm.servehttp.com/executeQuery.do");
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					// System.out.println("Warning: URL Host: " + urlHostName +
					// " vs. "
					// + session.getPeerHost());
					return true;
				}
			};

			// trustAllHttpsCertificates();
			// HttpsURLConnection.setDefaultHostnameVerifier(hv);
			httpUrl = new URL("http://" + url + "/executeQuery.do");
			HttpURLConnection httpConn = (HttpURLConnection) httpUrl
					.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			// OutputStream out = httpConn.getOutputStream();
			this.SQL = "SQL=" + SQL;
			// this.SQL = "SQL="+URLEncoder.encode(SQL,"UTF-8");
			// oSW = new OutputStreamWriter(out,"UTF-8");
			//
			// oSW.write(this.SQL);
			// oSW.close();
			out.print(this.SQL);
			out.flush();
			out.close();

			// in = new ObjectInputStream(request.getInputStream());
			oin = new ObjectInputStream(httpConn.getInputStream());
			// System.out.println("Object:"+oin);
//			CachedRowSetImpl crs = (CachedRowSetImpl) oin.readObject();
			
			List rsList = (List) oin.readObject();
			List<String> headers = (List<String>) rsList.get(0);
			List<List<Object>> data = (List<List<Object>>) rsList.get(1);
			rsTable = MyResultSet.ListToResultSet(headers, data);
			
			// while(crs.next()){
			// System.out.println("rs:"+crs.getString(0));
			// }
			// rsTable = (ResultSet) in.readObject();
//			rsTable = crs.getOriginal();
			// while(rsTable.next()){
			// System.out.println("rs:"+rsTable.getString(0));
			// }
			oin.close();

			// ObjectInputStream sInput = new ObjectInputStream();
			// sInput.readObject();
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(httpConn.getInputStream()));
			// status = br.readLine();
			// br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsTable;

	}

	public int executeUpdate(String SQL) {
		// public boolean executeUpdate(String SQL){
		int execUpdateResult = 0;
		boolean execResult = false;
		try {
			// URL httpurl = new
			// URL("http://localhost:8080/CSMP_DMM/executeUpdate.do");
			// URL httpurl = new URL("http://" + hostname +
			// "/executeUpdate.do");
			// URL httpurl = new
			// URL("http://dmm.servehttp.com/executeUpdate.do");
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					// System.out.println("Warning: URL Host: " + urlHostName +
					// " vs. "
					// + session.getPeerHost());
					return true;
				}
			};

			// trustAllHttpsCertificates();
			// HttpsURLConnection.setDefaultHostnameVerifier(hv);
			httpUrl = new URL("http://" + url + "/executeUpdate.do");
			HttpURLConnection httpConn = (HttpURLConnection) httpUrl
					.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			this.SQL = "SQL=" + SQL;
			// this.SQL = "SQL="+URLEncoder.encode(SQL,"UTF-8");

			out.print(this.SQL);
			out.flush();
			out.close();

			// oin = new ObjectInputStream(httpConn.getInputStream());
			// System.out.println("Object:"+oin);
			// CachedRowSetImpl crs = (CachedRowSetImpl) oin.readObject();
			// rsTable = crs.getOriginal();

			// ObjectInputStream sInput = new ObjectInputStream();
			// sInput.readObject();
			in = httpConn.getInputStream();
			execUpdateResult = in.read();
			in.close();

			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(httpConn.getInputStream()));
			// String tmp = br.readLine();
			// execUpdateResult = Integer.parseInt(tmp);
			// br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return execUpdateResult;
		return execUpdateResult;
	}

	public boolean execute(String SQL) {
		// public boolean executeUpdate(String SQL){
		boolean execResult = false;
		try {
			// URL httpurl = new
			// URL("http://localhost:8080/CSMP_DMM/executeUpdate.do");
			// URL httpurl = new URL("http://" + hostname +
			// "/executeUpdate.do");
			// URL httpurl = new
			// URL("http://dmm.servehttp.com/executeUpdate.do");
			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					// System.out.println("Warning: URL Host: " + urlHostName +
					// " vs. "
					// + session.getPeerHost());
					return true;
				}
			};

			// trustAllHttpsCertificates();
			// HttpsURLConnection.setDefaultHostnameVerifier(hv);
			httpUrl = new URL("http://" + url + "/execute.do");
			HttpURLConnection httpConn = (HttpURLConnection) httpUrl
					.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			this.SQL = "SQL=" + SQL;
			// this.SQL = "SQL="+URLEncoder.encode(SQL,"UTF-8");

			out.print(this.SQL);
			out.flush();
			out.close();

			// oin = new ObjectInputStream(httpConn.getInputStream());
			// System.out.println("Object:"+oin);
			// CachedRowSetImpl crs = (CachedRowSetImpl) oin.readObject();
			// rsTable = crs.getOriginal();

			// ObjectInputStream sInput = new ObjectInputStream();
			// sInput.readObject();
			in = httpConn.getInputStream();
			execResult = (in.read() == 1) ? true : false;
			in.close();

			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(httpConn.getInputStream()));
			// String tmp = br.readLine();
			// execUpdateResult = Integer.parseInt(tmp);
			// br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return execUpdateResult;
		return execResult;
	}

	// public boolean execute(String sql){
	// return false;
	//
	// }

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		private static final String SERVER_KEY_STORE_PASSWORD = "netdb510";

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			java.security.cert.X509Certificate[] chain = null;
			try {
				TrustManagerFactory tmf = TrustManagerFactory
						.getInstance("SunX509");
				KeyStore tks = KeyStore.getInstance("JKS");
				tks.load(
						this.getClass().getClassLoader()
								.getResourceAsStream("SSL/serverstore.jks"),
						SERVER_KEY_STORE_PASSWORD.toCharArray());
				tmf.init(tks);
				chain = ((X509TrustManager) tmf.getTrustManagers()[0])
						.getAcceptedIssuers();

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return chain;
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBatch(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

}
