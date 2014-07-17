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
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.lang.reflect.Field;

@TargetApi(3)
public class DoctorSchedulingCalendarActivity extends ControllerActivity {
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "yyyy   MMMM";

	// View for main
	private Button btnSave	= null;
	private Button btnQuery	= null;
	private ImageView 	iViwPrevMonth = null;
	private ImageView 	iViwNextMonth = null;
	private TextView	tViwDepName = null;
	private TextView	tViwDorName = null;
	private TextView tViwYearMonth = null;
	private GridView gViwCalendarView = null;
	private Calendar cldCalendar = null;
	private CalendarGridCellAdapter adapter;
	
	// View for Dialog
	private Dialog 	 dlgSelectDialog = null;
	private Spinner  spnDepName = null;
	private Spinner  spnDorName = null;
	private Button iBtnComfirm = null;
	private DatePicker 	 dPikDate = null;
	
	// Model
	private Department depart = null;
	private Doctor doctor = null;
	private Hospital hospital = null;
	private DoctorSchedule schedule = null;
	private MsContentValues departContent = null;
	private MsContentValues doctorContent = null;
	private MsContentValues scheduleContent = null;
	private MsContentValues hospitalContent = null;
	
	// variable
	private int depNameRowId = 0;
	private int dorNoRowId = 0;
	private HashMap<String, String> monthInfo = null;
	private int currentYear = 0;
	private int currentMonth = 0; // range = 1-12
	
	// Tag
	private final int INIT_TAG = 1;
	private final int SET_DOCTOR_SCHEDULING_TAG = 2;
	private final int GET_DOCTOR_SCHEDULING_TAG = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCurrentTime();
	
		monthInfo = new HashMap<String, String>();
		
		initHandler();
		
		initValueToView();
	}

	private void setCurrentTime() {
		cldCalendar = Calendar.getInstance(Locale.getDefault());
		currentMonth = cldCalendar.get(Calendar.MONTH) + 1;
		currentYear = cldCalendar.get(Calendar.YEAR);
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
				case GET_DOCTOR_SCHEDULING_TAG:
					getDoctorScheduleResult(msg);
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
		
		if ( adapter == null ) {
			initMonthInfo();
			adapter = new CalendarGridCellAdapter(
					this,getApplicationContext(),
					R.id.calendar_day_gridcell, 
					monthInfo);
		}
	}
	
	private void initListeners() {
		btnSave = (Button) this.findViewById(R.id.btn_save);
		btnQuery = (Button) this.findViewById(R.id.btn_query);
		
		tViwDepName = (TextView) this.findViewById(R.id.tViw_depart_name);
		tViwDorName = (TextView) this.findViewById(R.id.tViw_doctor_name);
		
		iViwPrevMonth = (ImageView) this.findViewById(R.id.iViw_prevMonth);
		iViwNextMonth = (ImageView) this.findViewById(R.id.iViw_nextMonth);
		tViwYearMonth = (TextView) this.findViewById(R.id.tViw_currentMonth);
		gViwCalendarView = (GridView) this.findViewById(R.id.gViw_calendar);

		btnSave.setOnClickListener(bOclSave);
		btnQuery.setOnClickListener(bOclQuery);
		iViwPrevMonth.setOnClickListener(iBtnOclPrevMonth);
		iViwNextMonth.setOnClickListener(iBtnOclNextMonth);
		gViwCalendarView.setAdapter(adapter);
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
				if (hospitalContent.status != StatusCode.success) {
					sendMessage(INIT_TAG, hospitalContent.status);
					return ;
				}
				
				departContent = depart.getDepartment(getLoginID());
				if (departContent.status != StatusCode.success) {
					sendMessage(INIT_TAG, departContent.status);
					return ;
				}
				
				doctorContent = doctor.getDoctor(getLoginID());
				if (doctorContent.status != StatusCode.success) {
					sendMessage(INIT_TAG, doctorContent.status);
					return ;
				}
				
				sendMessage(INIT_TAG, StatusCode.success);
			}
		}.start();
	}

	private void initValueToViewResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();

		initLayout();
		initListeners();
		
		initSelectDialogLayout();
		initSelectDialogListeners();
	
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
				
			case 34001:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"尚未建立醫生資料，請先建立醫生資料"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			case 32001:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"尚未建立醫院資料，請先建立醫院資料"));
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
			
			initSelectDialog();
			showSelectDialog();
		}
	}

	private void initSelectDialogLayout() {
		dlgSelectDialog = new Dialog(this);
		dlgSelectDialog.setContentView(R.layout.dialog_select_watch_scheduling);
		dlgSelectDialog.setTitle("選擇");
		
		// disable Day Visibility
		DatePicker picker = (DatePicker) dlgSelectDialog.findViewById(R.id.Dpk_Date);
		try {
			Field[] f = picker.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : f) {
				if (field.getName().equals("mDaySpinner")) {
					field.setAccessible(true);
					((View)field.get(picker)).setVisibility(View.GONE);
					break;
				}
			}
		} catch (Exception e ) {
			System.out.println(e.getMessage());
		}
	}
	
	private void initSelectDialogListeners() {
		spnDepName 	= (Spinner) dlgSelectDialog.findViewById(R.id.Spn_Department);
		spnDorName 	= (Spinner) dlgSelectDialog.findViewById(R.id.Spn_Doctor);
		dPikDate		= (DatePicker)dlgSelectDialog.findViewById(R.id.Dpk_Date);
		iBtnComfirm = (Button)dlgSelectDialog.findViewById(R.id.btn_comfirm);
		
		spnDepName.setOnItemSelectedListener(sOilDepName);
		spnDorName.setOnItemSelectedListener(sOilDoctorName);
		iBtnComfirm.setOnClickListener(iOclComfirm);
		
		dlgSelectDialog.setOnKeyListener(new 
				DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					changeActivity(DoctorSchedulingCalendarActivity.this, MenuActivity.class);
				}
				return true;
			}
		});
	}
	
	private void initSelectDialog() {
		setSpinnerMedicalDepartment();
		setSpinnerDoctorName();
	}
	
	private void showSelectDialog() {
		dlgSelectDialog.show();
	}
	
	private void dismissSelectDialog() {
		dlgSelectDialog.dismiss();
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
	
	private void initMonthInfo() {
		monthInfo.put("year", String.valueOf(currentYear));
		monthInfo.put("month", String.valueOf(currentMonth));
		
		String hospitalschedule = "0000000";
		
		for (int i=0; i<7; i++ ) 
			setConsultingTime("week"+String.valueOf(i), hospitalschedule.substring(i,i+1));
		for ( int i=0; i<31; i++ ) 
			monthInfo.put(String.valueOf(i), "false-false-false");
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
		cldCalendar.set(year, month - 1, cldCalendar.get(Calendar.DAY_OF_MONTH));
		tViwYearMonth.setText(DateFormat.format(dateTemplate,
				cldCalendar.getTime()));
		adapter.updateMonthInfoToButton(currentYear, currentMonth);
		adapter.notifyDataSetChanged();
		
	}
		
	private void setSpinnerMedicalDepartment() {
      	 
        //get reference to the spinner from the XML layout
        final Spinner spinner = spnDepName;
        
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
    
    private void setSpinnerDoctorName() {
     	 
        //get reference to the spinner from the XML layout
        final Spinner spinner = spnDorName;
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();

		if ( departContent.cv != null ) {
			if ( doctorContent.cv != null ) {
				String selected = 
						(String)departContent.cv[depNameRowId].get(DatabaseTable.Department.colDepName);
				for ( int i=0; i<doctorContent.cv.length; i++ ) {
					if ( selected.equals(
							(String)doctorContent.cv[i].get(DatabaseTable.Doctor.colDepName))) {
						list.add((String)doctorContent.cv[i].get(DatabaseTable.Doctor.colDorName));
					}
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
    
    private Spinner.OnItemSelectedListener sOilDepName = new OnItemSelectedListener() { 
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    			dorNoRowId = 0;
    			depNameRowId = pos;
    			setSpinnerDoctorName();
    	}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	
	private Spinner.OnItemSelectedListener sOilDoctorName = new OnItemSelectedListener() { 
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    			dorNoRowId = pos;
    	}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	private ImageButton.OnClickListener iBtnOclPrevMonth = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (currentMonth <= 1) {
				currentMonth = 12;
				currentYear--;
			} else {
				currentMonth--;
			}
			getDoctorSchedule();
		}
	};

	private ImageButton.OnClickListener iBtnOclNextMonth = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (currentMonth > 11) {
				currentMonth = 1;
				currentYear++;
			} else {
				currentMonth++;
			}
			getDoctorSchedule();
		}
	};

	
	private Button.OnClickListener bOclSave = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if ( departContent == null || departContent.cv == null ) {
				Toast.makeText(v.getContext(), "無法儲存，無科別資料", Toast.LENGTH_LONG).show();
			} else if ( doctorContent == null || doctorContent.cv == null ) {
				Toast.makeText(v.getContext(), "無法儲存，無醫生資料", Toast.LENGTH_LONG).show();
			} else {
				setDoctorSchedule();
			}
		}
	};
	
	private Button.OnClickListener bOclQuery = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showSelectDialog();
		}
	};
	
	private ImageButton.OnClickListener iOclComfirm = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			currentYear  = dPikDate.getYear();
			currentMonth = dPikDate.getMonth()+1;
			
			if ( spnDepName.getCount() <= 0 ||  
							spnDorName.getCount() <= 0) {
				Toast.makeText(v.getContext(), "請確認資料是否已建立", Toast.LENGTH_LONG).show();
				
			} else {
				getDoctorSchedule();
			}
		}
	};
	
	private void getDoctorSchedule() {		
		showProgessDialog(DoctorSchedulingCalendarActivity.this,
				"資料讀取中，讀取時間依據您的網路速度而有不同");
		
		new Thread() {
			public void run() {				
				int status =0;
				setMonthInfo(currentYear, currentMonth);
				sendMessage(GET_DOCTOR_SCHEDULING_TAG, status);
			}
		}.start();
	}
	
	private void getDoctorScheduleResult(Message msg) {
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
			tViwDepName.setText((String)spnDepName.getSelectedItem());
			tViwDorName.setText((String)spnDorName.getSelectedItem());
			setCalendarValueToView(currentYear, currentMonth);
			dismissSelectDialog();
		}
	}

	private void setDoctorSchedule() {
		final String dorNo = (String)doctorContent.cv[dorNoRowId].get(DatabaseTable.Doctor.colDorNo);
		final String depName = (String)departContent.cv[depNameRowId].get(DatabaseTable.Department.colDepName);
		final String schYear = String.format("%02d", currentYear);
		final String schMonth = String.format("%02d", currentMonth);
		
		showProgessDialog(DoctorSchedulingCalendarActivity.this, "儲存中...");
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
		} else {
			alertDialog.setMessage(String.format("[%d] %s", status, "儲存成功。"));
		}
		alertDialog.show();
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
