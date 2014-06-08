package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RevisePasswordActivity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_RevisePasswordback;

	/**
	 * �H�U��Button�ܼ�
	 */
	private Button button_newPasswordConfirm;
	private Button button_newPasswordCancel;
	
	
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
		setContentView(R.layout.activity_revise_password);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_revise_password).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_RevisePasswordback = (ImageButton) findViewById(R.id.ImageButton_RevisePasswordPage_back);
		button_newPasswordConfirm = (Button) findViewById(R.id.button_RevisePasswordPage_newPasswordConfirm);
		button_newPasswordCancel = (Button) findViewById(R.id.button_RevisePasswordPage_newPasswordCancel);

		button_RevisePasswordback.setOnClickListener(back);
		button_newPasswordConfirm.setOnClickListener(newPasswordConfirm);
		button_newPasswordCancel.setOnClickListener(newPasswordCancel);
	}

	/**
	 * back
	 * 
	 * ��A���Uback���s�A��^����(Home)
	 */
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			//for test
			Intent intent = new Intent();
			intent.setClass(RevisePasswordActivity.this, TestActivity.class);
			startActivity(intent);
			finish();			
			
			/*
			Intent intent = new Intent();
			intent.setClass(RevisePasswordActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			*/
		}
	};
	
	
	private void handleRevisePassword(View v) {
		String oldPassword = ((EditText)
				findViewById(R.id.oldPassword)).getText().toString();
		String newPassword = ((EditText)
				findViewById(R.id.newPassword)).getText().toString();
		String newPasswordConfirm = ((EditText)
				findViewById(R.id.newPasswordConfirm)).getText().toString();

		
		if ( oldPassword == null || oldPassword.equals("")) {
			Toast.makeText(getApplicationContext(), "�±K�X���ର��", Toast.LENGTH_LONG)
			.show();
		} else if ( newPassword == null || newPassword.equals("")) {
			Toast.makeText(getApplicationContext(), "�s�K�X���ର��", Toast.LENGTH_LONG)
			.show();
		} else if (!oldPassword.equals(newPassword) ) {
			Toast.makeText(getApplicationContext(), "�K�X���ק�", Toast.LENGTH_LONG)
			.show();
		}else if ( !newPassword.equals(newPasswordConfirm)) {
			Toast.makeText(getApplicationContext(), "�K�X�T�{���@�P", Toast.LENGTH_LONG)
			.show();
		} else {
		
			Account account = new Account(db);
	
			if ( account.changePasswd(getLoginID(), oldPassword, newPassword) == StatusCode.success ) {
				Toast.makeText(getApplicationContext(), "�K�X�󴫦��\", Toast.LENGTH_LONG)
				.show();

			} else {
				Toast.makeText(getApplicationContext(), "�K�X�󴫥���: �нT�{�N�K�X�O�_���T", Toast.LENGTH_LONG)
				.show();
			}
		}
	}


	private Button.OnClickListener newPasswordConfirm = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			handleRevisePassword(v);
			
		}
	};

	private Button.OnClickListener newPasswordCancel = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "newPasswordConfirm", Toast.LENGTH_LONG)
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
				RevisePasswordActivity.this);
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
