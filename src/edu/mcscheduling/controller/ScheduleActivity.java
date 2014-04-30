package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.mcscheduling.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScheduleActivity extends ControllerActivity {

	/**
	 * 以下為imageButton變數
	 */
	private ImageButton button_back;
	
	private UtilitySpinnerOnItemSelectedListener itemSelectedListener;

	ListView myListView;
	private SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	int selectedPosition;
	
	private static final String[] day = new String[] {
	"周一", "週二", "週三", "週四", "週五", "週六", "週日"
	};
	

	private static final String[] date = new String[] {
		"2/1", "2/2", "2/3", "2/4", "2/5", "2/6", "2/7"
	};

	private static final String[] monDoctor = new String[] {
		"王醫師", "休診", "林醫師", "鍾醫師", "陳醫師", "休診", "休診"
	};

	
	private static final String[] noonDoctor = new String[] {
		"林醫師", "鍾醫師", "陳醫師", "休診", "休診","王醫師", "休診"
	};

	private static final String[] nightDoctor = new String[] {
		"鍾醫師", "陳醫師", "休診", "休診","王醫師", "休診", "林醫師"
	};
	
	/**
	 * 目前這個Activity
	 */
	public static Activity thisActivity;

	
	View previousListView=null;
	
	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity的起始function(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setLayout();

		thisActivity = this;
		
		initializeListView();

        /**
         *  Creates all dynamically spinners
         */
		createAllSpinners();
		
		// Listen for button clicks
		setListeners();

	}
	
	private void createAllSpinners(){
        /**
         *  Creates all dynamically spinner
         */
		createSpinnerMedicalDepartment();
		createSpinnerDoctorName();
		createSpinnerWeek();		
	}
	
    private void createSpinnerMedicalDepartment() {
   	 
        //get reference to the spinner from the XML layout
        Spinner spinner = (Spinner) findViewById(R.id.Spinner_SchedulePage_medicalDepartment);
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        list.add("科別");
        list.add("內科");
        list.add("小兒科");
        list.add("外科");
        list.add("婦產科");
        list.add("牙科");
  
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

    private void createSpinnerDoctorName() {
      	 
        //get reference to the spinner from the XML layout
        Spinner spinner = (Spinner) findViewById(R.id.Spinner_SchedulePage_doctorName);
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        list.add("醫師名");
        list.add("全部");
        list.add("鍾醫師");
        list.add("王醫師");
        list.add("林醫師");
        list.add("陳醫師");
        list.add("趙醫師");
  
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
  
    private void createSpinnerWeek() {
     	 
        //get reference to the spinner from the XML layout
        Spinner spinner = (Spinner) findViewById(R.id.Spinner_SchedulePage_week);
        
        //Array list of animals to display in the spinner
        List<String> list = new ArrayList<String>();
        list.add("週數");
        list.add("第一週");
        list.add("第二週");
        list.add("第三週");
        list.add("第四週");
  
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
    
	private void setLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_schedule);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void initializeListView(){
		//找到listview組件
		myListView = (ListView) findViewById(R.id.ListView_SchedulePage_doctor);
		 
		 //把資料加入ArrayList中
		 for(int i=0; i<monDoctor.length; i++){
			 HashMap<String,String> item = new HashMap<String,String>();
			 item.put("day", day[i]);
			 item.put("date",date[i]);
			 item.put("monDoctor",monDoctor[i] );
			 item.put("noonDoctor",noonDoctor[i]);
			 item.put("nightDoctor",nightDoctor[i]);
			 list.add( item );
		 }
		 
		 //新增SimpleAdapter
		 adapter = new SimpleAdapter( 
		 this, 
		 list,
		 R.layout.schedule_list_view,
		 new String[] { "day","date","monDoctor","noonDoctor","nightDoctor" },
		 new int[] { R.id.textView_SchedulePage_day, R.id.textView_SchedulePage_date, R.id.textView_SchedulePage_monDoctor, R.id.textView_SchedulePage_noonDoctor , R.id.textView_SchedulePage_nightDoctor} );
		 
		 //ListActivity設定adapter
		 myListView.setAdapter( adapter );
		 
		 //啟用按鍵過濾功能（直接用listview物件，不需要getListView方法）
		 myListView.setTextFilterEnabled(true);

		 myListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Toast.makeText(getApplicationContext(), "position is: " + position, Toast.LENGTH_LONG).show();	
				selectedPosition=position;
				
				if(previousListView==null) {
					previousListView=v;
				}else{
					previousListView.setBackgroundResource(R.color.white);
					previousListView=v;
				}
				
				v.setBackgroundResource(R.color.gray);				
			}			 
		 });		
	}
		
	/**
	 * setListeners()
	 * 
	 * 設置每個button被click的時候，要執行的function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_SchedulePage_back);

		button_back.setOnClickListener(back);
	}

	/**
	 * back
	 * 
	 * 當你按下back按鈕，返回首頁(Home)
	 */
	private ImageButton.OnClickListener back = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			//for test
			Intent intent = new Intent();
			intent.setClass(ScheduleActivity.this, TestActivity.class);
			startActivity(intent);
			finish();
			
			/*
			Intent intent = new Intent();
			intent.setClass(EnrollActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			*/
		}
	};

	/**
	 *  Below is unused methods...
	 */
	public void addListViewRow(String name, String department, String jobTitle, String phoneNumber){
		/*
		 HashMap<String,String> item = new HashMap<String,String>();
		 item.put("day", name);
		 item.put("date",department);
		 item.put("monDoctor",jobTitle );
		 item.put("noonDoctor",phoneNumber);	
		 item.put("nightDoctor",  xx);
		 list.add( item );
		 myListView.setAdapter( adapter );
		 */
	}
}
