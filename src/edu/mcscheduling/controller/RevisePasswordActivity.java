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

public class RevisePasswordActivity extends ControllerActivity {

	private Button btnNewPasswordConfirm;
	
	private final int REVISE_TAG = 1;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initLayout();

		initListeners();
		
		initHandler();
	}

	private void initLayout() {
		// set layout
		setContentView(R.layout.activity_revise_password);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_revise_password).requestFocus();
	}

	public void initListeners() {
		btnNewPasswordConfirm = (Button) findViewById(R.id.button_RevisePasswordPage_newPasswordConfirm);
		
		btnNewPasswordConfirm.setOnClickListener(bOclNewPasswordConfirm);
		
	}
	

	
	private void initHandler() {
		handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        switch(msg.what){
		            case REVISE_TAG:
		            	revisePasswordResult(msg);
		           break;
		        }
		    }  
		};
	}
	
	private void revisePassword(final String userid,final String oldPassword,final String newPassword) {	
		new Thread() {
		    public void run() {
		    	Account account = new Account(db);
		    	int status = account.changePasswd(userid, oldPassword, newPassword);
	    		sendMessage(REVISE_TAG,status);	
		    }
		}.start();
	}
	
	private void revisePasswordResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();
		
		Builder alertDialog = new AlertDialog.Builder(RevisePasswordActivity.this);
		alertDialog.setTitle("提示");
		if ( status != StatusCode.success ) {
			switch(status){
			case -31002:
				alertDialog.setMessage(String.format("[%d] %s", status, "修改失敗。"));
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
			alertDialog.setPositiveButton("確定", null);
			alertDialog.setMessage(String.format("[%d] %s", status, "修改成功。"));
			alertDialog.show();
		}

	}

	private Button.OnClickListener bOclNewPasswordConfirm = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			String oldPassword = ((EditText)
					findViewById(R.id.oldPassword)).getText().toString();
			String newPassword = ((EditText)
					findViewById(R.id.newPassword)).getText().toString();
			String newPasswordConfirm = ((EditText)
					findViewById(R.id.newPasswordConfirm)).getText().toString();
		
			if ( newPassword.equals(newPasswordConfirm) ) {
				showProgessDialog(RevisePasswordActivity.this, "修改中...");
				revisePassword(getLoginID(), oldPassword, newPassword);
			} else {
				Toast.makeText(v.getContext(), "密碼不一致", Toast.LENGTH_LONG).show();
			}
			
		}
	};
	

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			changeActivity(RevisePasswordActivity.this, MenuActivity.class);
		}
		return true;
	}
}
