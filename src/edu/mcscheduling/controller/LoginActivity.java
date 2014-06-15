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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private Button button_login;

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
		setContentView(R.layout.activity_login);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		//findViewById(R.id.layout_login).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_login = (Button) findViewById(R.id.ImageButton_LoginPage_login);

		button_login.setOnClickListener(login);
	}
	
	
	/**
	 * Controller : Login
	 * 
	 * 
	 */
	
	private void handleLogin(View v) {
		String userid = ((EditText)
				findViewById(R.id.userAccount)).getText().toString();
		String userpasswd = ((EditText)
				findViewById(R.id.password)).getText().toString();
		
		if ( userid == null || userid.equals("")) {
			Toast.makeText(getApplicationContext(), "�ϥΪ̱b�����ର��", Toast.LENGTH_LONG)
			.show();
		} else if ( userpasswd == null || userpasswd.equals("")) {
			Toast.makeText(getApplicationContext(), "�ϥΪ̱K�X���ର��", Toast.LENGTH_LONG)
			.show();
		} else {
		
			Account account = new Account(db);
		
			if ( account.login(userid, userpasswd) == StatusCode.success ) {
				Toast.makeText(getApplicationContext(), "�n�J���\", Toast.LENGTH_LONG)	
				.show();
			
				// keep loginid
				setLoginID(userid);
			
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "�n�J����", Toast.LENGTH_LONG)
				.show();
			}
		}
	}
	
	
	/**
	 * login
	 * 
	 * ��A���U�n�J���s�A����������ާ@
	 */
	private ImageButton.OnClickListener login = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			handleLogin(v);
		}
	};

	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * �]�w���U�w�骺��^��ɡA�n���檺�ާ@�C�ثe�o�����ϥΪ̫��U��^��ɡA���������ާ@
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, AuthorizeActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
}
