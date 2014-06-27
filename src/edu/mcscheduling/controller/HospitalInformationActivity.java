package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Hospital;
import edu.mcscheduling.model.MsContentValues;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class HospitalInformationActivity extends ControllerActivity {

	private final String[] medicalGroup = {"無","萬年溪醫療群","台大醫療群","長庚醫療群","榮總醫療群"};

	/**
	 * 以下為Button變數
	 */
	private Button button_selectUploadManagerPhoto;
	private Button button_revise;
	private Button button_selectConsultingHour;

	/**
	 * 以下為dialog變數
	 */
	private Dialog dialog_selectConsultingHour;
	private Hospital hospital = null;
	private MsContentValues hospitalContent = null;
	
	private Department depart = null;
	private EditText hospitalNo = null;
	private Spinner areaID = null;
	private EditText hospitalName = null;
	private EditText hospitalAddress = null;
	private EditText contactName = null;
	private EditText hospitalPhone = null;
	private EditText depName = null;
	private EditText contactPhone = null;
	private Spinner opdSt1 = null;
	private Spinner opdEt1 = null;
	private Spinner opdSt2 = null;
	private Spinner opdEt2 = null;
	private Spinner opdSt3 = null;
	private Spinner opdEt3 = null;
	private CheckBox MonMorning = null;
	private CheckBox MonNoon = null;
	private CheckBox MonNight = null;
	private CheckBox TueMorning = null;
	private CheckBox TueNoon = null;
	private CheckBox TueNight = null;
	private CheckBox WedMorning = null;
	private CheckBox WedNoon = null;
	private CheckBox WedNight = null;
	private CheckBox ThuMorning = null;
	private CheckBox ThuNoon = null;
	private CheckBox ThuNight = null;
	private CheckBox FriMorning = null;
	private CheckBox FriNoon = null;
	private CheckBox FriNight = null;
	private CheckBox SatMorning = null;
	private CheckBox SatNoon = null;
	private CheckBox SatNight = null;
	private CheckBox SunMorning = null;
	private CheckBox SunNoon = null;
	private CheckBox SunNight = null;
	 
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

		// Listen for button clicks
		setListeners();
		
		initSelectConsultingHourView();
		
		hospital = new Hospital(db);
		depart = new Department(db);
		hospitalContent = hospital.getHospital(getLoginID());
		if ( hospitalContent.status != StatusCode.success ) {
			// show message
		}
		
		bindViewComponent();
		
		setValueOfView();

	}
	
	private void initSelectConsultingHourView() {
		// custom dialog
		dialog_selectConsultingHour = new Dialog(this);
		dialog_selectConsultingHour.setContentView(R.layout.dialog_select_consulting_hour);
		dialog_selectConsultingHour.setTitle("選擇看診時間");

		Button buttonConfirm = (Button) dialog_selectConsultingHour.findViewById(R.id.Dialog_button_confirm);
		Button buttonCancel = (Button) dialog_selectConsultingHour.findViewById(R.id.Dialog_button_cancel);
		
		buttonConfirm.setOnClickListener(dialogConfirm);
		buttonCancel.setOnClickListener(dialogCancel);
	}
	
	private void bindViewComponent() {
		hospitalNo = (EditText) findViewById(R.id.EditText_HospitalInformationPage_hospitalNumber);
		areaID = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_medicalGroup);
		hospitalName = (EditText) findViewById(R.id.EditText_HospitalInformationPage_hospitalName);
		hospitalAddress = (EditText) findViewById(R.id.EditText_HospitalInformationPage_hospitalAddress);
		contactName = (EditText) findViewById(R.id.EditText_HospitalInformationPage_hospitalManagerName); 
		hospitalPhone = (EditText) findViewById(R.id.EditText_HospitalInformationPage_hospitalPhoneNumber); 
		contactPhone = (EditText) findViewById(R.id.EditText_HospitalInformationPage_hospitalManagerPhoneNumber); 
		depName =  (EditText) findViewById(R.id.EditText_HospitalInformationPage_medicalDepartment); 
		
		opdSt1 = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_mornStartTime);
		opdEt1 = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_mornEndTime);
		opdSt2 = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_noonStartTime);
		opdEt2 = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_noonEndTime);
		opdSt3 = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_nightStartTime);
		opdEt3 = (Spinner) findViewById(R.id.Spinner_HospitalInformationPage_nightEndTime);
		
		MonMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.MonMorning);
		MonNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.MonNoon );
		MonNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.MonNight);
		TueMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.TueMorning);
		TueNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.TueNoon);
		TueNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.TueNight );
		WedMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.WedMorning);
		WedNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.WedNoon);
		WedNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.WedNight );
		ThuMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.ThuMorning);
		ThuNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.ThuNoon );
		ThuNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.ThuNight);
		FriMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.FriMorning);
		FriNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.FriNoon);
		FriNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.FriNight);
		SatMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.SatMorning);
		SatNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.SatNoon);
		SatNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.SatNight);
		SunMorning  = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.SunMorning);
		SunNoon     = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.SunNoon);
		SunNight    = (CheckBox)dialog_selectConsultingHour.findViewById(R.id.SunNight);		
	}
	
	private void setValueOfView() {
		String strTmp = null;
		boolean isNull = true;
		
		
		setSpinner_MedicalGroup();
		if ( hospitalContent != null ) {
			
			// Hospital No
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colHospitalNo);
			hospitalNo.setText(strTmp == null ? "":strTmp);
			
			// Area ID
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colAreaID);
			if ( strTmp != null ) {
				for ( int i=0; i<medicalGroup.length; i++ ) {
					if ( medicalGroup[i].equals(strTmp) ) {
						areaID.setSelection(i);
						break;
					}
				}
			} else {
				areaID.setSelection(0);
			}
			
			// Hospital Name 
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colHospitalName);
			hospitalName.setText(strTmp == null ? "":strTmp);
			
			// Hospital Address 
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colHospitalAddress);
			hospitalAddress.setText(strTmp == null ? "":strTmp);
			
			// Contact Name 
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colContactName);
			contactName.setText(strTmp == null ? "":strTmp);
			
			// Hospital Phone 
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colHospitalPhone);
			hospitalPhone.setText(strTmp == null ? "":strTmp);
			
			// Contact Phone 
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colContactPhone);
			contactPhone.setText(strTmp == null ? "":strTmp);
			
			// Dep Name
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colDepName);
			depName.setText(strTmp == null ? "":strTmp);
					
			// Morning Start Time
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colOPD_ST1);
			setSpinner_MorningStartTime(strTmp == null ? "":strTmp);

			// Morning End Time
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colOPD_ET1);
			setSpinner_MorningEndTime(strTmp == null ? "":strTmp);
	
			// Moon Start Time
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colOPD_ST2);
			setSpinner_NoonStartTime(strTmp == null ? "":strTmp);
			
			// Moon End Time
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colOPD_ET2);
			setSpinner_NoonEndTime(strTmp == null ? "":strTmp);
			
			// Night Start Time
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colOPD_ST3);
			showSpinner_NightStartTime(strTmp == null ? "":strTmp);
			
			// Night End Time
			strTmp = (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colOPD_ET3);
			setSpinner_NightEndTime(strTmp == null ? "":strTmp);
			
			// Consulting Hour
			strTmp =  (String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colHospitalschedule);
			if ( strTmp != null && ! strTmp.equals("")) {
				isNull = false;
			}
			
			int value = 0;
			if ( !isNull ) {
				for ( int i=0; i<7; i++ ) {
					value = Integer.valueOf(strTmp.substring(i,i+1));
					switch ( i ) {
					case 0:
						setConsultingHour(value, MonMorning, MonNoon, MonNight);
					break;
					case 1:
						setConsultingHour(value, TueMorning, TueNoon, TueNight);
					break;
					case 2:
						setConsultingHour(value, WedMorning, WedNoon, WedNight);
					break;
					case 3:
						setConsultingHour(value, ThuMorning, ThuNoon, ThuNight);
					break;
					case 4:
						setConsultingHour(value, FriMorning, FriNoon, FriNight);
					break;
					case 5:
						setConsultingHour(value, SatMorning, SatNoon, SatNight);
					break;
					case 6:
						setConsultingHour(value, SunMorning, SunNoon, SunNight);
					break;
					}	
				}
			}
		} else {
			// Hospital No
			hospitalNo.setText("");
			
			// Area ID
			
			// Hospital Name 
			hospitalName.setText("");
			
			// Hospital Address 
			hospitalAddress.setText("");
			
			// Contact Name 
			contactName.setText("");
			
			// Hospital Phone 
			hospitalPhone.setText("");
			
			// Contact Phone 
			contactPhone.setText("");
			
			// Dep Name
			depName.setText("");
			
			// Morning Start Time
			setSpinner_MorningStartTime("");

			// Morning End Time
			setSpinner_MorningEndTime("");
	        
			// Moon Start Time
			setSpinner_NoonStartTime("");
	        
			// Moon End Time
			setSpinner_NoonEndTime("");
	        
			// Night Start Time
			showSpinner_NightStartTime("");
	        
			// Might End Time
			setSpinner_NightEndTime("");
	       
	        // Consulting Hour
			MonMorning.setChecked(false);
			MonNoon.setChecked(false);
			MonNight.setChecked(false);
			TueMorning.setChecked(false);
			TueNoon.setChecked(false);
			TueNight.setChecked(false);
			WedMorning.setChecked(false);
			WedNoon.setChecked(false);
			WedNight.setChecked(false);
			ThuMorning.setChecked(false);
			ThuNoon.setChecked(false);
			ThuNight.setChecked(false);
			FriMorning.setChecked(false);
			FriNoon.setChecked(false);
			FriNight.setChecked(false);
			SatMorning.setChecked(false);
			SatNoon.setChecked(false);
			SatNight.setChecked(false);
			SunMorning.setChecked(false);
			SunNoon.setChecked(false);
			SunNight.setChecked(false);
		}
		
	}
	
    private void setSpinner_MedicalGroup() {
    	 
        //get reference to the spinner from the XML layout
        Spinner spinner = areaID;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        for ( int i=0; i<medicalGroup.length; i++ )
        	list.add(medicalGroup[i]);
  
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
     * Set Morning start time and end time....
     */
    private void setSpinner_MorningStartTime(String opd) {
   	 
        //get reference to the spinner from the XML layout
        Spinner spinner = opdSt1;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        boolean status = false;
        String[] time = {(String)"",
        				 (String)"7:00", 
        				 (String)"7:30",
        				 (String)"8:00",
        				 (String)"8:30",
        				 (String)"9:00",
        				 (String)"9:30",
        				 (String)"10:00"
        				 };
        
        
        for ( int i=0; i<time.length; i++ ) {
        	if ( status || time[i].equals(opd) ) {
        		list.add(time[i]);
        		status = true;
        	}
        }

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
  
    private void setSpinner_MorningEndTime(String opd) {
        //get reference to the spinner from the XML layout
        Spinner spinner = opdEt1;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        boolean status = false;
        String[] time = {(String)"",
        				 (String)"10:00", 
        				 (String)"10:30",
        				 (String)"11:00",
        				 (String)"11:30",
        				 (String)"12:00",
        				 (String)"12:30",
        				 (String)"13:00"
        				 };
        
        for ( int i=0; i<time.length; i++ ) {
        	if ( status || time[i].equals(opd) ) {
        		list.add(time[i]);
        		status = true;
        	}
        }

        
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
     * Sets noon start time and end time...
     */
    private void setSpinner_NoonStartTime(String opd) {
      	 
        //get reference to the spinner from the XML layout
        Spinner spinner = opdSt2;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        boolean status = false;
        String[] time = {(String)"",
        				 (String)"13:00", 
        				 (String)"13:30",
        				 (String)"14:00",
        				 (String)"14:30",
        				 (String)"15:00",
        				 };
        
        for ( int i=0; i<time.length; i++ ) {
        	if ( status || time[i].equals(opd) ) {
        		list.add(time[i]);
        		status = true;
        	}
        }

  
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
  
   
    private void setSpinner_NoonEndTime(String opd) {
        //get reference to the spinner from the XML layout
        Spinner spinner = opdEt2;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        boolean status = false;
        String[] time = {(String)"",
        				 (String)"15:00", 
        				 (String)"15:30",
        				 (String)"16:00",
        				 (String)"16:30",
        				 (String)"17:00",
        				 (String)"17:30",
        				 (String)"18:00",
        				 };
        
        for ( int i=0; i<time.length; i++ ) {
        	if ( status || time[i].equals(opd) ) {
        		list.add(time[i]);
        		status = true;
        	}
        }
        
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
	 * Sets night start time and end time...
	 */
	private void showSpinner_NightStartTime(String opd) {
	  	 
	    //get reference to the spinner from the XML layout
	    Spinner spinner = opdSt3;
	    
	    //Array list of animals to display in the spinner
	    List<String> list = new ArrayList<String>();
	
        boolean status = false;
        String[] time = {(String)"",
        				 (String)"18:00", 
        				 (String)"18:30",
        				 (String)"19:00",
        				 (String)"19:30",
        				 (String)"20:00",
        				 };
        
        for ( int i=0; i<time.length; i++ ) {
        	if ( status || time[i].equals(opd) ) {
        		list.add(time[i]);
        		status = true;
        	}
        }
        
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
	
	private void setSpinner_NightEndTime(String opd) {
	    //get reference to the spinner from the XML layout
	    Spinner spinner = opdEt3;
	    
	    //Array list of animals to display in the spinner
	    List<String> list = new ArrayList<String>();

        
        boolean status = false;
        String[] time = {(String)"",
        				 (String)"20:00", 
        				 (String)"20:30",
        				 (String)"21:00",
        				 (String)"21:30",
        				 (String)"22:00",
        				 (String)"22:30",
        				 (String)"23:00",
        				 };
        
        for ( int i=0; i<time.length; i++ ) {
        	if ( status || time[i].equals(opd) ) {
        		list.add(time[i]);
        		status = true;
        	}
        }
	    
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
		setContentView(R.layout.activity_hospital_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_hospitalInformation).requestFocus();
	}

	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		button_selectUploadManagerPhoto = (Button)  findViewById(R.id.button_HospitalInformationPage_uploadHospitalManagerPhoto);
		button_revise = (Button)  findViewById(R.id.button_HospitalInformationPage_revise);
		button_selectConsultingHour  = (Button)  findViewById(R.id.button_HospitalInformationPage_selectConsultingHour);

		button_selectUploadManagerPhoto.setOnClickListener(selectUploadManagerPhoto);
		button_revise.setOnClickListener(revise);
		button_selectConsultingHour.setOnClickListener(selectConsultingHour);
	}

	private Button.OnClickListener selectUploadManagerPhoto = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),"selectUploadManagerPhoto", Toast.LENGTH_LONG).show();
		}
	};
		
	private Button.OnClickListener revise = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			int status = 0;
			
			if ( depName == null || depName.getText().toString().equals("") ) {
				Toast.makeText(getApplicationContext(),"[科別] 至少選擇一科", Toast.LENGTH_LONG).show();
				return ;
			} 
			
			String hispitalSchedule = "";
			hispitalSchedule += getConsultingHour( MonMorning, MonNoon, MonNight);
			hispitalSchedule += getConsultingHour( TueMorning, TueNoon, TueNight);
			hispitalSchedule += getConsultingHour( WedMorning, WedNoon, WedNight);
			hispitalSchedule += getConsultingHour( ThuMorning, ThuNoon, ThuNight);
			hispitalSchedule += getConsultingHour( FriMorning, FriNoon, FriNight);
			hispitalSchedule += getConsultingHour( SatMorning, SatNoon, SatNight);
			hispitalSchedule += getConsultingHour( SunMorning, SunNoon, SunNight);
			
			status = hospital.setHospital(getLoginID(), 
							hospitalNo.getText().toString(),			// Hospital Name
							areaID.getSelectedItem().toString(),		// Area ID
							hospitalPhone.getText().toString(),			// Hospital Phone
							hospitalAddress.getText().toString(),		// Hospital Address
							contactName.getText().toString(),			// Contact Name
							contactPhone.getText().toString(),			// Contact Phone,
							depName.getText().toString(),				// Dep Name
							opdSt1.getSelectedItem().toString(),		// opdSt1
							opdEt1.getSelectedItem().toString(),		// opdEt1
							opdSt2.getSelectedItem().toString(),		// opdSt2
							opdEt2.getSelectedItem().toString(),		// opdEt2
							opdSt3.getSelectedItem().toString(),		// opdSt3
							opdEt3.getSelectedItem().toString(),		// opdEt3
							hispitalSchedule,							// hispitalSchedule
							"1",										// hospitalState
							"UploadImg\\BillPic.jpg"						// picPath
							);
			
			status += depart.setDepartment(getLoginID(), depName.getText().toString(), "NULL");
			
			
			if ( status < 0 ) {
				Toast.makeText(getApplicationContext(),"修改失敗", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private Button.OnClickListener selectConsultingHour = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dialog_selectConsultingHour.show();
		}
	};
	
	private void setConsultingHour(int value, CheckBox morning, CheckBox noon, CheckBox night) {
		
		if ( value >= 5 ) {
			night.setChecked(true);
			value -= 5;
		}
		if ( value >= 3 ) {
			noon.setChecked(true);
			value -= 3;
		}
		if ( value >= 1 ) {
			morning.setChecked(true);
			value -= 1;
		}

	}

	private String getConsultingHour(CheckBox morning, CheckBox noon, CheckBox night) {
		int value = 0;
		
		if ( morning.isChecked() )
			value += 1;
		
		if ( noon.isChecked() )
			value += 3;
		
		if ( night.isChecked() )
			value += 5;
		
		return String.valueOf(value);
	}
	
	private Button.OnClickListener dialogCancel = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"Press dialog button cancel!!", Toast.LENGTH_LONG).show();
			dialog_selectConsultingHour.dismiss();
		}
	};
	
	private Button.OnClickListener dialogConfirm = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"count is: "+count, Toast.LENGTH_LONG).show();
			dialog_selectConsultingHour.dismiss();
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
			changeActivity(HospitalInformationActivity.this, MenuActivity.class);
		}
		return true;
	}
}
