package edu.mcscheduling.controller;

import edu.mcau.Mapi;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import edu.mcscheduling.R;
import edu.mcscheduling.common.Logger;
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
import android.widget.Toast;


public class AuthorizeActivity extends ControllerActivity {

	private Mapi mapi;
	private Button button_online;
	private Button button_offline;
	
	private int downloadOfflieDb(String syncDbId) {
	
		CsmpWebService web = new CsmpWebService();
		
		if ( getDatabaseId().equals(syncDbId) ) {
			return StatusCode.success;
		}

		try {
			web.downloadDatabase(syncDbId, dbPath);
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return StatusCode.success;
	}
	/***************** Test ****************************/
	
	
	private boolean isLicenseOK (String license) {
		try {
			
			if ( license.equals("50701000"))
				return true;
		} catch (Exception e ) {
			return false;
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapi = new Mapi(getIntent(), this);
		
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

	private ImageButton.OnClickListener online = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			setDatabaseMode( AccessMode.ONLINE );
			mapi.authorize(Mapi.ONLINE_MODE);
		}
	};
	
	private ImageButton.OnClickListener offline = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			setDatabaseMode( AccessMode.OFFLINE );
			mapi.authorize(Mapi.OFFLINE_MODE);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Mapi.REQUEST_CODE) {
			if(resultCode == RESULT_OK){
				//Bundle bundle = data.getExtras();
				//String jsonStr = bundle.getString("RESPONSE");
				Map<String, String> result = mapi.getAuthorizationResult(data);
				String license =  result.get("STATUS").toString();
				String syncDbId = result.get("DB_DOWNLOAD_LINK").toString();
				
				System.out.println(syncDbId);
				try{   
			    	 
			    	 if ( license == null || license.equals("")) {
			    		 setDatabaseMode( AccessMode.NONE );
			    	 } else {
			    		 if ( isLicenseOK(license) ) {	    			
			    			 
			    			 switch ( getDatabaseMode() ) {
			    			 case OFFLINE:	
			    				 setAccessDriver(AccessDriver.SQLITE);
			    				 //showProgessDialog(AuthorizeActivity.this, "資料讀取中，讀取時間依據您的網路速度而有不同");
			    				 downloadOfflieDb(syncDbId);
			    				 //dismissProgresDialog();
			    			 break;
			    			 case ONLINE:	
			    				 setAccessDriver(AccessDriver.MSSQL); 
			    			 break;
			    			 default:
			    				 setAccessDriver(AccessDriver.MSSQL); 
								break;
			    			 }
			    			 changeActivity(AuthorizeActivity.this, LoginActivity.class);
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
				//textView.setText(result.toString());
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        	
	        }
	    }
	}
	
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
		return true;
	}
}
