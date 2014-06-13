package edu.mcscheduling.http;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.utilities.StringUtility;

// for test
public class CsmpWebService {
	private String webSite = "http://mcdm.servehttp.com";
	private URL url = null;
	
	public CsmpWebService() {

	}
	
	private HttpURLConnection onConnect(String serviceName) throws MalformedURLException,IOException {
		URL url = new URL(webSite+"/"+serviceName);
		URLConnection conn = url.openConnection();
		HttpURLConnection httpConn  = (HttpURLConnection)conn;
		try {
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Connection", "Keep-Alive" ) ;
			httpConn.setRequestProperty("Cache-Control", "no-cache" ) ;
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setReadTimeout(50000);
			httpConn.setDoOutput(true); 
			httpConn.setDoInput(true); 
			//httpConn.connect();
		} catch (ProtocolException e) {
			StatusCode.ERR_HTTP_PROTOCOL_ERR(e.getMessage());
			return null;
		}
		return httpConn;
	}
	
	public String getAllTableOfApp(String smeId, String appId) {
		
		final String parm = "appId=" + appId + "&smeId=" + smeId;
		
		AsyncTask<String, Integer, String> asyncTask = 
				new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... parm) {
				System.out.println();
				try {
					HttpURLConnection httpConn = onConnect("GetAllTableOfApp");	
					if ( httpConn == null ) {
						return null;
					}
					OutputStream om = httpConn.getOutputStream();
					om.write(parm[0].getBytes());
				
					int statusCode = httpConn.getResponseCode();

					if ( statusCode != 200 ) 
						return null;
										
					InputStream im = httpConn.getInputStream();
					
					int len;
					byte[] buf = new byte[1024];
					String result = "";
					
					while((len = im.read(buf)) > 0) {
						result += new String(buf,0,len);
					}	
					
					return result;
				
				} catch (MalformedURLException e ){
					System.out.println(e.getMessage());
				} catch (IOException e ) {
					System.out.println(e.getMessage());
				}
				return null;
			}	
		}.execute(parm);
		
		try {
			return (String) asyncTask.get();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public String slowSyncDatabase(String syncDbId, String smeId) {
		
		final String syncRules =  "[" 
			+ "{\"Var[0]\":\""+smeId+"\"},"
			+ "{\"TblName\":\"users\",\"TblSql\":\"SELECT t1.* FROM users t1 WHERE userid=\'$Var[0]\'\"},"
			+ "{\"TblName\":\"Hospital\",\"TblSql\":\"SELECT t2.* FROM users t1,Hospital t2 WHERE userid=\'$Var[0]\' AND t1.HospitalNo=t2.HospitalNo\"}," 
			+ "{\"TblName\":\"DorSchedule\",\"TblSql\":\"SELECT t2.* FROM users t1,DorSchedule t2 WHERE userid=\'$Var[0]\' AND t1.HospitalNo=t2.HospitalNo\"}," 
			+ "{\"TblName\":\"Department\",\"TblSql\":\"SELECT t2.* FROM users t1,Department t2 WHERE userid=\'$Var[0]\' AND t1.HospitalNo=t2.HospitalNo\"}," 
			+ "{\"TblName\":\"Doctor\",\"TblSql\":\"SELECT t2.* FROM users t1,Doctor t2 WHERE userid=\'$Var[0]\' AND t1.HospitalNo=t2.HospitalNo\"},"
			+ "{\"TblName\":\"CodeFile\",\"TblSql\":\"SELECT t2.* FROM users t1,CodeFile t2 WHERE userid=\'$Var[0]\' AND t1.HospitalNo=t2.HospitalNo\"}" 
			+ "]";
		
		final String parm =  "syncRules=" + syncRules 
							+ "&syncDBId=" + syncDbId 
							+ "&smeId=" + smeId;
		
		AsyncTask<String, Integer, String> asyncTask = 
				new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... parm) {
				try {
					HttpURLConnection httpConn = onConnect("SlowSyncDatabase");	
					if ( httpConn == null ) {
						return null;
					}
					
					OutputStream om = httpConn.getOutputStream();
					om.write(parm[0].getBytes());
				
					int statusCode = httpConn.getResponseCode();

					if ( statusCode != 200 )  
						return null;
										
					InputStream im = httpConn.getInputStream();
					
					int len;
					byte[] buf = new byte[1024];
					String result = "";
					
					while((len = im.read(buf)) > 0) {
						result += new String(buf,0,len);
					}	
					
					return result;
				
				} catch (MalformedURLException e ){
					System.out.println(e.getMessage());
				} catch (IOException e ) {
					System.out.println(e.getMessage());
				}
				return null;
			}	
		}.execute(parm);
		
		try {
			return (String) asyncTask.get();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public int downloadDatabase(String dbDownloadLink,final String dbPath) {
	final String parm = "dbDownloadLink=" + dbDownloadLink;
		
		AsyncTask<String, Integer, Integer> asyncTask = 
				new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... parm) {
				System.out.println(parm[0]);
				
				File dir = new File(StringUtility.getDirectory(dbPath));
				if ( !dir.exists() && !dir.mkdirs() )
					return StatusCode.ERR_OPEN_DIR(StringUtility.getDirectory(dbPath));
				
				BufferedOutputStream buff;
				DataOutputStream dbOut;
				try {
					buff = new BufferedOutputStream(new FileOutputStream(dbPath));
					dbOut = new DataOutputStream( buff );
				} catch (FileNotFoundException e1) {
					return StatusCode.ERR_OPEN_SQLITE_FILE(dbPath);
				}
				 
				
				try {
					HttpURLConnection httpConn = onConnect("DownloadDatabase.php");	
					
					if ( httpConn == null ) 
						return StatusCode.ERR_HTTP_CONNECT_ERR();
					
					OutputStream om = httpConn.getOutputStream();
					om.write(parm[0].getBytes());
				
					int statusCode = httpConn.getResponseCode();

					if ( statusCode != 200 )
						return StatusCode.ERR_HTTP_RESPONSE_CODE_ERR(statusCode);
										
					InputStream im = httpConn.getInputStream();
					
					int len;
					byte[] buf = new byte[1024];
					String result = "";
					int sum = 0;
					while((len = im.read(buf)) > 0) {
						dbOut.write(buf,0,len);
					}	
					dbOut.flush();
					dbOut.close();
					im.close();
					om.close();
				
				} catch (MalformedURLException e ){
					return StatusCode.ERR_HTTP_URL_ILLEGAL(e.getMessage());
				} catch (IOException e ) {
					return StatusCode.ERR_HTTP_IO_ERR(e.getMessage());
				} 
				return StatusCode.success;
			}
	
		}.execute(parm);
		
		try {
			return (Integer) asyncTask.get();
		} catch (Exception e) {
			return -1;
		}
	}
}
