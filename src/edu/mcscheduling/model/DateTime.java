package edu.mcscheduling.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {

	public String getDateTime() {
		Date time = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
							Locale.getDefault());
		return  formatter.format(time);
	}
	
	@Override
	public String toString() {
		return getDateTime();
	}
}
