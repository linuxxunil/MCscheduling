package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;
import edu.mcscheduling.R;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import edu.mcscheduling.model.Hospital;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorInformation_New_Activity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_back;
	
	/**
	 * �H�U��Button�ܼ�
	 */
	private Button button_selectUploadDoctorPhoto;
	private Button button_cleanAll;
	private Button button_addNewDoctor;

	private TextView doctorNo = null;
	private TextView hospitalNo = null;
	private EditText dorName = null;
	private EditText jobTitle = null; 
	private EditText telephone = null;
	private Spinner  depName = null;
	private EditText history = null;
	private EditText subject = null;
	

	
	private Doctor doctor = null;
	private Department depart = null;
	private ContentValues[] doctorContent = null;
	private ContentValues[] departContent = null;
	
	private UtilitySpinnerOnItemSelectedListener itemSelectedListener;
	
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
		
		
		
		//deBundle(savedInstanceState);
	
		thisActivity = this;
		
		setListeners();
		
		
		doctor = new Doctor(db);
		depart = new Department(db);
		doctorContent = doctor.getDoctor(getLoginID());
		departContent = depart.getDepartment(getLoginID());
		bindViewComponent();
		setValueOfView();
	}
	
	private void bindViewComponent(){
		doctorNo=(TextView)findViewById(R.id.TextView_NewDoctorInformationPage_doctorNumber);
		hospitalNo=(TextView)findViewById(R.id.TextView_NewDoctorInformationPage_hospitalNumber);
		dorName=(EditText)findViewById(R.id.EditText_NewDoctorInformationPage_doctorName); 
		depName=(Spinner)findViewById(R.id.Spinner_newDoctorInformationPage_medicalDepartment); 
		jobTitle=(EditText)findViewById(R.id.EditText_NewDoctorInformationPage_jobTitle);  
		telephone=(EditText)findViewById(R.id.EditText_NewDoctorInformationPage_doctorPhoneNumber);
		history=(EditText)findViewById(R.id.EditText_NewDoctorInformationPage_experience);
		subject=(EditText)findViewById(R.id.EditText_NewDoctorInformationPage_skill);
	}

	private void setValueOfView() {
		String strTmp = null;
		boolean isNull = true;
		
		setSpinner_MedicalDepartment();
	}
	
    private void setSpinner_MedicalDepartment() {
    	 
        //get reference to the spinner from the XML layout
        Spinner spinner = depName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        if ( departContent == null ) 
        	return ;
        
        for ( int i=0; i<departContent.length; i++ ) {
        	list.add((String)departContent[i].get(DatabaseTable.Department.colDepName) );
        }
        

        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinner.setAdapter(dataAdapter);
        //attach the listener to the spinner
        itemSelectedListener=new UtilitySpinnerOnItemSelectedListener();
        spinner.setOnItemSelectedListener(itemSelectedListener);
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

	private void deBundle(Bundle bundle){
		bundle =this.getIntent().getExtras();
		
		if(bundle!=null){
			String doctorName = bundle.getString("doctorName");
			String medicalDepartment = bundle.getString("medicalDepartment");
			String jobTitle = bundle.getString("jobTitle");
			String phoneNumber = bundle.getString("phoneNumber");	
			String isRevised = bundle.getString("isRevised");	
			
			if("true".equals(isRevised)){
				button_addNewDoctor.setText("�ק�");
				dorName.setText(doctorName);
				//jobTitle.setText(jobTitle); 
				telephone.setText(phoneNumber);				
			}
 
		}else{
			//Toast.makeText(getApplicationContext(), "bundle is null!!", Toast.LENGTH_LONG).show();	
		}
	
		
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
		setContentView(R.layout.activity_new_doctor_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_newDoctorInformation).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_NewDoctorInformationPage_back);
		button_selectUploadDoctorPhoto = (Button)  findViewById(R.id.button_NewDoctorInformationPage_uploadDoctorPhoto);
		button_cleanAll = (Button)  findViewById(R.id.button_NewDoctorInformationPage_clearAll);
		button_addNewDoctor  = (Button)  findViewById(R.id.button_NewDoctorInformationPage_new);

		button_back.setOnClickListener(back);
		button_selectUploadDoctorPhoto.setOnClickListener(selectUploadDoctorPhoto);
		button_cleanAll.setOnClickListener(cleanAll);
		button_addNewDoctor.setOnClickListener(addNewDoctor);
	}

	/**
	 * back
	 * 
	 * ��A���Uback���s�A��^����(Home)
	 */
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(DoctorInformation_New_Activity.this, DoctorInformation_Display_Activity.class);            
			startActivity(intent);
			finish();	
		}
	};

	private Button.OnClickListener selectUploadDoctorPhoto = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),"selectUploadDoctorPhoto", Toast.LENGTH_LONG).show();
		}
	};
	
	private Button.OnClickListener cleanAll = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			depName.setSelection(0); 	
			dorName.setText("");			
			telephone.setText("");	
			jobTitle.setText("");			
			history.setText("");			
			subject.setText("");			
			Toast.makeText(getApplicationContext(),"cleanAll", Toast.LENGTH_LONG).show();
		}
	};
	
	private Button.OnClickListener addNewDoctor = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			int status = 0;
			
			status = doctor.addDoctor(
						getLoginID(),							//userid, 
						depName.getSelectedItem().toString(), 	//depName, 
						dorName.getText().toString(),			//dorName, 
						telephone.getText().toString(),			//telephone, 
						jobTitle.getText().toString(),			//jobTitle, 
						history.getText().toString(),			//history, 
						subject.getText().toString(),			//subject, 
						"NULL",									//desc, 
						"UploadImg\\BillPic.jpg"				//picPath
					);
			
			if ( status < 0 ) {
				Toast.makeText(getApplicationContext(),"�s�W����", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),"�s�W���\", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(DoctorInformation_New_Activity.this, DoctorInformation_Display_Activity.class);
	       
				startActivity(intent);
				finish();	
			}
		}
	};
	
	/**
	 * openOptionsDialog_leaveAPP()
	 * 
	 * �Q�n���}app�ɡA�}��optionDialog�A�T�{�ϥΪ̬O�_�u���n���}
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(DoctorInformation_New_Activity.this);
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
