package edu.mcscheduling.model;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Account {
	private DatabaseDriver db;

	public Account(DatabaseDriver db) {
		this.db = db;
	}

	/**
	 * register 使用者註冊，會回傳成功與否
	 * 
	 * @param userid
	 * @param username
	 * @param userpasswd
	 * @throws Exception
	 */
	public void register(String userid, String username, String userpasswd)
			throws Exception {
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());

		String columns = String.format("`%s`,`%s`,`%s`,`%s`",
				DatabaseTable.User.colUserid, DatabaseTable.User.colUsername,
				DatabaseTable.User.colUserpasswd,
				DatabaseTable.User.colUservalid);

		String values = String.format("`%s`,`%s`,`%s`,`%s`", userid, username,
				userpasswd, formatter.format(time));

		db.inset(DatabaseTable.User.name, columns, values);
	}

	public boolean login(String userid, String userpasswd) throws Exception {
		String columns = String.format("`%s`,`%s`",
				DatabaseTable.User.colUserid, DatabaseTable.User.colUserpasswd);

		String whereExpr = String.format("`%s`=`%s`", userid, userpasswd);

		Cursor cursor = db.select(DatabaseTable.User.name, columns, whereExpr);

		if (cursor != null && cursor.getCount() == 1)
			return true;

		return false;
	}

}
