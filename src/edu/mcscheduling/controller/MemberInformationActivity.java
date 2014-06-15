package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;
import edu.mcscheduling.R;
import edu.mcscheduling.model.Account;
import edu.mcscheduling.model.DatabaseTable;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MemberInformationActivity extends ControllerActivity {
	
	private final String[] passQuestionList = {"就讀的國小學校","目前的居住地","喜歡的球類運動","最愛的歌星","最喜歡的車子"};
	private final String[] workTypeList = {"無"};
	
	/**
	 * 以下為Button變數
	 */
	private Button button_save;
	private Button button_uploadIDphoto;
	

	/**
	 * 目前這個Activity
	 */
	public static Activity thisActivity;

	

	/**
	 * 
	 */
	private Account account = null;
	private ContentValues[] content = null;

	private EditText userid = null;
	private EditText username = null;
	private Spinner userpwdquestion = null;
	private EditText userpwdans = null;
	private Spinner workType = null;
	private EditText uservaild = null;
	
	
	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();

		thisActivity = this;

        /**
         *  Dynamically spinner
         */

        
		// Listen for button clicks
		setListeners();
		
		account = new Account(db);
		
		content = account.getMemberInformation(getLoginID());
		
		bindViewComponent();
		
		setValueOfView();
	}

	private void bindViewComponent() {
		userid = (EditText) findViewById(R.id.EditText_MemberInformationPage_email);
		username = (EditText) findViewById(R.id.EditText_MemberInformationPage_username);
		userpwdquestion = (Spinner) findViewById(R.id.Spinner_MemberInformationPage_passwordTip);
		userpwdans = (EditText) findViewById(R.id.EditText_MemberInformationPage_tips);
		workType = (Spinner)findViewById(R.id.Spinner_MemberInformationPage_workPattern);
		uservaild = (EditText) findViewById(R.id.EditText_MemberInformationPage_registrationDate);
		
	}
	
	private void setValueOfView() {
		String strTmp = null;
		
		setSpinner_PasswordTip();
        setSpinner_WorkPattern();
        
        if ( content != null ) {
        	// User ID
        	strTmp = (String)content[0].get(DatabaseTable.User.colUserid);
        	userid.setText(strTmp == null ? "":strTmp);

        	// Username
        	strTmp = (String)content[0].get(DatabaseTable.User.colUsername);
        	username.setText(strTmp == null ? "":strTmp);
        	
        	// User Password Question
        	strTmp = (String)content[0].get(DatabaseTable.User.colUserpwdquestion);
        	
			if ( strTmp != null ) {
				for ( int i=0; i<passQuestionList.length; i++ ) {
					if ( passQuestionList[i].equals(strTmp) ) {
						userpwdquestion.setSelection(i);
						break;
					}
				}
			} else {
				userpwdquestion.setSelection(0);
			}
        	
        	// User Password Answer
        	strTmp = (String)content[0].get(DatabaseTable.User.colUserpwdans);
        	userpwdans.setText(strTmp == null ? "":strTmp);
        	
        	// Work Type
        	//strTmp = (String)content[0].get(DatabaseTable.User.colUserpwdquestion);
        	workType.setSelection(0);
        	
        	// User valid
        	strTmp = (String)content[0].get(DatabaseTable.User.colUservalid);
        	uservaild.setText(strTmp == null ? "":strTmp);
        	
        } else {
        	userid.setText(getLoginID());
        	username.setText("");
        	userpwdquestion.setSelection(0);
        	userpwdans.setText("");
        	workType.setSelection(0);
        	uservaild.setText("");
        }   
	}
	
    private void setSpinner_PasswordTip() {
    	 
        //get reference to the spinner from the XML layout
        Spinner spinner = userpwdquestion;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        for ( int i=0; i<passQuestionList.length; i++ )
        	list.add(passQuestionList[i]);
  
        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinner.setAdapter(dataAdapter);
        //attach the listener to the spinner
        spinner.setOnItemSelectedListener(new UtilitySpinnerOnItemSelectedListener());
        
    }
    
    private void setSpinner_WorkPattern() {
   	 
        //get reference to the spinner from the XML layout
        Spinner spinner = workType;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        for ( int i=0; i<workTypeList.length; i++ )
        	list.add(workTypeList[i]);
  
        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinner.setAdapter(dataAdapter);
        //attach the listener to the spinner
        spinner.setOnItemSelectedListener(new UtilitySpinnerOnItemSelectedListener());
        
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
		setContentView(R.layout.activity_member_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_memberInformation).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		//button_delete = (Button)  findViewById(R.id.button_MemberInformationPage_delete);
		button_save = (Button)  findViewById(R.id.button_MemberInformationPage_save);
		button_uploadIDphoto = (Button)  findViewById(R.id.button_MemberInformationPage_uploadIDphoto);

		//button_delete.setOnClickListener(delete);
		button_save.setOnClickListener(save);
		button_uploadIDphoto.setOnClickListener(uploadIDphoto);
	}

	private Button.OnClickListener uploadIDphoto = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			Toast.makeText(getApplicationContext(),"選擇要上傳的檔案", Toast.LENGTH_LONG).show();
		}
	};


	private Button.OnClickListener save = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			int status = 0;
			
			status = account.setMemberInformation(
					userid.getText().toString(),					// userid
					username.getText().toString(),					// username
					userpwdquestion.getSelectedItem().toString(),	// passwdQuestion
					userpwdans.getText().toString()					// passwdAnswer
					);
	
			if ( status < 0 ) {
				Toast.makeText(getApplicationContext(),"修改失敗", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_LONG).show();
			}	
		}
	};

	/**
	 * onKeyDown(int keyCode, KeyEvent event)
	 * 
	 * 設定按下硬體的返回鍵時，要執行的操作。目前這裡讓使用者按下返回鍵時，不執行任何操作
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(MemberInformationActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
}
