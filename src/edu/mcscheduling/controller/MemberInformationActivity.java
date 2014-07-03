package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.List;

import edu.mcscheduling.R;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.Account;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.MsContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MemberInformationActivity extends ControllerActivity {

	private final String[] passQuestionList = { "就讀的國小學校", "目前的居住地", "喜歡的球類運動",
			"最愛的歌星", "最喜歡的車子" };
	private final String[] workTypeList = { "無" };

	private Button button_save;
	private Button button_uploadIDphoto;
	private MsContentValues content = null;
	private EditText userid = null;
	private EditText username = null;
	private Spinner userpwdquestion = null;
	private EditText userpwdans = null;
	private Spinner workType = null;
	private EditText uservaild = null;
	
	private final int INIT_TAG = 1;
	private final int SET_TAG = 2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initHandler();
		
		initValueToView();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	private void setSpinner_PasswordTip() {

		// get reference to the spinner from the XML layout
		Spinner spinner = userpwdquestion;

		// Array list of animals to display in the spinner
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < passQuestionList.length; i++)
			list.add(passQuestionList[i]);

		// create an ArrayAdaptar from the String Array
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// set the view for the Drop down list
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// set the ArrayAdapter to the spinner
		spinner.setAdapter(dataAdapter);
		// attach the listener to the spinner
		spinner.setOnItemSelectedListener(new UtilitySpinnerOnItemSelectedListener());

	}

	private void setSpinner_WorkPattern() {

		// get reference to the spinner from the XML layout
		Spinner spinner = workType;

		// Array list of animals to display in the spinner
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < workTypeList.length; i++)
			list.add(workTypeList[i]);

		// create an ArrayAdaptar from the String Array
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// set the view for the Drop down list
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// set the ArrayAdapter to the spinner
		spinner.setAdapter(dataAdapter);
		// attach the listener to the spinner
		spinner.setOnItemSelectedListener(new UtilitySpinnerOnItemSelectedListener());

	}

	private void initLayout() {
		// set layout
		setContentView(R.layout.activity_member_information);

		// let screen orientation be vertical
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the focus on the layout not the edittext
		findViewById(R.id.layout_memberInformation).requestFocus();
	}

	private void initListeners() {
		userid = (EditText) findViewById(R.id.EditText_MemberInformationPage_email);
		username = (EditText) findViewById(R.id.EditText_MemberInformationPage_username);
		userpwdquestion = (Spinner) findViewById(R.id.Spinner_MemberInformationPage_passwordTip);
		userpwdans = (EditText) findViewById(R.id.EditText_MemberInformationPage_tips);
		workType = (Spinner) findViewById(R.id.Spinner_MemberInformationPage_workPattern);
		uservaild = (EditText) findViewById(R.id.EditText_MemberInformationPage_registrationDate);
		button_save = (Button) findViewById(R.id.button_MemberInformationPage_save);
		button_uploadIDphoto = (Button) findViewById(R.id.button_MemberInformationPage_uploadIDphoto);

		button_save.setOnClickListener(save);
		button_uploadIDphoto.setOnClickListener(uploadIDphoto);
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case INIT_TAG:
					initValueToViewResult(msg);
					break;
				case SET_TAG:
					setMemberInformationResult(msg);
				}
			}
		};
	}

	private void initValueToView() {
		showProgessDialog(MemberInformationActivity.this,
				"資料讀取中，讀取時間依據您的網路速度而有不同");
		new Thread() {
			public void run() {
				Account account = new Account(db);
				content = account.getMemberInformation(getLoginID());
				sendMessage(INIT_TAG, content.status);
			}
		}.start();

	}

	private void initValueToViewResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();

		initLayout();
		initListeners();

		String strTmp = null;
		setSpinner_PasswordTip();
		setSpinner_WorkPattern();

		if (status != StatusCode.success) {

			Builder alertDialog = new AlertDialog.Builder(
					MemberInformationActivity.this);
			alertDialog.setTitle("提示");
			alertDialog.setPositiveButton("確定", null);
			switch (status) {
			case -12402:
				alertDialog.setMessage(String.format("[%08d] %s", status,
						"連線失敗。"));
				break;
			case -23303:
				alertDialog.setMessage(String.format("[%08d] %s", status,
						"讀取失敗。"));
				break;
			default:
				alertDialog.setMessage(String.format("[%08d] %s", status,
						"未知錯誤。"));
				break;
			}
			alertDialog.show();
		} else {
			if (content.cv == null) {
				userid.setText(getLoginID());
				username.setText("");
				userpwdquestion.setSelection(0);
				userpwdans.setText("");
				workType.setSelection(0);
				uservaild.setText("");
			} else {
				// User ID
				strTmp = (String) content.cv[0]
						.get(DatabaseTable.User.colUserid);
				userid.setText(strTmp == null ? "" : strTmp);

				// Username
				strTmp = (String) content.cv[0]
						.get(DatabaseTable.User.colUsername);
				username.setText(strTmp == null ? "" : strTmp);

				// User Password Question
				strTmp = (String) content.cv[0]
						.get(DatabaseTable.User.colUserpwdquestion);

				if (strTmp != null) {
					for (int i = 0; i < passQuestionList.length; i++) {
						if (passQuestionList[i].equals(strTmp)) {
							userpwdquestion.setSelection(i);
							break;
						}
					}
				} else {
					userpwdquestion.setSelection(0);
				}

				// User Password Answer
				strTmp = (String) content.cv[0]
						.get(DatabaseTable.User.colUserpwdans);
				userpwdans.setText(strTmp == null ? "" : strTmp);

				// Work Type
				// strTmp =
				// (String)content[0].get(DatabaseTable.User.colUserpwdquestion);
				workType.setSelection(0);

				// User valid
				strTmp = (String) content.cv[0]
						.get(DatabaseTable.User.colUservalid);
				uservaild.setText(strTmp == null ? "" : strTmp);
			}
		}
	}

	private Button.OnClickListener uploadIDphoto = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {

			Toast.makeText(getApplicationContext(), "選擇要上傳的檔案",
					Toast.LENGTH_LONG).show();
		}
	};

	private Button.OnClickListener save = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showProgessDialog(MemberInformationActivity.this, "儲存中...");

			setMemberInformation();
		}
	};

	private void setMemberInformation() {
		new Thread() {
			public void run() {
				int status = StatusCode.success;
				Account account = new Account(db);
				status = account.setMemberInformation(userid.getText()
						.toString(), // userid
						username.getText().toString(), // username
						userpwdquestion.getSelectedItem().toString(), // passwdQuestion
						userpwdans.getText().toString() // passwdAnswer
						);
				sendMessage(SET_TAG, status);
			}
		}.start();
	}

	private void setMemberInformationResult(Message msg) {
		int status = msg.getData().getInt("status");
		dismissProgresDialog();

		Builder alertDialog = new AlertDialog.Builder(
				MemberInformationActivity.this);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			changeActivity(MemberInformationActivity.this, MenuActivity.class);
		}
		return true;
	}
}
