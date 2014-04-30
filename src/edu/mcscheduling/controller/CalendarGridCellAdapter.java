package edu.mcscheduling.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.mcscheduling.R;

public class CalendarGridCellAdapter extends BaseAdapter implements OnClickListener {	
	private static final int DAY_OFFSET = 1;
	
	private final String[] weekdays = new String[] { 
			"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	
	private final String[] months = { "01", "02", "03", "04", 
									  "05", "06", "07", "08", 
									  "09", "10", "11", "12" };

	private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	

	private Button gridcell;
	private TextView num_events_per_day;
	//private final HashMap<String, Integer> eventsPerMonthMap;
	
	private final SimpleDateFormat dateFormatter = 
						new SimpleDateFormat("dd-MMM-yyyy");

	private DoctorSchedulingCalendarActivity schedulingActivity;
	
	private int daysInMonth;
	private int currentDayOfMonth;
	private int currentWeekDay;

	
	private final Context context;
	private final List<String> list;
	//for changing cell background
	private int idToChangeBackground=-1;
	
	private boolean[][] selectedItem = new boolean[31][3];
	private HashMap<String, Integer> eventsPerMonthMap;
	// Days in Current Month
	public CalendarGridCellAdapter(DoctorSchedulingCalendarActivity activity,
									Context context, 
									int textViewResourceId,
									int month, int year) {
		super();
		
		schedulingActivity = activity;
		this.context = context;
		
		list = new ArrayList<String>();

		Calendar calendar = Calendar.getInstance();
		currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		currentWeekDay    = calendar.get(Calendar.DAY_OF_WEEK);
		
		/**
		 *  設定每個日期按鍵上的value
		 */
		//setMonthInfoToList (month, year);

		// Find Number of Events
		eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);

	}

	private String getMonthAsString(int i) {
		return months[i];
	}

	private String getWeekDayAsString(int i) {
		return weekdays[i];
	}

	private int getNumberOfDaysOfMonth(int i) {
		return daysOfMonth[i];
	}

	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	/**
	 * Prints Month
	 * 
	 * @param mm
	 * @param yy
	 */
	private void setMonthInfoToList (int mm, int yy) {
		int trailingSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;

		int currentMonth = mm - 1;
		
		String currentMonthName = getMonthAsString(currentMonth);

		daysInMonth = getNumberOfDaysOfMonth(currentMonth);

		GregorianCalendar gregCalendar = new GregorianCalendar(yy, currentMonth, 1);

		
		if ( currentMonth == 11 ) { // 12月
			prevMonth = currentMonth - 1;
			nextMonth = 0;
			prevYear = yy;
			nextYear = yy + 1;
		} else if ( currentMonth == 0 ) { // 1月
			prevMonth = 11;
			nextMonth = currentMonth + 1;
			prevYear = yy - 1;
			nextYear = yy;
		} else {
			prevMonth = currentMonth - 1;
			nextMonth = currentMonth + 1;
			nextYear = yy;
			prevYear = yy;
		}
		
		daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
		
		// 取得當月第一天是星期幾
		int firstDayOfWeek = gregCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		trailingSpaces = firstDayOfWeek;

		// 設定閏年
		if (gregCalendar.isLeapYear(gregCalendar.get(Calendar.YEAR)))
			if (mm == 2)
				++daysInMonth;
			else if (mm == 3)
				++daysInPrevMonth;

		int id=0;
		String isMornSelected="null";
		String isNoonSelected="null";
		String isNightSelected="null";
		
		// Trailing Month days
		for (int i = 0; i < trailingSpaces; i++) {
			list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)+ i)	 
					+ "-GREY"
					+ "-" + getMonthAsString(prevMonth)
					+ "-" + prevYear 
					+ "-" + id 
					+ "-" + "null" 
					+ "-" + "null" 
					+ "-" + "null");
			id++;
		}

		// Current Month Days
		for (int i = 1; i <= daysInMonth; i++) {
			//Log.d(currentMonthName, String.valueOf(i) + " "
			//		+ getMonthAsString(currentMonth) + " " + yy + "-" +id + "-" + isMornSelected + "-" + isNoonSelected + "-" + isNightSelected);
			if (i == getCurrentDayOfMonth()) {
				list.add(String.valueOf(i) 
						+ "-BLUE"
						+ "-" + getMonthAsString(currentMonth) 
						+ "-" + yy 
						+ "-" + id 
						+ "-" + isMornSelected 
						+ "-" + isNoonSelected 
						+ "-" + isNightSelected);
			} else {
				list.add(String.valueOf(i) 
						+ "-WHITE" 
						+ "-" + getMonthAsString(currentMonth) 
						+ "-" + yy 
						+ "-" + id 
						+ "-" + isMornSelected 
						+ "-" + isNoonSelected 
						+ "-" + isNightSelected);
			}
			id++;
		}
		
		// Leading Month days
		for (int i = 0; i < list.size() % 7; i++) {
			//Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
			list.add(String.valueOf(i + 1) 
					+ "-GREY" 
					+ "-" + getMonthAsString(nextMonth) 
					+ "-" + nextYear 
					+ "-" + id 
					+ "-" + isMornSelected 
					+ "-" + isNoonSelected 
					+ "-" + isNightSelected);
			id++;
		}
	}

	/**
	 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
	 * ALL entries from a SQLite database for that month. Iterate over theul4
	 * List of All entries, and get the dateCreated, which is converted into
	 * day.
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
			int month) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		return map;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private View setConsultingInfoToButton(int position, View convertView, ViewGroup parent) {
		System.out.println("AAAAAAAAAA");
		
		View row = convertView;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.screen_gridcell, parent, false);
		}

		// Get a reference to the Day gridcell
		gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
		gridcell.setOnClickListener(this);

		// ACCOUNT FOR SPACING

		//Log.d(tag, "Current Day: " + list.get(position));
		
		// 擷取
		String[] buttonInfo = list.get(position).split("-");
		String day 		= buttonInfo[0];
		//Strind dayColr = day_color[1];
		String month 	= buttonInfo[2];
		String year 	= buttonInfo[3];
		String id 		= buttonInfo[4];
		String isMornSelected = buttonInfo[5];
		String isNoonSelected = buttonInfo[6];
		String isNightSelected = buttonInfo[7];
		
		if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
			if (eventsPerMonthMap.containsKey(day)) {
				num_events_per_day = (TextView) row
						.findViewById(R.id.num_events_per_day);
				Integer numEvents = (Integer) eventsPerMonthMap.get(day);
				num_events_per_day.setText(numEvents.toString());
			}
		}

		// 設定 button 內容
		gridcell.setText(day);
		gridcell.setTag(day 
				+ "-" + month 
				+ "-" + year 
				+ "-" + id
						);
		
		// 設定 button 裡的字體顏色
		if (buttonInfo[1].equals("GREY")) {
			gridcell.setTextColor(context.getResources()
					.getColor(R.color.lightgray));
		}
		if (buttonInfo[1].equals("WHITE")) {
			gridcell.setTextColor(context.getResources().getColor(
					R.color.lightgray02));
		}
		if (buttonInfo[1].equals("BLUE")) {  //設定當今日期的顏色
			//gridcell.setTextColor(_context.getResources().getColor(R.color.orrange));
			gridcell.setTextColor(context.getResources().getColor(R.color.lightgray02));
		}
		
		
		// 設定按鈕顏色
		
		/*
		if("true".equals(isMornSelected) || "false".equals(isMornSelected)){

			if("true".equals(isMornSelected)){
				if("true".equals(isNoonSelected)){
					if("true".equals(isNightSelected)){
						//1
						gridcell.setBackgroundResource(R.drawable.gridcell_background_1);
					}else{
						//2
						gridcell.setBackgroundResource(R.drawable.gridcell_background_2);
					}
				}else{
					if("true".equals(isNightSelected)){
						//3
						gridcell.setBackgroundResource(R.drawable.gridcell_background_3);
					}else{
						//4
						gridcell.setBackgroundResource(R.drawable.gridcell_background_4);
					}				
				}
			}else{
				if("true".equals(isNoonSelected)){
					if("true".equals(isNightSelected)){
						//5
						gridcell.setBackgroundResource(R.drawable.gridcell_background_5);
					}else{
						//6
						gridcell.setBackgroundResource(R.drawable.gridcell_background_6);
					}
				}else{
					if("true".equals(isNightSelected)){
						//7
						gridcell.setBackgroundResource(R.drawable.gridcell_background_7);
					}else{
						//8
						gridcell.setBackgroundResource(R.drawable.gridcell_background_8);
					}				
				}			
			}			
		
		}else{
			//do nothing
		}
	*/
	
		return row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return setConsultingInfoToButton(position, convertView, parent);
	}

	
	public void openOptionsDialog_selectConsultingHour()  
	{
    	AlertDialog.Builder builder=new AlertDialog.Builder(schedulingActivity);
    	builder.setTitle("選擇看診時間");
    	CharSequence[] session ={"早上","下午","晚上"};
    	boolean[]   sessionStatus = new boolean[3];
    	for ( int i=0; i<sessionStatus.length; i++ )
    		sessionStatus[i]=false;	
    	
    		builder.setMultiChoiceItems(session, 
    			new boolean[]{false,true,false}, 
    			new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// 紀錄時段
				//sessionStatus[which]=isChecked;	
			}
		});
    	
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            	
                //Toast.makeText(getApplicationContext(), "Selected date: " + selectedYear+"/"+selectedMonth+"/"+selectedDay + selecteditem, Toast.LENGTH_LONG).show();
                //adapter.setListContentByChoice(adapter.getListCurrentPosition(),sessionStatus[0], sessionStatus[1], sessionStatus[2]);                
                //adapter.notifyDataSetChanged();
            }
        });
    	

    	builder.show();
    }	
	@Override
	public void onClick(View view) {	
		String date_month_year = (String) view.getTag();
		
		System.out.println(date_month_year);
		String[] item = date_month_year.split("-");
		Toast.makeText(context, "The id you select is: " + item[3], Toast.LENGTH_LONG).show();
		
		int id=Integer.parseInt(item[3]);
		
		idToChangeBackground=id;
		
		try {
			//先行定義時間格式
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			//取得現在時間
			Date dt = sdf.parse(date_month_year);//"20-11-2012"
			
			SimpleDateFormat sdf6 = new SimpleDateFormat("y");//year
			String year=sdf6.format(dt);
			
			SimpleDateFormat sdf7 = new SimpleDateFormat("M");//month
			String month=sdf7.format(dt);

			SimpleDateFormat sdf8 = new SimpleDateFormat("d");//month
			String day=sdf8.format(dt);

			if( Integer.parseInt(month)== this.schedulingActivity.getCurrentMonth() ){
				//Toast.makeText(this._context, "Selected date: " + year+"/"+month+"/"+day, Toast.LENGTH_LONG).show();
				//this.calendarActivity.openOptionsDialog_selectConsultingHour();
				openOptionsDialog_selectConsultingHour();
				this.schedulingActivity.setSelectedDay(Integer.parseInt(day));
				this.schedulingActivity.setSelectedMonth(Integer.parseInt(month));
				this.schedulingActivity.setSelectedYear(Integer.parseInt(year));
			}else{
				Toast.makeText(context, "The date you select is not current month!!", Toast.LENGTH_LONG).show();
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getCurrentDayOfMonth() {
		return currentDayOfMonth;
	}
	
	public int getListCurrentPosition(){
		return idToChangeBackground;
	}
	/*
	public void setListContentByChoice(int position,boolean morn, boolean noon, boolean night){
		String[] day_color = list.get(position).split("-");
		String theday = day_color[0];
		//Strind dayColr = day_color[1];
		String themonth = day_color[2];
		String theyear = day_color[3];
		String idOfList = day_color[4];
		String tmp="";
		tmp=day_color[0]+"-"+day_color[1]+"-"+day_color[2]+"-"+day_color[3]+"-"+day_color[4];
		
		
		if(morn)
			tmp+="-true";
		else
			tmp+="-false";
		
		if(noon)
			tmp+="-true";
		else
			tmp+="-false";

		if(night)
			tmp+="-true";
		else
			tmp+="-false";
		
		list.set(position, tmp);
		
		//Toast.makeText(this._context, "List is: " + list.get(position), Toast.LENGTH_LONG).show();
	}
*/
}