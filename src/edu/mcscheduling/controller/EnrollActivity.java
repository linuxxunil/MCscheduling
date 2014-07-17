package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EnrollActivity extends ControllerActivity {

	private Button button_enroll;
	
	private final int ENROLL_TAG = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initHandler();
		
		initLayout();
		
		initListeners();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	private void initLayout() {
		// set layout
		setContentView(R.layout.activity_enroll);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_enroll).requestFocus();
	}

	private void initListeners() {
		button_enroll = (Button) findViewById(R.id.Button_EnrollPage_enroll);

		button_enroll.setOnClickListener(enroll);
	}

	private void initHandler() {
		handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        switch(msg.what){
		            case ENROLL_TAG:
		            	exeEnrollResult(msg);
		           break;
		        }
		    }  
		};
	}
	
	private ImageButton.OnClickListener enroll = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			String userid = ((EditText)
					findViewById(R.id.userAccount)).getText().toString();
			String username = ((EditText)
					findViewById(R.id.userName)).getText().toString();
			String userpasswd = ((EditText)
					findViewById(R.id.password)).getText().toString();
			String userpasswdConfirm = ((EditText)
					findViewById(R.id.passwordConfirm)).getText().toString();
			
			if ( !userpasswd.equals(userpasswdConfirm)) {
				Builder alertDialog = new AlertDialog.Builder(EnrollActivity.this);
				alertDialog.setTitle("提示");
				alertDialog.setMessage(String.format("[%d] %s", 0, "密碼確認不一致。"));
				alertDialog.setPositiveButton("確定", null);
				alertDialog.show();
			}
			
			showProgessDialog(EnrollActivity.this, "註冊中...");

			exeEnroll(userid, username, userpasswd);	
		}
	};
	
	private void exeEnroll(final String userid,final String username, 
												final String userpasswd) {	
		new Thread() {
		    public void run() {
		    	Account account = new Account(db);
		    	int status = account.register(userid, username, userpasswd);
	    		sendMessage(ENROLL_TAG, status);	
		    }
		}.start();
	}
	
	private void exeEnrollResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();
		
		Builder alertDialog = new AlertDialog.Builder(EnrollActivity.this);
		alertDialog.setTitle("提示");
		if ( status != StatusCode.success ) {
			
			switch(status){
			case 31001:
				alertDialog.setMessage(String.format("[%d] %s", status, "使用者已存在。"));
				break;
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
				alertDialog.setMessage(String.format("[%d] %s", status, "未知錯誤。"));
				break;
			}
			alertDialog.setPositiveButton("確定", null);
			alertDialog.show();
		} else {
			alertDialog.setMessage(String.format("[%d] %s", status, "註冊成功。"));
			alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int id) {
	        		changeActivity(EnrollActivity.this, LoginActivity.class);
	        	}
	        });
			alertDialog.show();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			changeActivity(EnrollActivity.this, HomeActivity.class);
		}
		return false;
	}
}
