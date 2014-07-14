package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private Button button_enroll;
	private Button button_login;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity���_�lfunction(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();

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
		setContentView(R.layout.activity_home);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_enroll = (Button) findViewById(R.id.Button_HomePage_enroll);
		button_login = (Button) findViewById(R.id.Button_login);

		button_enroll.setOnClickListener(enroll);
		button_login.setOnClickListener(login);
	}

	/**
	 * enroll
	 * 
	 * ��A���U���U���s�A����������ާ@
	 */
	private Button.OnClickListener enroll = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			changeActivity(HomeActivity.this, EnrollActivity.class);
		}
	};

	/**
	 * login
	 * 
	 * ��A���U�n�J���s�A����������ާ@
	 */
	private Button.OnClickListener login = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			changeActivity(HomeActivity.this, LoginActivity.class);
		}
	};

	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * �Q�n���}app�ɡA�}��optionDialog�A�T�{�ϥΪ̬O�_�u���n���}
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		builder.setTitle("APP�T��");
		builder.setMessage("�u���n���}��APP");
		builder.setPositiveButton("�T�{", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				finish();
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
			AlertDialog.Builder builder = new AlertDialog.Builder(
									HomeActivity.this);
			builder.setTitle("APP�T��");
			builder.setMessage("�u���n���}��APP");
			builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					
				}
			});

			builder.setNegativeButton("�T�{", new DialogInterface.OnClickListener() {

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
