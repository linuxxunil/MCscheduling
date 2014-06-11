package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.mcscheduling.R;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import edu.mcscheduling.model.DoctorSchedule;
import edu.mcscheduling.model.Hospital;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


@TargetApi(3)
public class DoctorSchedulingCalendarActivity extends ControllerActivity {
	//private static final String tag = "MyCalendarActivity";
	
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "yyyy   MMMM";
	
	/**
	 * 以下為imageButton變數
	 */
	// View Componet
	private ImageButton button_back	= null;
	private Button button_save	= null;
	private ImageView 	button_prevMonth = null;
	private ImageView 	button_nextMonth = null;
	
	private TextView comYearMonth = null;
	private Spinner  comDepName = null;
	private Spinner  comDorName = null;
	private GridView comCalendarView = null;
	private Calendar comCalendar = null;
	
	//
	private CalendarGridCellAdapter adapter;
	private Department depart = null;
	private Doctor doctor = null;
	private Hospital hospital = null;
	private DoctorSchedule schedule = null;
	private int depNameRowId = 0;
	private int dorNoRowId = 0;
	private ContentValues[] departContent = null;
	private ContentValues[] doctorContent = null;
	private ContentValues[] scheduleContent = null;
	private ContentValues[] hospitalContent = null;
	
	private HashMap<String, String> monthInfo = null;
	
	private int currentYear = 0;
	private int currentMonth = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setLayout();
		
		setCurrentTime();
	
		depart = new Department(db);
		doctor = new Doctor(db);
		schedule = new DoctorSchedule(db);
		hospital = new Hospital(db);
		
		monthInfo = new HashMap<String, String>();
		
		departContent = depart.getDepartment(getLoginID());
		hospitalContent = hospital.getHospital(getLoginID());
		
		bindViewComponent();
		
		setValueOfView();
		
		setListeners();
	}
	
	private void setCurrentTime() {
		comCalendar = Calendar.getInstance(Locale.getDefault());
		currentMonth = comCalendar.get(Calendar.MONTH) + 1;
		currentYear = comCalendar.get(Calendar.YEAR);
	}
	
	private void bindViewComponent(){
		comDepName = (Spinner)findViewById(R.id.Spinner_DoctorSchedulingCalendarPage_medicalDepartment);
		comDorName = (Spinner)findViewById(R.id.Spinner_DoctorSchedulingCalendarPage_doctorName);
		comYearMonth = (TextView) this.findViewById(R.id.currentMonth);
		comCalendarView = (GridView) this.findViewById(R.id.calendar);
	}

	private void setValueOfView() {
		
		setSpinner_MedicalDepartment();
		
		setSpinner_DoctorName();
				
		setCalendar_ValueOfView(currentYear, currentMonth);
	}
	
	private void setConsultingTime(String key, String substring) {
			String[] consultingChecked = {"false","false","false"};
			int value = Integer.valueOf(substring);
				
			if ( value >= 5 ) {
				consultingChecked[2] = "true";
				value -= 5;
			}
				
			if ( value >= 3 ) {
				consultingChecked[1] = "true";
				value -= 3;
			}
				
			if ( value >= 1 ) {
				consultingChecked[0] = "true";
				value -= 1;
			}

			monthInfo.put(key,
						consultingChecked[0] + "-" + 
						consultingChecked[1] + "-" + 
						consultingChecked[2] );
	}
	
	private void setMonthInfo(int year,int month) {
		
		if ( (year<2012) || (month<1 || month>12)) {
			return ;
		}
		
		monthInfo.put("year", String.valueOf(year));
		monthInfo.put("month", String.valueOf(month));
		
		String hospitalschedule = null;
		if ( hospitalContent == null ) {
			hospitalschedule = "0000000";
		} else {
			hospitalschedule = 
				(String)hospitalContent[0].get(DatabaseTable.Hospital.colHospitalschedule);
		}
		
		for (int i=0; i<7; i++ ) 
			setConsultingTime("week"+String.valueOf(i), hospitalschedule.substring(i,i+1));
		
		if ( departContent == null || doctorContent == null ) {
			return ;
		} else {
			scheduleContent = schedule.getDoctorScheduleByDepName_AND_DorNo_AND_SchYear_SchMonth(
					getLoginID(), 
					(String)departContent[depNameRowId].get(DatabaseTable.Doctor.colDepName), 
					(String)doctorContent[dorNoRowId].get(DatabaseTable.Doctor.colDorNo),
					year, month) ;
		
			if ( scheduleContent == null ) {
				for ( int i=0; i<31; i++ ) 
					monthInfo.put(String.valueOf(i), "false-false-false");
			} else {
				String tmp = (String) scheduleContent[0].get(DatabaseTable.DoctorSchedule.colSchedule);				
				System.out.println(tmp.length());
				for ( int i=0; i<31; i++ ) 
					setConsultingTime(String.valueOf(i), tmp.substring(i,i+1) );
			}
		}
	}
	
	private void setCalendar_ValueOfView(int year, int month) {
		
		setMonthInfo(year, month);
		
		if ( adapter == null ) {
			adapter = new CalendarGridCellAdapter(
					this,getApplicationContext(),
					R.id.calendar_day_gridcell, 
					monthInfo);
			comCalendarView.setAdapter(adapter);
		}
		
		comCalendar.set(year, month - 1, comCalendar.get(Calendar.DAY_OF_MONTH));
		comYearMonth.setText(DateFormat.format(dateTemplate,
					comCalendar.getTime()));
		
		adapter.updateMonthInfoToButton(year, month);
		adapter.notifyDataSetChanged();
		
	}
		
	private void setLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_doctor_scheduling_calendar);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public void setListeners() {
		
		button_back = (ImageButton) findViewById(R.id.ImageButton_DoctorSchedulingCalendarPage_back);
		button_save = (Button) findViewById(R.id.Button__DoctorSchedulingCalendarPage_save);
		button_prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		button_nextMonth = (ImageView) this.findViewById(R.id.nextMonth);

		button_back.setOnClickListener(back);
		button_save.setOnClickListener(save);
		button_prevMonth.setOnClickListener(prevMonth);
		button_nextMonth.setOnClickListener(nextMonth);
		comDepName.setOnItemSelectedListener(spinner_depName);
		comDorName.setOnItemSelectedListener(spinner_doctorName);
	}
    
	private void setSpinner_MedicalDepartment() {
      	 
        //get reference to the spinner from the XML layout
        final Spinner spinner = comDepName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        if ( departContent != null ) {
        	for ( int i=0; i<departContent.length; i++ ) {
        		list.add((String)departContent[i].get(DatabaseTable.Department.colDepName));
        	}
        }
        
        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinner.setAdapter(dataAdapter);

        
    }
    
    private void setSpinner_DoctorName() {
     	 
        //get reference to the spinner from the XML layout
        final Spinner spinner = comDorName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();

		if ( departContent != null ) {
			doctorContent = doctor.getDoctorByDepName(getLoginID(),
					(String)departContent[depNameRowId].get(DatabaseTable.Department.colDepName));
			
			if ( doctorContent != null ) {
				for ( int i=0; i<doctorContent.length; i++ ) {
					list.add((String)doctorContent[i].get(DatabaseTable.Doctor.colDorName));
				}
			} 
		}
       
        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinner.setAdapter(dataAdapter);
    }
    
    private OnItemSelectedListener spinner_depName = new OnItemSelectedListener() { 
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    		dorNoRowId = 0;
    		depNameRowId = pos;
    		setSpinner_DoctorName();
    		setMonthInfo(currentYear, currentMonth);
    		adapter.updateMonthInfoToButton(currentYear, currentMonth);
    		adapter.notifyDataSetChanged();
    	}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

    
	private OnItemSelectedListener spinner_doctorName = new OnItemSelectedListener() { 
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    		dorNoRowId = pos;
    		setMonthInfo(currentYear, currentMonth);
    		adapter.updateMonthInfoToButton(currentYear, currentMonth);
    		adapter.notifyDataSetChanged();
    	}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	
	private ImageButton.OnClickListener prevMonth = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (currentMonth <= 1) {
				currentMonth = 12;
				currentYear--;
			} else {
				currentMonth--;
			}
			setCalendar_ValueOfView(currentYear, currentMonth);
		}
	};

	private ImageButton.OnClickListener nextMonth = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (currentMonth > 11) {
				currentMonth = 1;
				currentYear++;
			} else {
				currentMonth++;
			}
			setCalendar_ValueOfView(currentYear, currentMonth);
		}
	};
	
	
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};
	
	private String getConsultingTime() {
		String consultingTime = "";
		String tmp = null;
		int value = 0;
		int numOfDaysOfMonth = adapter.getNumberOfDaysOfMonth(currentMonth);
		
		for ( int i=0; i<numOfDaysOfMonth; i++ ) {
			tmp = monthInfo.get(String.valueOf(i));
			
			if ( tmp != null ) {
				String[] selected = tmp.split("-");
				value = 0;
				if ( selected[0].equals("true") ) {
					value += 1;
				}
				
				if ( selected[1].equals("true") ) {
					value += 3;
				}
				
				if ( selected[2].equals("true") ) {
					value += 5;
				}
				consultingTime += String.valueOf(value);
			}
		}
		
		for (int i=consultingTime.length(); i<31; i++)
			consultingTime += "0";
		
		return consultingTime;
	}
	
	private Button.OnClickListener save = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			
			String dorNo = (String)doctorContent[dorNoRowId].get(DatabaseTable.Doctor.colDorNo);
			String depName = (String)departContent[depNameRowId].get(DatabaseTable.Department.colDepName);
			String schYear = String.valueOf(currentYear);
			String schMonth = String.valueOf(currentMonth);

			scheduleContent = schedule.getDoctorScheduleByDorNo_ShcYear_ShcMonth(
					getLoginID(),
					dorNo,
					schYear,
					schMonth);
			
			if ( scheduleContent == null ) 
				if ( schedule.addDoctorSchedule(
						getLoginID(), 
						dorNo,
						depName, 
						schYear, 
						schMonth,
						getConsultingTime(), 
						"null") == 0 ) { 
					Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getApplicationContext(), "修改失敗", Toast.LENGTH_LONG).show();
				}
			else
				if ( schedule.updateDoctorSchedule(
						getLoginID(), 
						dorNo, 
						depName, 
						schYear, 
						schMonth, 
						getConsultingTime(), 
						"null") == 0 ) {
					Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getApplicationContext(), "修改失敗", Toast.LENGTH_LONG).show();
				}
		}
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return false;
	}
	
    /**
	 * 
	 * @param month
	 * @param year
	 */
	@Override
	
	public void onDestroy() {
		super.onDestroy();
	}
}



