package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.HashMap;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.Department;
import edu.mcscheduling.model.Doctor;
import edu.mcscheduling.model.Hospital;
import edu.mcscheduling.model.MsContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 要修改的地方:
 * (1)ListView的資料應該每次都從資料庫中重新載入，而非靠class內的array來載入資料
 *    
 * 
 * @author acer
 *
 */
public class DoctorInformation_Display_Activity extends ControllerActivity {
	/**
	 * 以下為button變數
	 */
	Button button_new;
	Button button_delete;
	Button button_revise;
	
	private ListView myListView;
	private SimpleAdapter adapter;
	
	
	private Doctor doctor = null;
	private Department depart = null;
	private MsContentValues doctorContent = null;
	private MsContentValues departContent = null;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private int clickPosition=-1;
	private View previousListView=null;
	
	private final int INIT_TAG = 1;
	private final int DEL_DOCTOR_TAG = 2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initHandler();
		
		initValueToView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case INIT_TAG:
					initValueToViewResult(msg);
					break;
				case DEL_DOCTOR_TAG:
					deleteDoctorResult(msg);
					break;
				}
			}
		};
	}
	
	private void initLayoutDoctorList() {
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
	
	private void initLayout() {
		// set layout
		setContentView(R.layout.activity_display_doctor_information);

		// let screen orientation be landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initLayoutDoctorList();
	}
	
	private void initListeners() {
		button_new = (Button) findViewById(R.id.button_DisplayDoctorInformationPage_new);
		button_delete = (Button) findViewById(R.id.button_DisplayDoctorInformationPage_delete);
		button_revise = (Button) findViewById(R.id.button_DisplayDoctorInformationPage_revise);
		myListView = (ListView) findViewById(R.id.ListView_DisplayDoctorInformationPage_doctor);
		
		button_new.setOnClickListener(newListViewRow);
		button_delete.setOnClickListener(deleteListViewRow);
		button_revise.setOnClickListener(revise);
	}
	
	private void initValueToView() {
		showProgessDialog(DoctorInformation_Display_Activity.this,
				"資料讀取中，讀取時間依據您的網路速度而有不同");
		new Thread() {
			public void run() {
				depart = new Department(db);
				departContent = depart.getDepartment(getLoginID());
				
				if ( departContent.status != StatusCode.success ) {
					sendMessage(INIT_TAG, departContent.status);
					return ;
				}
				
				doctor = new Doctor(db);
				doctorContent = doctor.getDoctor(getLoginID());
				sendMessage(INIT_TAG, doctorContent.status);
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
					DoctorInformation_Display_Activity.this);
			
			alertDialog.setTitle("提示");
			switch (status) {
			case -12402:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"連線失敗。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorInformation_Display_Activity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			case -23303:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"讀取失敗。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorInformation_Display_Activity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			case 33001:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"尚未建立部門資料。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		changeActivity(DoctorInformation_Display_Activity.this, MenuActivity.class);
    	        	}
    	        });
				break;
			case 34001:
				alertDialog.setMessage(String.format("[%d] %s", status,
						"尚未建立醫生資料。"));
				alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		
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
			if ( doctorContent.cv != null ) {
				for ( int i=0; i< doctorContent.cv.length; i++ ) {
					addListViewRow(
							(String) doctorContent.cv[i].get(DatabaseTable.Doctor.colDorName),
							(String) doctorContent.cv[i].get(DatabaseTable.Doctor.colDepName),
							(String) doctorContent.cv[i].get(DatabaseTable.Doctor.colJobTitle),
							(String) doctorContent.cv[i].get(DatabaseTable.Doctor.colTelephone)
					);
				}
			}
		
			//	啟用按鍵過濾功能（直接用listview物件，不需要getListView方法）
			myListView.setTextFilterEnabled(true);

			myListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
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
	}
	
	private void addListViewRow(String name, String department, String jobTitle, String phoneNum){
		HashMap<String,String> item = new HashMap<String,String>();
		item.put(DatabaseTable.Doctor.colDorName, name);
		item.put(DatabaseTable.Doctor.colDepName, department);		
		item.put(DatabaseTable.Doctor.colJobTitle, jobTitle);
		item.put(DatabaseTable.Doctor.colTelephone, phoneNum);
				
		list.add(item);
		myListView.setAdapter( adapter );
	}

	private void removeListViewRow(int index){
		 list.remove(index);
		 myListView.setAdapter( adapter );
	}
	
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
			if(clickPosition == -1){
				Toast.makeText(getApplicationContext(), "請先選擇要刪除的欄位", Toast.LENGTH_LONG)
				.show();	
			} else {
				showProgessDialog(DoctorInformation_Display_Activity.this,"刪除中...");
				deleteDoctor();
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
	            //將Bundle物件assign給intent
	            intent.putExtras(bundle);
				startActivity(intent);
				finish();	
				clickPosition=-1;			
			}else{
				Toast.makeText(getApplicationContext(), "請先選擇要修改的欄位", Toast.LENGTH_LONG)
				.show();	
			}
		}
	};	
	
	
	private void deleteDoctor() {
		new Thread() {
			public void run() {
				String dorNo = (String)doctorContent.cv[clickPosition].
										get((DatabaseTable.Doctor.colDorNo));
				int status = doctor.deleteDoctor(getLoginID(), dorNo);
				sendMessage(DEL_DOCTOR_TAG, status);
			}
		}.start();
	}
	
	private void deleteDoctorResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();
		
		Builder alertDialog = new AlertDialog.Builder(
				DoctorInformation_Display_Activity.this);
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
			removeListViewRow(clickPosition);
			clickPosition=-1;
			alertDialog.setMessage(String.format("[%d] %s", status, "刪除成功。"));
			alertDialog.show();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			changeActivity(DoctorInformation_Display_Activity.this, MenuActivity.class);
		}
		return true;
	}
}
