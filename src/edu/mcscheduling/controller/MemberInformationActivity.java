package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;
import edu.mcscheduling.R;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MemberInformationActivity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_back;
	
	/**
	 * �H�U��Button�ܼ�
	 */
	private Button button_delete;
	private Button button_save;
	private Button button_uploadIDphoto;
	

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

        /**
         *  Dynamically spinner
         */
        createSpinnerPasswordTip();
        createSpinnerWorkPattern();
        
		// Listen for button clicks
		setListeners();
	}

    private void createSpinnerPasswordTip() {
    	 
        //get reference to the spinner from the XML layout
        Spinner spinner = (Spinner) findViewById(R.id.Spinner_MemberInformationPage_passwordTip);
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        list.add("�L");
        list.add("�NŪ����p�Ǯ�");
        list.add("�NŪ������");
        list.add("�ӤH�ͤ�");
        list.add("�ӤH�X�ͦa");
  
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
    
    private void  createSpinnerWorkPattern() {
   	 
        //get reference to the spinner from the XML layout
        Spinner spinner = (Spinner) findViewById(R.id.Spinner_MemberInformationPage_workPattern);
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        list.add("�L");
        list.add("��¾");
        list.add("��¾");
        list.add("��L");
  
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
		setContentView(R.layout.activity_member_information);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_memberInformation).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_MemberInformationPage_back);
		button_delete = (Button)  findViewById(R.id.button_MemberInformationPage_delete);
		button_save = (Button)  findViewById(R.id.button_MemberInformationPage_save);
		button_uploadIDphoto = (Button)  findViewById(R.id.button_MemberInformationPage_uploadIDphoto);

		button_back.setOnClickListener(back);
		button_delete.setOnClickListener(delete);
		button_save.setOnClickListener(save);
		button_uploadIDphoto.setOnClickListener(uploadIDphoto);
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
			intent.setClass(MemberInformationActivity.this, TestActivity.class);
			startActivity(intent);
			finish();
			
			/*
			Intent intent = new Intent();
			intent.setClass(EnrollActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			*/
		}
	};

	private Button.OnClickListener uploadIDphoto = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),"��ܭn�W�Ǫ��ɮ�", Toast.LENGTH_LONG).show();
		}
	};
	
	private Button.OnClickListener delete = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),"delete", Toast.LENGTH_LONG).show();
		}
	};
	

	private Button.OnClickListener save = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),"save", Toast.LENGTH_LONG).show();
		}
	};
	
	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * �Q�n���}app�ɡA�}��optionDialog�A�T�{�ϥΪ̬O�_�u���n���}
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MemberInformationActivity.this);
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
