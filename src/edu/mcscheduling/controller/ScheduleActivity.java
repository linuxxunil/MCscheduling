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
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_back;
	
	private UtilitySpinnerOnItemSelectedListener itemSelectedListener;

	ListView myListView;
	private SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	int selectedPosition;
	
	private static final String[] day = new String[] {
	"�P�@", "�g�G", "�g�T", "�g�|", "�g��", "�g��", "�g��"
	};
	

	private static final String[] date = new String[] {
		"2/1", "2/2", "2/3", "2/4", "2/5", "2/6", "2/7"
	};

	private static final String[] monDoctor = new String[] {
		"����v", "��E", "�L��v", "����v", "����v", "��E", "��E"
	};

	
	private static final String[] noonDoctor = new String[] {
		"�L��v", "����v", "����v", "��E", "��E","����v", "��E"
	};

	private static final String[] nightDoctor = new String[] {
		"����v", "����v", "��E", "��E","����v", "��E", "�L��v"
	};
	
	/**
	 * �ثe�o��Activity
	 */
	public static Activity thisActivity;

	
	View previousListView=null;
	
	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity���_�lfunction(ex main function)
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
        list.add("��O");
        list.add("����");
        list.add("�p���");
        list.add("�~��");
        list.add("������");
        list.add("����");
  
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
        list.add("��v�W");
        list.add("����");
        list.add("����v");
        list.add("����v");
        list.add("�L��v");
        list.add("����v");
        list.add("����v");
  
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
        list.add("�g��");
        list.add("�Ĥ@�g");
        list.add("�ĤG�g");
        list.add("�ĤT�g");
        list.add("�ĥ|�g");
  
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
		//���listview�ե�
		myListView = (ListView) findViewById(R.id.ListView_SchedulePage_doctor);
		 
		 //���ƥ[�JArrayList��
		 for(int i=0; i<monDoctor.length; i++){
			 HashMap<String,String> item = new HashMap<String,String>();
			 item.put("day", day[i]);
			 item.put("date",date[i]);
			 item.put("monDoctor",monDoctor[i] );
			 item.put("noonDoctor",noonDoctor[i]);
			 item.put("nightDoctor",nightDoctor[i]);
			 list.add( item );
		 }
		 
		 //�s�WSimpleAdapter
		 adapter = new SimpleAdapter( 
		 this, 
		 list,
		 R.layout.schedule_list_view,
		 new String[] { "day","date","monDoctor","noonDoctor","nightDoctor" },
		 new int[] { R.id.textView_SchedulePage_day, R.id.textView_SchedulePage_date, R.id.textView_SchedulePage_monDoctor, R.id.textView_SchedulePage_noonDoctor , R.id.textView_SchedulePage_nightDoctor} );
		 
		 //ListActivity�]�wadapter
		 myListView.setAdapter( adapter );
		 
		 //�ҥΫ���L�o�\��]������listview����A���ݭngetListView��k�^
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
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_SchedulePage_back);

		button_back.setOnClickListener(back);
	}

	/**
	 * back
	 * 
	 * ��A���Uback���s�A��^����(Home)
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
