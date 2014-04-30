package edu.mcscheduling.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.mcscheduling.R;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(3)
public class DoctorSchedulingCalendarActivity extends ControllerActivity {
	private static final String tag = "MyCalendarActivity";
	
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "yyyy   MMMM";

	
	private int selectedMonth;
	private int selectedYear;
	private int selectedDay;
	
	//For spinner
	private UtilitySpinnerOnItemSelectedListener itemSelectedListener;
	
	/**
	 * 以下為imageButton變數
	 */
	// View Componet
	private ImageButton button_back	= null;
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
	private ContentValues[] departContent = null;
	private ContentValues[] doctorContent = null;
	
	
	private int currentMonth, currentYear;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setLayout();
		
		setListeners();
		
		setCurrentTime();
	
		depart = new Department(db);
		doctor = new Doctor(db);	
		departContent = depart.getDepartment(getLoginID());
		doctorContent = doctor.getDoctor(getLoginID());
		
		bindViewComponent();
		
		setValueOfView();
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
				
		setCalendar_ValueOfView(currentMonth, currentYear);
	}
	
	
	private void setCalendar_ValueOfView(int month, int year) {
		adapter = new CalendarGridCellAdapter(
				this,getApplicationContext(),
				R.id.calendar_day_gridcell, 
				month, 
				year);
		
		
		comCalendar.set(year, month - 1, comCalendar.get(Calendar.DAY_OF_MONTH));
		comYearMonth.setText(DateFormat.format(dateTemplate,
					comCalendar.getTime()));
		
		adapter.notifyDataSetChanged();
		comCalendarView.setAdapter(adapter);
	}
		
	private void setLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_doctor_scheduling_calendar);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	
	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		
		button_back = (ImageButton) findViewById(R.id.ImageButton_DoctorSchedulingCalendarPage_back);
		button_prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		button_nextMonth = (ImageView) this.findViewById(R.id.nextMonth);

		button_back.setOnClickListener(back);
		button_prevMonth.setOnClickListener(prevMonth);
		button_nextMonth.setOnClickListener(nextMonth);

	}
    
	private void setSpinner_MedicalDepartment() {
      	 
        //get reference to the spinner from the XML layout
        Spinner spinner = comDepName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        for ( int i=0; i<departContent.length; i++ ) {
        	list.add((String)departContent[i].get(DatabaseTable.Department.colDepName));
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
    
    private void setSpinner_DoctorName() {
     	 
        //get reference to the spinner from the XML layout
        Spinner spinner = comDorName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();

        for ( int i=0; i<doctorContent.length; i++ ) {
        	list.add((String)doctorContent[i].get(DatabaseTable.Doctor.colDorName));
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

	private ImageButton.OnClickListener prevMonth = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (currentMonth <= 1) {
				currentMonth = 12;
				currentYear--;
			} else {
				currentMonth--;
			}

			setCalendar_ValueOfView(currentMonth, currentYear);
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

			setCalendar_ValueOfView(currentMonth, currentYear);
		}
	};
	
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			//for test
			Intent intent = new Intent();
			intent.setClass(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	
    /**
	 * 
	 * @param month
	 * @param year
	 */



	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}
	
	public int getCurrentMonth(){
		return currentMonth;
	}

	public void setSelectedDay(int day){
		this.selectedDay=day;
	}
	public void setSelectedMonth(int month){
		this.selectedMonth=month;
	}	
	public void setSelectedYear(int year){
		this.selectedYear=year;
	}	
	
}



