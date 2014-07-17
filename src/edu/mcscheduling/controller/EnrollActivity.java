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
				alertDialog.setTitle("����");
				alertDialog.setMessage(String.format("[%d] %s", 0, "�K�X�T�{���@�P�C"));
				alertDialog.setPositiveButton("�T�w", null);
				alertDialog.show();
			}
			
			showProgessDialog(EnrollActivity.this, "���U��...");

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
		alertDialog.setTitle("����");
		if ( status != StatusCode.success ) {
			
			switch(status){
			case 31001:
				alertDialog.setMessage(String.format("[%d] %s", status, "�ϥΪ̤w�s�b�C"));
				break;
			case -31102:
				alertDialog.setMessage(String.format("[%d] %s", status, "�b���椣�ର�šC"));
				break;
			case -31104:
				alertDialog.setMessage(String.format("[%d] %s", status, "�K�X�椣�ର�šC"));
				break;
			case -31001:
				alertDialog.setMessage(String.format("[%d] %s", status, "�n�J���ѡA�z���b���K�X���~�C"));
				break;
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status, "�s�u���ѡC"));
				break;
			default:
				alertDialog.setMessage(String.format("[%d] %s", status, "�������~�C"));
				break;
			}
			alertDialog.setPositiveButton("�T�w", null);
			alertDialog.show();
		} else {
			alertDialog.setMessage(String.format("[%d] %s", status, "���U���\�C"));
			alertDialog.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
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
