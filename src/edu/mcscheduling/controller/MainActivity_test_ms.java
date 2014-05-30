package com.example.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import net.sourceforge.jtds.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends Activity {
	TextView mytext;


	private MSSqlDriver msdata = new MSSqlDriver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mytext = (TextView) findViewById(R.id.textView1);

		ConnectTask dt = new ConnectTask();
		dt.execute();
	}

	class ConnectTask extends AsyncTask<Integer, Integer, String> {
		Connection con = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			int a = msdata.onConnect();
			ResultSet rs = msdata.selectMS("SELECT TOP 1000 [HospitalNo] FROM [cscheduling].[dbo].[Doctor]"); 
				try {
					while (rs.next()) { 
						Log.i("Jason", "HospitalNo :" + rs.getString("HospitalNo"));
					}
					rs.close();
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
				}
				
			int b = msdata.inset("INSERT INTO [cscheduling].[dbo].[Department] ([HospitalNo],[DepName],[UpdateID],[UpdateDate],[Desc]) VALUES ('00007','外科','b@b.com','2014-03-31 00:00:00.000','NULL')"); 
			int c = msdata.delete("DELETE FROM [cscheduling].[dbo].[Department] WHERE [DepName]='外科'"); 
			int d = msdata.update("UPDATE [cscheduling].[dbo].[Department] SET [HospitalNo]=77777 WHERE [DepName]='竹科'");
			int e = msdata.createTable("CREATE TABLE [dbo].[test]([HospitalNo] [nvarchar](10) NOT NULL,[DepName] [nvarchar](50) NULL) ON [PRIMARY]");
			return "success";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void button1click(View view) {
		mytext.setText("fuck");
	}

}
