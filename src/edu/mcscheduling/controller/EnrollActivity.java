package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EnrollActivity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_enroll;
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
		setContentView(R.layout.activity_enroll);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_enroll).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_enroll = (ImageButton) findViewById(R.id.ImageButton_EnrollPage_enroll);

		button_enroll.setOnClickListener(enroll);
	}
	
	/**
	 * Controller : Enroll
	 * 
	 * 
	 */
	
	private void handleEnroll(View v) {
		String userid = ((EditText)
				findViewById(R.id.userAccount)).getText().toString();
		String username = ((EditText)
				findViewById(R.id.userName)).getText().toString();
		String userpasswd = ((EditText)
				findViewById(R.id.password)).getText().toString();
		String userpasswdConfirm = ((EditText)
				findViewById(R.id.passwordConfirm)).getText().toString();
		
		if ( userid == null || userid.equals("")) {
			Toast.makeText(getApplicationContext(), "�ϥΪ̱b�����ର��", Toast.LENGTH_LONG)
			.show();
		} else if ( username == null || userpasswd.equals("")) {
			Toast.makeText(getApplicationContext(), "�ϥΪ̩m�W���ର��", Toast.LENGTH_LONG)
			.show();
		} else if ( userpasswd == null || userpasswd.equals("")) {
			Toast.makeText(getApplicationContext(), "�K�X���ର��", Toast.LENGTH_LONG)
			.show();
		} else if ( !userpasswd.equals(userpasswdConfirm)) {
			Toast.makeText(getApplicationContext(), "�K�X�T�{���@�P", Toast.LENGTH_LONG)
			.show();
		} else {
			
			Account account = new Account(db);
	
			if ( account.register(userid, username, userpasswd) == StatusCode.success ) {
				Toast.makeText(getApplicationContext(), "���U���\", Toast.LENGTH_LONG)
				.show();
			
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "���U����:�ϥΪ̤w�s�b", Toast.LENGTH_LONG)
				.show();
			}
		}
	}
	
	/**
	 * enroll
	 * 
	 * ��A���U���U���s�A����������ާ@
	 */
	private ImageButton.OnClickListener enroll = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			handleEnroll(v);			

		}
	};

	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * �Q�n���}app�ɡA�}��optionDialog�A�T�{�ϥΪ̬O�_�u���n���}
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				EnrollActivity.this);
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
			openOptionsDialog_leaveAPP();
		}
		return false;
	}
}
