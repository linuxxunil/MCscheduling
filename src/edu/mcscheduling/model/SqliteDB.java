package edu.mcscheduling.model;

import java.sql.SQLException;


import android.database.Cursor;

public class SqliteDB {
	private static final String dbPath = "/sdcard/data/cscheduling/cscheduling.db";
	private static SqliteDriver db;

	public SqliteDB() {

		try {

			db = new SqliteDriver(dbPath);
			db.createTable(DatabaseTable.CodeFile.create());
			db.createTable(DatabaseTable.Dcotor.create());
			db.createTable(DatabaseTable.Department.create());
			db.createTable(DatabaseTable.DocSchedule.create());
			db.createTable(DatabaseTable.Hospital.create());
			db.createTable(DatabaseTable.User.create());
			/*
			 * Account account = new Account(db);
			 * 
			 * account.register("a@a.com", "jesse", "1qazxcv");
			 * 
			 * Cursor cursor = db.select("select *  from User"); int rows_num =
			 * cursor.getCount();
			 * 
			 * cursor.moveToFirst(); for ( int i=0; i<rows_num; i++) {
			 * System.out.println(cursor.getString(0)); cursor.moveToNext(); }
			 * cursor.close();
			 */
			//db.update(DatabaseTable.User.name, "'a@a.com'='b@b.com'",
			// "`userid`=`a@a.com`");
		} catch (Exception e) {

			// System.out.println(e.getMessage() + e.getLocalizedMessage());
		}

	}

}
