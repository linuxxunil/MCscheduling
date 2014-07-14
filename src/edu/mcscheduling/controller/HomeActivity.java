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
	 * 以下為imageButton變數
	 */
	private Button button_enroll;
	private Button button_login;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
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
	 * 設置每個button被click的時候，要執行的function
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
	 * 當你按下註冊按鈕，執行對應的操作
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
	 * 當你按下登入按鈕，執行對應的操作
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
	 * 想要離開app時，開啟optionDialog，確認使用者是否真的要離開
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
		builder.setTitle("APP訊息");
		builder.setMessage("真的要離開此APP");
		builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int i) {
			}
		});
		builder.show();
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
									HomeActivity.this);
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
