package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class LoginActivity extends ControllerActivity {

	/**
	 * 以下為imageButton變數
	 */
	private Button button_login;
	
	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initLayout();

		initListeners();
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
		setContentView(R.layout.activity_login);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		//findViewById(R.id.layout_login).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	private void initListeners() {
		button_login = (Button) findViewById(R.id.ImageButton_LoginPage_login);

		button_login.setOnClickListener(login);
	}
	
	/**
	 * login
	 * 
	 * 當你按下登入按鈕，執行對應的操作
	 */
	private ImageButton.OnClickListener login = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			exeLogin(v);
		}
	};
	
	
	private final int LOGIN_TAG = 1;
	
	private void initHandler() {
		handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        switch(msg.what){
		            case LOGIN_TAG:
		            	_exeLoginResult(msg);
		           break;
		        }
		    }  
		};
	}
	
	private void exeLogin(final View v) {
		String userid = ((EditText)
				findViewById(R.id.userAccount)).getText().toString();
		String userpasswd = ((EditText)
				findViewById(R.id.password)).getText().toString();
		
		//	if ( userid == null || userid.equals("") ) 
			//return -1;
		//	else if ( userpasswd == null || userpasswd.equals(""))
			//return -2;
		
		showProgessDialog(LoginActivity.this, "資料讀取中，讀取時間依據您的網路速度而有不同");
		
		initHandler();
	
		_exeLogin(userid, userpasswd);
	}
	
	private void _exeLogin(final String userid,final String userpasswd) {
		new Thread() {
		    public void run() {
		    	Account account = new Account(db);
		    	int status = account.login(userid, userpasswd);
		    	
		    	if ( status == StatusCode.success ) {
		    		setLoginID(userid);
		    		sendMessage(LOGIN_TAG,111,"AAAAA");
		    	} else {
		    		sendMessage(LOGIN_TAG,111,"AAAAA");
		    	}
		    }
		}.start();
	}
	
	private void _exeLoginResult(Message msg) {
		dismissProgresDialog();
		
		changeActivity(LoginActivity.this, LoginActivity.class);
	}
	
	

	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * 設定按下硬體的返回鍵時，要執行的操作。目前這裡讓使用者按下返回鍵時，不執行任何操作
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		//	changeActivity(LoginActivity.this, AuthorizeActivity.class);
		}
		return false;
	}
}
