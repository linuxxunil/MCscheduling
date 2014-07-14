package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.app.AlertDialog.Builder;
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
		setContentView(R.layout.activity_login);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
	
	private ImageButton.OnClickListener login = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			String userid = ((EditText)
					findViewById(R.id.userAccount)).getText().toString();
			String userpasswd = ((EditText)
					findViewById(R.id.password)).getText().toString();
			
			showProgessDialog(LoginActivity.this, "資料讀取中，讀取時間依據您的網路速度而有不同");
			
			exeLogin(userid, userpasswd);
		}
	};
	
	private final int LOGIN_TAG = 1;
	
	private void initHandler() {
		handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        switch(msg.what){
		            case LOGIN_TAG:
		            	exeLoginResult(msg);
		           break;
		        }
		    }  
		};
	}

	private void exeLogin(final String userid,final String userpasswd) {	
		new Thread() {
		    public void run() {
		    	Account account = new Account(db);
		    	
		    	int status = account.login(userid, userpasswd);
		    	if ( status == StatusCode.success ) {
		    		setLoginID(userid);
		    	}
	    		sendMessage(LOGIN_TAG,status);	
		    }
		}.start();
	}
	
	private void exeLoginResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();
		
		if ( status != StatusCode.success ) {
			Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
			alertDialog.setTitle("提示");
			switch(status){
			case -31102:
				alertDialog.setMessage(String.format("[%d] %s", status, "帳號欄不能為空。"));
				break;
			case -31104:
				alertDialog.setMessage(String.format("[%d] %s", status, "密碼欄不能為空。"));
				break;
			case -31001:
				alertDialog.setMessage(String.format("[%d] %s", status, "登入失敗，您的帳號密碼錯誤。"));
				break;
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status, "連線失敗。"));
				break;
			default:
				alertDialog.setMessage(String.format("[%d] %s", status, "登入失敗，未知錯誤。"));
				break;
			}
			alertDialog.setPositiveButton("確定", null);
			alertDialog.show();
		} else 
			changeActivity(LoginActivity.this, MenuActivity.class);
	}
	
	
	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * 設定按下硬體的返回鍵時，要執行的操作。目前這裡讓使用者按下返回鍵時，不執行任何操作
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
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
