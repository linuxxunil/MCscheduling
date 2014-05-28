package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;
import edu.mcscheduling.R;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorInformation_Edit_Activity extends ControllerActivity {

	/**
	 * 以下為imageButton變數
	 */
	private ImageButton button_back;
	
	/**
	 * 以下為Button變數
	 */
	private Button button_selectUploadDoctorPhoto;
	private Button button_reviseDoctor;

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
	private int target = 0;	// 外部輸入
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
		
		handleBundle(savedInstanceState);
		
		setLayout();
	
	
		thisActivity = this;
		
		setListeners();
		
		doctor = new Doctor(db);
		
		depart = new Department(db);
		
		doctorContent = doctor.getDoctor(getLoginID());
		
		departContent = depart.getDepartment(getLoginID());
		
		bindViewComponent();
		
		setValueOfView();
		
	}
	
	private void handleBundle(Bundle bundle) {
		bundle = this.getIntent().getExtras();
		
		if(bundle != null){
			target = bundle.getInt("Position");
		}else{
			//Toast.makeText(getApplicationContext(), "bundle is null!!", Toast.LENGTH_LONG).show();	
		}
	}
	
	private void bindViewComponent(){
		doctorNo=(TextView)findViewById(R.id.TextView_EditDoctorInformationPage_doctorNumber);
		hospitalNo=(TextView)findViewById(R.id.TextView_EditDoctorInformationPage_hospitalNumber);
		dorName=(EditText)findViewById(R.id.EditText_EditDoctorInformationPage_doctorName); 
		depName=(Spinner)findViewById(R.id.Spinner_EditDoctorInformationPage_medicalDepartment); 
		jobTitle=(EditText)findViewById(R.id.EditText_EditDoctorInformationPage_jobTitle);  
		telephone=(EditText)findViewById(R.id.EditText_EditDoctorInformationPage_doctorPhoneNumber);
		history=(EditText)findViewById(R.id.EditText_EditDoctorInformationPage_experience);
		subject=(EditText)findViewById(R.id.EditText_EditDoctorInformationPage_skill);
	}

	private void setValueOfView() {
		String strTmp = null;
		
		setSpinner_MedicalDepartment();
		
		if ( doctorContent != null ) {
			
			// Doctor No
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colDorNo);
			doctorNo.setText(strTmp == null ? "":strTmp);
			
			// Hospital No
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colHospitalNo);
			hospitalNo.setText(strTmp == null ? "":strTmp);
			
			// Dortoc Name
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colDorName);
			dorName.setText(strTmp == null ? "":strTmp);
			
			// Job Title
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colJobTitle);
			jobTitle.setText(strTmp == null ? "":strTmp);
			
			// telephone
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colTelephone);
			telephone.setText(strTmp == null ? "":strTmp);
			
			// history
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colHistory);
			history.setText(strTmp == null ? "":strTmp);
			
			// subject
			strTmp = (String)doctorContent[target].get(DatabaseTable.Doctor.colSubject);
			subject.setText(strTmp == null ? "":strTmp);
		} else {
			// Doctor No
			doctorNo.setText("");
						
			// Hospital No
			hospitalNo.setText("");
						
			// Job Title
			jobTitle.setText("");
						
			// telephone
			telephone.setText("");
						
			// history
			history.setText("");
						
			// subject
			subject.setText("");
		}
		
		
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
		setContentView(R.layout.activity_edit_doctor_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_editDoctorInformation).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_EditDoctorInformationPage_back);
		button_selectUploadDoctorPhoto = (Button)  findViewById(R.id.button_EditDoctorInformationPage_uploadDoctorPhoto);
		button_reviseDoctor  = (Button)  findViewById(R.id.button_EditDoctorInformationPage_revise);

		button_back.setOnClickListener(back);
		button_selectUploadDoctorPhoto.setOnClickListener(selectUploadDoctorPhoto);
		button_reviseDoctor.setOnClickListener(reviseDoctor);
		
	}

	/**
	 * back
	 * 
	 * 當你按下back按鈕，返回首頁(Home)
	 */
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(DoctorInformation_Edit_Activity.this, DoctorInformation_Display_Activity.class);            
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
	
	private Button.OnClickListener reviseDoctor = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			int status = 0;
			
			status = doctor.updateDoctor(
						getLoginID(),							//userid, 
						doctorNo.getText().toString(),			//dorNo
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
				Toast.makeText(getApplicationContext(),"修改失敗", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(DoctorInformation_Edit_Activity.this, DoctorInformation_Display_Activity.class);
	       
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
		AlertDialog.Builder builder = new AlertDialog.Builder(DoctorInformation_Edit_Activity.this);
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
			// do nothing...
		}
		return false;
	}

	// 以下為目前尚未使用，但未來會用到的function----------------------------------------------

	/**
	 * isNetworkAvailable()
	 * 
	 * 檢查目前的網路狀況
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
	 * 顯示目前的網路狀況，讓使用者知道
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
