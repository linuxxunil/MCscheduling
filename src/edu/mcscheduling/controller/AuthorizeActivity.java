package edu.mcscheduling.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.http.CsmpWebService;
import edu.mcscheduling.utilities.StringUtility;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;


public class AuthorizeActivity extends ControllerActivity {

	/***************** SLM Provide Library ****************************/
	String PACKAGE_NAME = "au.project.mact";
	String CLASS_NAME = "au.project.mact.MainActivity";
	String CSP_APP_ID = "csp01";
	String LICENSE;	
	 
	private boolean checkMactInstalled(){
			PackageManager pm = getPackageManager();
			List<ApplicationInfo> m_appList = pm.getInstalledApplications(0);
			boolean mactInstallFlag = false;
			for (ApplicationInfo ai : m_appList) {
				Log.e("PACKAGES",ai.packageName.toString());
				if(ai.packageName.equals(PACKAGE_NAME)){
					mactInstallFlag = true;
					break;
				}
			}
			return mactInstallFlag;
		} 
	 
	 private String getLicense(){
			String license = "";
			String MY_PACKAGE_NAME = getApplicationContext().getPackageName();
			String MY_CLASS_NAME = MY_PACKAGE_NAME +".controller."+  this.getClass().getSimpleName();
			Intent sendIntent = new Intent();
			sendIntent.setAction("android.intent.action.MAIN");
			sendIntent.setClassName(PACKAGE_NAME, CLASS_NAME);
			sendIntent.putExtra("MY_PACKAGE_NAME", MY_PACKAGE_NAME);
			sendIntent.putExtra("MY_CLASS_NAME", MY_CLASS_NAME);
			Log.e("MY_PACKAGE_NAME",MY_PACKAGE_NAME);
			Log.e("MY_CLASS_NAME", MY_CLASS_NAME);
			startActivity(sendIntent);
			this.finish();
			return license;
		}
	
	 /***************** SLM Provide Library ****************************/
	
	private Button button_online;
	private Button button_offline;
	
	// for test
	private int getOfflineDatabase(String smeId,String appId) {
	
		smeId="sme79";
		appId="aaa";
		
		CsmpWebService web = new CsmpWebService();
		String result = web.getAllTableOfApp(smeId, appId);
		
		JSONObject obj;
		String syncDbId = null;
		try {
			obj = new JSONObject(result);
			if ( obj.getInt("STATUS") != 0 )
				return -1;
			
			syncDbId = (String)obj.get("APP_DB_ID");
			
			result = web.slowSyncDatabase(syncDbId, smeId);
			obj = new JSONObject(result);
			
			if ( obj.getInt("STATUS") != 0 )
				return -2;
			
			web.downloadDatabase(syncDbId, dbPath);
				
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return StatusCode.success;
	}
	
	
	
	private boolean isLicenseOK (String license) {
		try {
			JSONObject obj = new JSONObject(license);
			String status = (String)obj.get("STATUS");
			if ( status.equals("507000"))
				return true;
		} catch (Exception e ) {
			return false;
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try{   
	    	 Intent getIntent = getIntent();
	    	 String license = getIntent.getStringExtra("LICENSE");
	    	 if ( license == null || license.equals("")) {
	    		 setDatabaseMode( AccessMode.NONE );
	    	 } else {
	    		 if ( isLicenseOK(license) ) {	    			
	    			 
	    			 switch ( getDatabaseMode() ) {
	    			 case OFFLINE:	
	    				 setAccessDriver(AccessDriver.SQLITE);
	    			 break;
	    			 case ONLINE:	
	    				 setAccessDriver(AccessDriver.MSSQL); 
	    			 break;
	    			 default:
						 setAccessDriver(AccessDriver.SQLITE);
						 //getOfflineDatabase("sme79","aaa");
						break;
	    			 }
	    			 
	    			 Intent intent = new Intent();
	    			 intent.setClass(AuthorizeActivity.this, LoginActivity.class);
	    			 startActivity(intent);
    				 finish();
	    		 } else {
	    			 setDatabaseMode( AccessMode.NONE );
	    			 AlertDialog.Builder builder = new AlertDialog.Builder(
	    						AuthorizeActivity.this);
	    				builder.setTitle("授權失敗");
	    				builder.setMessage("請連訊貴公司資訊人員");
	    				builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
	    					@Override
	    					public void onClick(DialogInterface dialog, int i) {
	    						finish();
	    					}
	    				});
	    				builder.show();
	    		 }
	    	 }
	     }
	     catch (Exception e){
	    	 System.out.println(e.getMessage());
	     }
		
		setLayout();
		
		setListeners();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	private void setLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_authorize);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void setListeners() {
		button_online = (Button) findViewById(R.id.button_Online);
		button_offline = (Button) findViewById(R.id.button_Offline);

		button_online.setOnClickListener(online);
		button_offline.setOnClickListener(offline);
	}
	
	private int getCsmpAuthorize() {
		try{
        	if(checkMactInstalled()){
        		getLicense();
        	}else{
        		return StatusCode.WAR_MACT_UNINSTALLED();
        	}
    	} catch (Exception e){
	    	
	    }
		return StatusCode.success;
	}

	private ImageButton.OnClickListener online = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			getCsmpAuthorize();
			setDatabaseMode( AccessMode.ONLINE );
		}
	};
	
	private ImageButton.OnClickListener offline = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			getCsmpAuthorize();
			setDatabaseMode( AccessMode.OFFLINE );
		}
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					AuthorizeActivity.this);
			builder.setTitle("APP訊息");
			builder.setMessage("真的要離開此APP");
			builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					
				}
			});

			builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int i) {
					finish();
				}
			});
			builder.show();
		}
		return false;
	}
}
