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
	 * �H�U��imageButton�ܼ�
	 */
	private Button button_login;
	
	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity���_�lfunction(ex main function)
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
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
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
			
			showProgessDialog(LoginActivity.this, "���Ū�����AŪ���ɶ��̾ڱz�������t�צӦ����P");
			
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
			alertDialog.setTitle("����");
			switch(status){
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
				alertDialog.setMessage(String.format("[%d] %s", status, "�n�J���ѡA�������~�C"));
				break;
			}
			alertDialog.setPositiveButton("�T�w", null);
			alertDialog.show();
		} else 
			changeActivity(LoginActivity.this, MenuActivity.class);
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
					LoginActivity.this);
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
