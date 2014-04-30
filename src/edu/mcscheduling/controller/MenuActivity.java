package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
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
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_back;
	private ImageButton button_hospital_profile;
	private ImageButton button_doctor_profile;
	private ImageButton button_schedule;
	private ImageButton button_registeredinquiry;
	private ImageButton button_membershipInformation;
	private ImageButton button_healthInformation;


	
	/**
	 * �ثe�o��Activity
	 */
	public static Activity thisActivity;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity���_�lfunction(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();

		thisActivity = this;

		// Listen for button clicks
		setListeners();
	}

	/**
	 * onCreateOptionsMenu(Menu menu)
	 * 
	 * �]�wmenu button�n���檺�ާ@
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	/**
	 * setLayout()
	 * 
	 * �]�wlayout
	 */
	private void setLayout() {
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
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_back 				 = (ImageButton) findViewById(R.id.ImageButton_MenuPage_back);
		button_hospital_profile      = (ImageButton) findViewById(R.id.ImageButton_MenuPage_hospitalProfile);
		button_doctor_profile	     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_doctorProfile);
		button_schedule			     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_schedule);
		button_registeredinquiry     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_registeredinquiry);
		button_membershipInformation = (ImageButton) findViewById(R.id.ImageButton_MenuPage_membershipInformation);
		button_healthInformation     = (ImageButton) findViewById(R.id.ImageButton_MenuPage_healthInformation);

		button_back.setOnClickListener(back);
		button_hospital_profile.setOnClickListener(hospital_profile);      
		button_doctor_profile.setOnClickListener(doctor_profile);	    
		button_schedule.setOnClickListener(schedule);			 
		button_registeredinquiry.setOnClickListener(registeredinquiry);     
		button_membershipInformation.setOnClickListener(membershipInformation); 
		button_healthInformation.setOnClickListener(healthInformation);  
	}


	/**
	 * back
	 * 
	 * ��A���Uback���s�A��^����(Home)
	 */
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
			alertDialog.setMessage("�T�w�n�X");
			
			DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					logoutID();
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			};
			
			alertDialog.setNegativeButton("�T�w", OkClick );
			alertDialog.setPositiveButton("����", null );
			alertDialog.show();
		}
	};
	
	
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
			Toast.makeText(getApplicationContext(), "registeredinquiry", Toast.LENGTH_LONG).show();
		}
	};
	
	
	private ImageButton.OnClickListener membershipInformation = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent();
			intent.setClass(MenuActivity.this, MemberInformationActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	
	private ImageButton.OnClickListener healthInformation = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "healthInformation", Toast.LENGTH_LONG).show();
		}
	};

	/**
	 * enroll
	 * 
	 * ��A���U���U���s�A����������ާ@
	 */
	private ImageButton.OnClickListener enroll = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "enroll", Toast.LENGTH_LONG)
					.show();
		}
	};

	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * �Q�n���}app�ɡA�}��optionDialog�A�T�{�ϥΪ̬O�_�u���n���}
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MenuActivity.this);
		builder.setTitle("APP�T��");
		builder.setMessage("�u���n���}��APP");
		builder.setPositiveButton("�T�{", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				thisActivity.finish();
			}
		});

		builder.setNegativeButton("����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int i) {
			}
		});
		builder.show();
	}

	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * �]�w���U�w�骺��^��ɡA�n���檺�ާ@�C�ثe�o�����ϥΪ̫��U��^��ɡA���������ާ@
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do nothing...
		}
		return false;
	}

	// �H�U���ثe�|���ϥΡA�����ӷ|�Ψ쪺function----------------------------------------------

	/**
	 * isNetworkAvailable()
	 * 
	 * �ˬd�ثe���������p
	 * 
	 * @return true indicates the network is available. false indicates the
	 *         network is not available.
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * responseToNetworkStatus()
	 * 
	 * ��ܥثe���������p�A���ϥΪ̪��D
	 */
	public void responseToNetworkStatus() {
		if (isNetworkAvailable() == false) {
			Toast.makeText(getApplicationContext(),
					"Network connection error!!", Toast.LENGTH_LONG).show();
		} else {
			// do nothing...
		}
	}
}
