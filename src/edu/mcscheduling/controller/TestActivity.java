package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class TestActivity extends Activity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private Button button_layout01;
	private Button button_layout02;
	private Button button_layout03;
	private Button button_layout04;
	private Button button_layout05;
	private Button button_layout06;
	private Button button_layout07;
	private Button button_layout08;
	private Button button_layout09;
	private Button button_layout10;
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
		setContentView(R.layout.activity_test);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_layout01 = (Button)findViewById(R.id.button_01);
		button_layout02 = (Button)findViewById(R.id.button_02);
		button_layout03 = (Button)findViewById(R.id.button_03);
		button_layout04 = (Button)findViewById(R.id.button_04);
		button_layout05 = (Button)findViewById(R.id.button_05);
		button_layout06 = (Button)findViewById(R.id.button_06);
		button_layout07 = (Button)findViewById(R.id.button_07);
		button_layout08 = (Button)findViewById(R.id.button_08);
		button_layout09 = (Button)findViewById(R.id.button_09);
		button_layout10 = (Button)findViewById(R.id.button_10);

		
		button_layout01.setOnClickListener(setLayout01);
		button_layout02.setOnClickListener(setLayout02);
		button_layout03.setOnClickListener(setLayout03);
		button_layout04.setOnClickListener(setLayout04);
		button_layout05.setOnClickListener(setLayout05);
		button_layout06.setOnClickListener(setLayout06);
		button_layout07.setOnClickListener(setLayout07);
		button_layout08.setOnClickListener(setLayout08);
		button_layout09.setOnClickListener(setLayout09);
		button_layout10.setOnClickListener(setLayout10);
	}


	private ImageButton.OnClickListener setLayout01 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();			
		}
	};

	private ImageButton.OnClickListener setLayout02 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, EnrollActivity.class);
			startActivity(intent);
			finish();					
		}
	};

	private ImageButton.OnClickListener setLayout03 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();					
		}
	};

	private ImageButton.OnClickListener setLayout04 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();					
		}
	};

	private ImageButton.OnClickListener setLayout05 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, HospitalInformationActivity.class);
			startActivity(intent);
			finish();	
		}
	};

	private ImageButton.OnClickListener setLayout06 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, DoctorInformation_Display_Activity.class);
			startActivity(intent);
			finish();	
		}
	};

	private ImageButton.OnClickListener setLayout07 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, DoctorSchedulingCalendarActivity.class);
			startActivity(intent);
			finish();	
		}
	};

	private ImageButton.OnClickListener setLayout08 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this,ScheduleActivity.class);
			startActivity(intent);
			finish();	
		}
	};

	private ImageButton.OnClickListener setLayout09 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, MemberInformationActivity.class);
			startActivity(intent);
			finish();			
		}
	};

	private ImageButton.OnClickListener setLayout10 = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, RevisePasswordActivity.class);
			startActivity(intent);
			finish();					
		}
	};

	
	
	
	
	
	

	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * �Q�n���}app�ɡA�}��optionDialog�A�T�{�ϥΪ̬O�_�u���n���}
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
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
