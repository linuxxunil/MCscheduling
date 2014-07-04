package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import edu.mcscheduling.model.DoctorSchedule;
import edu.mcscheduling.model.Hospital;
import edu.mcscheduling.model.MsContentValues;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "yyyy   MMMM";

	// View Componet
	private Button button_save	= null;
	private ImageView 	button_prevMonth = null;
	private ImageView 	button_nextMonth = null;
	
	private TextView comYearMonth = null;
	private Spinner  comDepName = null;
	private Spinner  comDorName = null;
	private GridView comCalendarView = null;
	private Calendar comCalendar = null;
	
	
	private CalendarGridCellAdapter adapter;
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
	private boolean isInitialized = false;
	
	private final int INIT_TAG = 1;
	private final int SET_DOCTOR_SCHEDULING_TAG = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCurrentTime();
	
		monthInfo = new HashMap<String, String>();
		
		initHandler();
		
		initValueToView();
			
	}
	
	private void setCurrentTime() {
		comCalendar = Calendar.getInstance(Locale.getDefault());
		currentMonth = comCalendar.get(Calendar.MONTH) + 1;
		currentYear = comCalendar.get(Calendar.YEAR);
	}
	
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case INIT_TAG:
					initValueToViewResult(msg);
					break;
				case SET_DOCTOR_SCHEDULING_TAG:
					setDoctorScheduleResult(msg);
					break;
				}
			}
		};
	}
	
	private void initLayout() {
		// set layout
		setContentView(R.layout.activity_doctor_scheduling_calendar);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private void initListeners() {
		
		button_save = (Button) findViewById(R.id.Button__DoctorSchedulingCalendarPage_save);
		button_prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		button_nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		
		comDepName = (Spinner)findViewById(R.id.Spinner_DoctorSchedulingCalendarPage_medicalDepartment);
		comDorName = (Spinner)findViewById(R.id.Spinner_DoctorSchedulingCalendarPage_doctorName);
		comYearMonth = (TextView) this.findViewById(R.id.currentMonth);
		comCalendarView = (GridView) this.findViewById(R.id.calendar);

		button_save.setOnClickListener(save);
		button_prevMonth.setOnClickListener(prevMonth);
		button_nextMonth.setOnClickListener(nextMonth);
		comDepName.setOnItemSelectedListener(spinner_depName);
		comDorName.setOnItemSelectedListener(spinner_doctorName);
	}
	
	private void initValueToView() {
		showProgessDialog(DoctorSchedulingCalendarActivity.this,
				"資料讀取中，讀取時間依據您的網路速度而有不同");
		new Thread() {
			public void run() {
				depart = new Department(db);
				doctor = new Doctor(db);
				schedule = new DoctorSchedule(db);
				hospital = new Hospital(db);
				

				hospitalContent = hospital.getHospital(getLoginID());
				if (hospitalContent.status != StatusCode.success)
					sendMessage(INIT_TAG, hospitalContent.status);
				
				departContent = depart.getDepartment(getLoginID());
				if (departContent.status != StatusCode.success)
					sendMessage(INIT_TAG, departContent.status);
				
				setMonthInfo(currentYear, currentMonth);
				
				sendMessage(INIT_TAG, StatusCode.success);
			}
		}.start();
	}

	private void initValueToViewResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();

		initLayout();
		initListeners();
	
		if (status != StatusCode.success) {
			Builder alertDialog = new AlertDialog.Builder(
					DoctorSchedulingCalendarActivity.this);
			
			alertDialog.setTitle("提示");
			switch (status) {
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"連線失敗。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			case -23303:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"讀取失敗。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
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
			setSpinner_MedicalDepartment();
			setSpinner_DoctorName();
			setCalendarValueToView(currentYear, currentMonth);
			isInitialized = true;
		}
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
		
		if ( departContent == null || departContent.cv == null || 
				doctorContent == null || doctorContent.cv == null ) {
			for ( int i=0; i<31; i++ ) 
				monthInfo.put(String.valueOf(i), "false-false-false");
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
				for ( int i=0; i<31; i++ ) 
					setConsultingTime(String.valueOf(i), tmp.substring(i,i+1) );
			}
		}
	}
	
	private void setCalendarValueToView(int year, int month) {

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
		
		adapter.updateMonthInfoToButton(currentYear, currentMonth);
		adapter.notifyDataSetChanged();
		
	}
		
	private void setSpinner_MedicalDepartment() {
      	 
        //get reference to the spinner from the XML layout
        final Spinner spinner = comDepName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        
        if ( departContent.cv != null ) {
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
			
			if ( doctorContent.cv != null ) {
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
			setCalendarValueToView(currentYear, currentMonth);
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
			setCalendarValueToView(currentYear, currentMonth);
		}
	};

	private Button.OnClickListener save = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if ( departContent == null || departContent.cv == null ) {
				Toast.makeText(v.getContext(), "無法儲存，無科別資料", Toast.LENGTH_LONG).show();
			} else if ( doctorContent == null || doctorContent.cv == null ) {
				Toast.makeText(v.getContext(), "無法儲存，無醫生資料", Toast.LENGTH_LONG).show();
			} else {
				showProgessDialog(DoctorSchedulingCalendarActivity.this, "儲存中...");
				setDoctorSchedule();
			}
		}
	};
	
	private void setDoctorSchedule() {
		final String dorNo = (String)doctorContent.cv[dorNoRowId].get(DatabaseTable.Doctor.colDorNo);
		final String depName = (String)departContent.cv[depNameRowId].get(DatabaseTable.Department.colDepName);
		final String schYear = String.format("%02d", currentYear);
		final String schMonth = String.format("%02d", currentMonth);
		
		new Thread() {
			public void run() {				
				int status ;
				scheduleContent = schedule.getDoctorScheduleByDorNo_ShcYear_ShcMonth(
							getLoginID(),
							dorNo,
							schYear,
							schMonth);
						
				if ( scheduleContent.status != StatusCode.success ) {
					status = schedule.addDoctorSchedule(
							getLoginID(), 
							dorNo,
							depName, 
							schYear, 
							schMonth,
							getConsultingTime(), 
							"null");
					sendMessage(SET_DOCTOR_SCHEDULING_TAG, status);
				} else {
					status = schedule.updateDoctorSchedule(
							getLoginID(), 
							dorNo, 
							depName, 
							schYear, 
							schMonth, 
							getConsultingTime(), 
							"null");
					sendMessage(SET_DOCTOR_SCHEDULING_TAG, status);
				}
						
				sendMessage(SET_DOCTOR_SCHEDULING_TAG, status);
			}
		}.start();
	}
	
	private void setDoctorScheduleResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();

		Builder alertDialog = new AlertDialog.Builder(
				DoctorSchedulingCalendarActivity.this);
		alertDialog.setTitle("提示");
		alertDialog.setPositiveButton("確定", null);
		if (status != StatusCode.success) {
			switch (status) {
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"連線失敗。"));
				break;
			case -23303:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"儲存失敗。"));
				break;
			default:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"未知錯誤。"));
				break;
			}
			alertDialog.show();
		} else {
			alertDialog.setMessage(String.format("[%d] %s", status, "儲存成功。"));
			alertDialog.show();
		}
	}
	
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			changeActivity(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
		}
		return true;
	}
}



