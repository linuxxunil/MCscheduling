package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Doctor;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * �n�ק諸�a��:
 * (1)ListView��������ӨC�����q��Ʈw�����s���J�A�ӫD�aclass����array�Ӹ��J���
 *    
 * 
 * @author acer
 *
 */
public class DoctorInformation_Display_Activity extends ControllerActivity {

	/**
	 * �H�U��imageButton�ܼ�
	 */
	private ImageButton button_back;
	
	/**
	 * �H�U��button�ܼ�
	 */
	Button button_new;
	Button button_delete;
	Button button_revise;
	
	private ListView myListView;
	private SimpleAdapter adapter;
	
	
	private Doctor doctor = null;
	private ContentValues[] doctorContent = null;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private int clickPosition=-1;
	private View previousListView=null;
	

	/**
	 * �ثe�o��Activity
	 */
	public static Activity thisActivity;

	/**
	 * onCreate(Bundle savedInstanceState)
	 * 
	 * Activity���_�lfunction(ex main function)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setLayout();

		thisActivity = this;
		
		//initializeListView();
		
		//deBundle(savedInstanceState);
		
		// Listen for button clicks
		setListeners();
		
		doctor = new Doctor(db);
		doctorContent = doctor.getDoctor(getLoginID());
		
		setListView_DoctorInformation();
	}
	
	private void deBundle(Bundle bundle){
		bundle =this.getIntent().getExtras();
		
		if(bundle!=null){
			String doctorName = bundle.getString("doctorName");
			String medicalDepartment = bundle.getString("medicalDepartment");
			String jobTitle = bundle.getString("jobTitle");
			String phoneNumber = bundle.getString("phoneNumber");	
			addListViewRow(doctorName,medicalDepartment,jobTitle ,phoneNumber );	 
		}else{
			//Toast.makeText(getApplicationContext(), "bundle is null!!", Toast.LENGTH_LONG).show();	
		}
	
		
	}
	
	private void setLayout() {
		// set layout without titleBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set layout
		setContentView(R.layout.activity_display_doctor_information);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	
	private void initSimpleAdapter() {
		adapter = new SimpleAdapter( 
				this, 
				list,
				R.layout.display_doctor_information_list_view,
				new String[] {	DatabaseTable.Doctor.colDorName,
								DatabaseTable.Doctor.colDepName,
								DatabaseTable.Doctor.colJobTitle,
								DatabaseTable.Doctor.colTelephone },
				new int[] { R.id.textView_DisplayDoctorInformationPage_textView1, 
							R.id.textView_DisplayDoctorInformationPage_textView2, 
							R.id.textView_DisplayDoctorInformationPage_textView3, 
							R.id.textView_DisplayDoctorInformationPage_textView4 } );
	}
	
	
	public void setListView_DoctorInformation() {
		//���listview�ե�
		myListView = (ListView) findViewById(R.id.ListView_DisplayDoctorInformationPage_doctor);
		
		initSimpleAdapter();
		
		for ( int i=0; i< doctorContent.length; i++ ) {
			addListViewRow(
					(String) doctorContent[i].get(DatabaseTable.Doctor.colDorName),
					(String) doctorContent[i].get(DatabaseTable.Doctor.colDepName),
					(String) doctorContent[i].get(DatabaseTable.Doctor.colJobTitle),
					(String) doctorContent[i].get(DatabaseTable.Doctor.colTelephone)
					);
		}
		
		//�ҥΫ���L�o�\��]������listview����A���ݭngetListView��k�^
		myListView.setTextFilterEnabled(true);

		myListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(getApplicationContext(), "position is: " + position, Toast.LENGTH_LONG).show();	
				
				clickPosition = position;
				
				if(previousListView == null) {
					previousListView = v;
				}else{
					previousListView.setBackgroundResource(R.color.white);
					previousListView = v;
				}
			
				v.setBackgroundResource(R.color.gray);
			}			 
		 });		
	}
	
	public void addListViewRow(String name, String department, String jobTitle, String phoneNum){
		HashMap<String,String> item = new HashMap<String,String>();
		item.put(DatabaseTable.Doctor.colDorName, name);
		item.put(DatabaseTable.Doctor.colDepName, department);		
		item.put(DatabaseTable.Doctor.colJobTitle, jobTitle);
		item.put(DatabaseTable.Doctor.colTelephone, phoneNum);
				
		list.add(item);
		myListView.setAdapter( adapter );
	}

	public void removeListViewRow(int index){
		 list.remove(index);
		 myListView.setAdapter( adapter );
	}
	
	/**
	 * setListeners()
	 * 
	 * �]�m�C��button�Qclick���ɭԡA�n���檺function
	 */
	public void setListeners() {
		button_back = (ImageButton) findViewById(R.id.ImageButton_DisplayDoctorInformationPage_back);
		button_new = (Button) findViewById(R.id.button_DisplayDoctorInformationPage_new);
		button_delete = (Button) findViewById(R.id.button_DisplayDoctorInformationPage_delete);
		button_revise = (Button) findViewById(R.id.button_DisplayDoctorInformationPage_revise);
		
		button_back.setOnClickListener(back);
		button_new.setOnClickListener(newListViewRow);
		button_delete.setOnClickListener(deleteListViewRow);
		button_revise.setOnClickListener(revise);
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
			intent.setClass(DoctorInformation_Display_Activity.this, MenuActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	private Button.OnClickListener newListViewRow = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			//for test
			Intent intent = new Intent();
			intent.setClass(DoctorInformation_Display_Activity.this, DoctorInformation_New_Activity.class);
			startActivity(intent);
			finish();			
		}
	};
	
	private Button.OnClickListener deleteListViewRow = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(clickPosition != -1){
				
				String dorNo = (String)doctorContent[clickPosition].get((DatabaseTable.Doctor.colDorNo));
				
				if ( doctor.deleteDoctor(getLoginID(), dorNo) == StatusCode.success ) {			
					Toast.makeText(getApplicationContext(), "�R�����\", Toast.LENGTH_LONG);
					removeListViewRow(clickPosition);
					clickPosition=-1;
				} else {
					Toast.makeText(getApplicationContext(), "�R������", Toast.LENGTH_LONG);
				}
			}else{
				Toast.makeText(getApplicationContext(), "�Х���ܭn�R�������", Toast.LENGTH_LONG)
				.show();	
			}

		}
	};
	
	private Button.OnClickListener revise = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if( clickPosition != -1 ){
				Intent intent = new Intent();
				intent.setClass(DoctorInformation_Display_Activity.this, DoctorInformation_Edit_Activity.class);
				
				Bundle bundle = new Bundle();
				bundle.putInt("Position", clickPosition);
	            //�NBundle����assign��intent
	            intent.putExtras(bundle);
				startActivity(intent);
				finish();	
				clickPosition=-1;			
			}else{
				Toast.makeText(getApplicationContext(), "�Х���ܭn�ק諸���", Toast.LENGTH_LONG)
				.show();	
			}
			
		
		}
	};	
	
}
