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
import edu.mcscheduling.model.MsContentValues;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ScheduleViewActivity extends ControllerActivity {
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "yyyy   MMMM";
	
	/**
	 * 以下為imageButton變數
	 */
	// View Componet
	private ImageView 	button_prevMonth = null;
	private ImageView 	button_nextMonth = null;
	
	private TextView comYearMonth = null;
	private Spinner  comDepName = null;
	private Spinner  comDorName = null;
	private GridView comCalendarView = null;
	private Calendar comCalendar = null;
	
	//
	private GridCellAdapter adapter;
	private Department depart = null;
	private Doctor doctor = null;
	private Hospital hospital = null;
	private DoctorSchedule schedule = null;
	private int depNameRowId = 0;
	private int dorNoRowId = 0;
	private MsContentValues departContent = null;
	private MsContentValues doctorContent = null;
	private MsContentValues scheduleContent = null;
	private MsContentValues hospitalContent = null;
	
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
		comDepName = (Spinner)findViewById(R.id.Spinner_ScheduleViewPage_medicalDepartment);
		comDorName = (Spinner)findViewById(R.id.Spinner_ScheduleViewPage_doctorName);
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
		if ( hospitalContent.cv == null ) {
			hospitalschedule = "0000000";
		} else {
			hospitalschedule = 
				(String)hospitalContent.cv[0].get(DatabaseTable.Hospital.colHospitalschedule);
		}
		
		for (int i=0; i<7; i++ ) 
			setConsultingTime("week"+String.valueOf(i), hospitalschedule.substring(i,i+1));
		
		if ( departContent == null || doctorContent == null ) {
			return ;
		} else {
			scheduleContent = schedule.getDoctorScheduleByDepName_AND_DorNo_AND_SchYear_SchMonth(
					getLoginID(), 
					(String)departContent.cv[depNameRowId].get(DatabaseTable.Doctor.colDepName), 
					(String)doctorContent.cv[dorNoRowId].get(DatabaseTable.Doctor.colDorNo),
					year, month) ;
		
			if ( scheduleContent.cv == null ) {
				for ( int i=0; i<31; i++ ) 
					monthInfo.put(String.valueOf(i), "false-false-false");
			} else {
				String tmp = (String) scheduleContent.cv[0].get(DatabaseTable.DoctorSchedule.colSchedule);				
				System.out.println(tmp);
				for ( int i=0; i<31; i++ ) 
					setConsultingTime(String.valueOf(i), tmp.substring(i,i+1) );
			}
		}
	}
	
	private void setCalendar_ValueOfView(int year, int month) {
		
		setMonthInfo(year, month);
		
		if ( adapter == null ) {
			adapter = new GridCellAdapter(
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
		setContentView(R.layout.activity_schedule_view);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public void setListeners() {
		
		button_prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		button_nextMonth = (ImageView) this.findViewById(R.id.nextMonth);

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
        	for ( int i=0; i<departContent.cv.length; i++ ) {
        		list.add((String)departContent.cv[i].get(DatabaseTable.Department.colDepName));
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

		if ( departContent.cv != null ) {
			doctorContent = doctor.getDoctorByDepName(getLoginID(),
					(String)departContent.cv[depNameRowId].get(DatabaseTable.Department.colDepName));
			
			if ( doctorContent != null ) {
				for ( int i=0; i<doctorContent.cv.length; i++ ) {
					list.add((String)doctorContent.cv[i].get(DatabaseTable.Doctor.colDorName));
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
			// TODO Auto-generated method stub
			
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
			// TODO Auto-generated method stub
			
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
			Intent intent = new Intent();
			intent.setClass(ScheduleViewActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
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
			intent.setClass(ScheduleViewActivity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private class GridCellAdapter extends CalendarGridCellAdapter {	
		public GridCellAdapter(Activity activity, Context context,
				int textViewResourceId, HashMap<String, String> monthInfo) {
			super(activity, context, textViewResourceId, monthInfo);
		}
		@Override
		public void onClick(View view) {
			// nothing
		}
	}		
}
