package edu.mcscheduling.controller;

import java.io.IOException;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
import edu.mcscheduling.model.MsContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuActivity extends ControllerActivity {

	/**
	 * 以下為imageButton變數
	 */
	private ImageButton button_hospital_profile;
	private ImageButton button_doctor_profile;
	private ImageButton button_schedule;
	private ImageButton button_registeredinquiry;
	private ImageButton button_membershipInformation;
	private ImageButton button_healthInformation;

	
	private final int MEMBER_TAG = 0;

	
	/**
	 * 目前這個Activity
	 */
	public static Activity thisActivity;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initLayout();
		// Listen for button clicks
		initListeners();
		
		initHandler();
	}

	/**
	 * onCreateOptionsMenu(Menu menu)
	 * 
	 * 設定menu button要執行的操作
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	/**
	 * setLayout()
	 * 
	 * 設定layout
	 */
	private void initLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_menu);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void initListeners() {
		button_hospital_profile      = (ImageButton) findViewById(R.id.ImageButton_MenuPage_hospitalProfile);
		button_doctor_profile	     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_doctorProfile);
		button_schedule			     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_schedule);
		button_registeredinquiry     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_registeredinquiry);
		button_membershipInformation = (ImageButton) findViewById(R.id.ImageButton_MenuPage_membershipInformation);
		button_healthInformation     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_healthInformation);

		button_hospital_profile.setOnClickListener(hospital_profile);      
		button_doctor_profile.setOnClickListener(doctor_profile);	    
		button_schedule.setOnClickListener(schedule);			 
		button_registeredinquiry.setOnClickListener(registeredinquiry);     
		button_membershipInformation.setOnClickListener(membershipInformation); 
		button_healthInformation.setOnClickListener(healthInformation);  
	}
	
	/**
	 * Hospital Information
	 */
	private ImageButton.OnClickListener hospital_profile = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MenuActivity.this, HospitalInformationActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	/**
	 * Doctor Information
	 */
	private ImageButton.OnClickListener doctor_profile = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MenuActivity.this, DoctorInformation_Display_Activity.class);
			startActivity(intent);
			finish();
			
		}
	};

	/**
	 * schedule
	 */
	
	private ImageButton.OnClickListener schedule = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent();
			intent.setClass(MenuActivity.this, DoctorSchedulingCalendarActivity.class);
			startActivity(intent);
			finish();
			
			
		}
	};
	
	
	private ImageButton.OnClickListener registeredinquiry = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MenuActivity.this, ScheduleViewActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MEMBER_TAG:
					exeMemberInformationResult(msg);
					break;
				}
			}
		};
	}
	
	
	private MsContentValues accountContent = null;
	private void exeMemberInformation() {
		new Thread() {
			public void run() {
				Account account = new Account(db);
				accountContent = account.getMemberInformation(getLoginID());
				sendMessage(MEMBER_TAG, accountContent.status);
			}
		}.start();
	}
	
	private void exeMemberInformationResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();
		try {
		if ( accountContent != null ){
		Bundle bundle = new Bundle();
		bundle.putSerializable("accountContent", accountContent);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(MenuActivity.this, MemberInformationActivity.class);
		startActivity(intent);
		finish();
		}
		} catch ( Exception e ) {
			System.out.println("AAAAAA" + e.getMessage());
		}
	
		
	}
	
	
	
	private ImageButton.OnClickListener membershipInformation = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			changeActivity(MenuActivity.this, MemberInformationActivity.class);
		}
	};
	
	
	private ImageButton.OnClickListener healthInformation = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "healthInformation", Toast.LENGTH_LONG).show();
		}
	};

	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * 設定按下硬體的返回鍵時，要執行的操作。目前這裡讓使用者按下返回鍵時，不執行任何操作
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
			alertDialog.setMessage("確定登出");
			
			DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					logoutID();
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			};
			
			alertDialog.setNegativeButton("確定", OkClick );
			alertDialog.setPositiveButton("取消", null );
			alertDialog.show();
		}
		return false;
	}
}
