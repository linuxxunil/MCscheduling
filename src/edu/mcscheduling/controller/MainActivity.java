package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import edu.mcscheduling.R.menu;
import edu.mcscheduling.common.StatusCode;
import edu.mcscheduling.model.DatabaseTable;
import edu.mcscheduling.model.SqliteDriver;
import edu.mcscheduling.model.SqliteDriver;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		test();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void test() {
		SqliteDriver sqlite = new SqliteDriver("/sdcard/data/cscheduling/cscheduling2.db");
		
		sqlite.onConnect();
		
			sqlite.createTable(DatabaseTable.Hospital.create());
			sqlite.createTable(DatabaseTable.CodeFile.create());
			sqlite.createTable(DatabaseTable.Doctor.create());
			sqlite.createTable(DatabaseTable.Department.create());
			sqlite.createTable(DatabaseTable.DoctorSchedule.create());
			sqlite.createTable(DatabaseTable.User.create());
	}
}
