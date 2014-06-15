package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;
import edu.mcscheduling.R;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import edu.mcscheduling.model.Hospital;
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
import android.widget.TextView;
import android.widget.Toast;

public class DoctorInformation_New_Activity extends ControllerActivity {
	
	/**
	 * 以下為Button變數
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
	

	private Hospital hospital = null;
	private Doctor doctor = null;
	private Department depart = null;
	private ContentValues[] hospitalContent = null;
	private ContentValues[] doctorContent = null;
	private ContentValues[] departContent = null;
	
	private UtilitySpinnerOnItemSelectedListener itemSelectedListener;
	
	/**
	 * 目前這個Activity
	 */
	public static Activity thisActivity;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();
	
		thisActivity = this;
		
		setListeners();
		hospital = new Hospital(db);
		doctor = new Doctor(db);
		depart = new Department(db);
		hospitalContent = hospital.getHospital(getLoginID());
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

		if ( hospitalContent != null ) {
			strTmp = (String)hospitalContent[0].get(DatabaseTable.Hospital.colHospitalNo);
			hospitalNo.setText(strTmp == null ? "":strTmp);
		}
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
		setContentView(R.layout.activity_new_doctor_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_newDoctorInformation).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		button_selectUploadDoctorPhoto = (Button)  findViewById(R.id.button_NewDoctorInformationPage_uploadDoctorPhoto);
		button_cleanAll = (Button)  findViewById(R.id.button_NewDoctorInformationPage_clearAll);
		button_addNewDoctor  = (Button)  findViewById(R.id.button_NewDoctorInformationPage_new);

		button_selectUploadDoctorPhoto.setOnClickListener(selectUploadDoctorPhoto);
		button_cleanAll.setOnClickListener(cleanAll);
		button_addNewDoctor.setOnClickListener(addNewDoctor);
	}

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
				Toast.makeText(getApplicationContext(),"新增失敗", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),"新增成功", Toast.LENGTH_LONG).show();
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
	 * 想要離開app時，開啟optionDialog，確認使用者是否真的要離開
	 */
	public void openOptionsDialog_leaveAPP() {
		AlertDialog.Builder builder = new AlertDialog.Builder(DoctorInformation_New_Activity.this);
		builder.setTitle("APP訊息");
		builder.setMessage("真的要離開此APP");
		builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				thisActivity.finish();
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
			Intent intent = new Intent();
			intent.setClass(DoctorInformation_New_Activity.this, DoctorInformation_Display_Activity.class);            
			startActivity(intent);
			finish();	
		}
		return false;
	}
}
