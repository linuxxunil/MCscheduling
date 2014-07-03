package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import edu.mcscheduling.model.MsContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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

public class DoctorInformation_Edit_Activity extends ControllerActivity {

	private Button button_selectUploadDoctorPhoto;
	private Button button_reviseDoctor;

	private TextView doctorNo = null;
	private TextView hospitalNo = null;
	private EditText dorName = null;
	private EditText jobTitle = null;
	private EditText telephone = null;
	private Spinner depName = null;
	private EditText history = null;
	private EditText subject = null;

	private Doctor doctor = null;
	private Department depart = null;
	private MsContentValues doctorContent = null;
	private MsContentValues departContent = null;
	private int clickPosition = 0; // 外部輸入
	private UtilitySpinnerOnItemSelectedListener itemSelectedListener;

	private final int INIT_TAG = 1;
	private final int REVISE_TAG = 2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPostion(savedInstanceState);

		initHandler();
		
		initValueToView();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	private void getPostion(Bundle bundle) {
		bundle = this.getIntent().getExtras();

		if (bundle != null) {
			clickPosition = bundle.getInt("Position");
		} else {
			// Toast.makeText(getApplicationContext(), "bundle is null!!",
			// Toast.LENGTH_LONG).show();
		}
	}

	private void setSpinner_MedicalDepartment() {
		// get reference to the spinner from the XML layout
		Spinner spinner = depName;

		// Array list of animals to display in the spinner
		List<String> list = new ArrayList<String>();

		if (departContent.cv == null)
			return;

		for (int i = 0; i < departContent.cv.length; i++) {
			list.add((String) departContent.cv[i]
					.get(DatabaseTable.Department.colDepName));
		}

		// create an ArrayAdaptar from the String Array
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// set the view for the Drop down list
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// set the ArrayAdapter to the spinner
		spinner.setAdapter(dataAdapter);
		// attach the listener to the spinner
		itemSelectedListener = new UtilitySpinnerOnItemSelectedListener();
		spinner.setOnItemSelectedListener(itemSelectedListener);
	}

	private void initLayout() {
		// set layout
		setContentView(R.layout.activity_edit_doctor_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_editDoctorInformation).requestFocus();
	}

	private void initListeners() {
		button_selectUploadDoctorPhoto = (Button) findViewById(R.id.button_EditDoctorInformationPage_uploadDoctorPhoto);
		button_reviseDoctor = (Button) findViewById(R.id.button_EditDoctorInformationPage_revise);

		doctorNo = (TextView) findViewById(R.id.TextView_EditDoctorInformationPage_doctorNumber);
		hospitalNo = (TextView) findViewById(R.id.TextView_EditDoctorInformationPage_hospitalNumber);
		dorName = (EditText) findViewById(R.id.EditText_EditDoctorInformationPage_doctorName);
		depName = (Spinner) findViewById(R.id.Spinner_EditDoctorInformationPage_medicalDepartment);
		jobTitle = (EditText) findViewById(R.id.EditText_EditDoctorInformationPage_jobTitle);
		telephone = (EditText) findViewById(R.id.EditText_EditDoctorInformationPage_doctorPhoneNumber);
		history = (EditText) findViewById(R.id.EditText_EditDoctorInformationPage_experience);
		subject = (EditText) findViewById(R.id.EditText_EditDoctorInformationPage_skill);

		button_selectUploadDoctorPhoto
				.setOnClickListener(selectUploadDoctorPhoto);
		button_reviseDoctor.setOnClickListener(reviseDoctor);

	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case INIT_TAG:
					initValueToViewResult(msg);
					break;
				}
			}
		};
	}
	
	private void initValueToView() {
		showProgessDialog(DoctorInformation_Edit_Activity.this,
				"資料讀取中，讀取時間依據您的網路速度而有不同");
		new Thread() {
			public void run() {
				doctor = new Doctor(db);
				depart = new Department(db);
				doctorContent = doctor.getDoctor(getLoginID());
				if (doctorContent.status != StatusCode.success)
					sendMessage(INIT_TAG, doctorContent.status);
				departContent = depart.getDepartment(getLoginID());
				if (doctorContent.status != StatusCode.success)
					sendMessage(INIT_TAG, departContent.status);
				sendMessage(INIT_TAG, StatusCode.success);
			}
		}.start();
	}

	private void initValueToViewResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();

		initLayout();
		initListeners();
		setSpinner_MedicalDepartment();
		
		String strTmp = null;
		
		if (status != StatusCode.success) {
			Builder alertDialog = new AlertDialog.Builder(
					DoctorInformation_Edit_Activity.this);
			
			alertDialog.setTitle("提示");
			switch (status) {
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"連線失敗。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorInformation_Edit_Activity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			case -23303:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"讀取失敗。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorInformation_Edit_Activity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			default:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"未知錯誤。"));
				alertDialog.setPositiveButton("確定",null);
				break;
			}
			alertDialog.show();
		} else {
			if (doctorContent.cv != null) {
				// Doctor No
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colDorNo);
				doctorNo.setText(strTmp == null ? "" : strTmp);
				// Hospital No
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colHospitalNo);
				hospitalNo.setText(strTmp == null ? "" : strTmp);
				// Dortoc Name
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colDorName);
				dorName.setText(strTmp == null ? "" : strTmp);
				// Job Title
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colJobTitle);
				jobTitle.setText(strTmp == null ? "" : strTmp);
				// telephone
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colTelephone);
				telephone.setText(strTmp == null ? "" : strTmp);
				// history
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colHistory);
				history.setText(strTmp == null ? "" : strTmp);
				// subject
				strTmp = (String) doctorContent.cv[clickPosition]
									.get(DatabaseTable.Doctor.colSubject);
				subject.setText(strTmp == null ? "" : strTmp);
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
	}

	private Button.OnClickListener selectUploadDoctorPhoto = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(), "selectUploadDoctorPhoto",
					Toast.LENGTH_LONG).show();
		}
	};

	private Button.OnClickListener reviseDoctor = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showProgessDialog(DoctorInformation_Edit_Activity.this, "儲存中...");
			reviseDecotor();
		}
	};
	
	private void reviseDecotor() {
		new Thread() {
			public void run() {
				int status = doctor.updateDoctor(getLoginID(), // userid,
						doctorNo.getText().toString(), // dorNo
						depName.getSelectedItem().toString(), // depName,
						dorName.getText().toString(), // dorName,
						telephone.getText().toString(), // telephone,
						jobTitle.getText().toString(), // jobTitle,
						history.getText().toString(), // history,
						subject.getText().toString(), // subject,
						"NULL", // desc,
						"UploadImg\\BillPic.jpg" // picPath
				);
				sendMessage(REVISE_TAG, status);
			}
		}.start();
	}
	
	private void reviseDecotorResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();
		
		Builder alertDialog = new AlertDialog.Builder(
				DoctorInformation_Edit_Activity.this);
		alertDialog.setTitle("提示");
		if (status != StatusCode.success) {
			switch (status) {
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"連線失敗。"));
				alertDialog.setPositiveButton("確定", null);
				break;
			case -23303:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"儲存失敗。"));
				alertDialog.setPositiveButton("確定", null);
				break;
			default:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"未知錯誤。"));
				alertDialog.setPositiveButton("確定", null);
				break;
			}
			alertDialog.show();
		} else {
			alertDialog.setMessage(String.format("[%d] %s", status, "儲存成功。"));
			alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int id) {
	        		changeActivity(DoctorInformation_Edit_Activity.this,
	        					DoctorInformation_Display_Activity.class);
	        	}
	        });
			alertDialog.show();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			changeActivity(DoctorInformation_Edit_Activity.this,
					DoctorInformation_Display_Activity.class);
		}
		return true;
	}
}
