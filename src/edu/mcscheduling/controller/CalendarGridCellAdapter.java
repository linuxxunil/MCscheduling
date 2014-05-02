package edu.mcscheduling.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import edu.mcscheduling.R;

/**
 * DoctorSchedulingCalendarActivity <-Set-> HashTable <-Get-> CalendarGridCellAdapter 
 * 
 * HashTable Content has
 * <Year,"2014" >
 * <Month,"5" >
 * 
 * <0,"true-true,true"> // <day,"morning-noon-night" 
 * ...
 * <30,"true-false-true>
 * <31,"true-false-true">
 * @author jesse
 *
 */


public class CalendarGridCellAdapter extends BaseAdapter implements OnClickListener {	
	private static final int DAY_OFFSET = 1;
	
	
	private final String[] months = { "01", "02", "03", "04", 
									  "05", "06", "07", "08", 
									  "09", "10", "11", "12" };

	private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	
	private Button gridcell;
	private TextView num_events_per_day;


	private Activity schedulingActivity;
	
	private int daysInMonth;
	private int currentDayOfMonth;

	private int monthStartID = -1;
	private int monthStopID = -1;
	private final int totalGrid = 7*6;
	private final Context context;
	private final List<String> buttonInfoList;
	private final HashMap<String, Integer> eventsPerMonthMap;
	private HashMap<String, String> monthInfo;
	
	private void initButtonInfoList() {
		for (int i=0; i<totalGrid; i++) 
				buttonInfoList.add("");
	}
	
	// Days in Current Month
	public CalendarGridCellAdapter(	Activity activity,
									Context context, 
									int textViewResourceId,
									HashMap<String,String> monthInfo) {
		super();
		
		schedulingActivity = activity;
		this.context = context;
		this.monthInfo = monthInfo;
		
		buttonInfoList = new ArrayList<String>();
		initButtonInfoList();
		
		/**
		 *  設定每個日期按鍵上的value
		 */
		
		updateMonthInfoToButton (Integer.valueOf(monthInfo.get("year")),
							  Integer.valueOf(monthInfo.get("month")));
		
		// Find Number of Events
		eventsPerMonthMap = findNumberOfEventsPerMonth();

	}

	public void updateMonthInfoToButton (int yy, int mm) {
		int trailingSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;
		int currentMonth = mm - 1;
		
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
		daysInMonth = getNumberOfDaysOfMonth(currentMonth);
		// 取得當月第一天是星期幾
		int firstDayOfWeek = gregCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		
		monthStartID = trailingSpaces = firstDayOfWeek;

		monthStopID = monthStartID + daysInMonth;

		// 設定閏年
		if (gregCalendar.isLeapYear(gregCalendar.get(Calendar.YEAR)))
			if (mm == 2)
				++daysInMonth;
			else if (mm == 3)
				++daysInPrevMonth;

		int id = 0;
		// Trailing Month days
		for (int i = 0; i < trailingSpaces; i++) {
			buttonInfoList.set(id, String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)+ i)	 
					+ "-GREY"
					+ "-" + prevYear
					+ "-" + getMonthAsString(prevMonth)
					+ "-" + id 
					+ "-" + "false" 
					+ "-" + "false" 
					+ "-" + "false");
			id++;
		}

		// Current Month Days
		for (int i = 1; i <= daysInMonth; i++) {
				String selected = monthInfo.get(String.valueOf(i-1));
				
				if ( selected == null ) {
					buttonInfoList.set(id, String.valueOf(i) 
						+ "-GREY"
						+ "-" + yy  
						+ "-" + getMonthAsString(currentMonth)
						+ "-" + id 
						+ "-" + "false" 
						+ "-" + "false"  
						+ "-" + "false" );
				} else {
					String[] checked = selected.split("-");
					
					if ( checked[0].equals("null") ) {
						buttonInfoList.set(id, String.valueOf(i) 
								+ "-GREY"
								+ "-" + yy  
								+ "-" + getMonthAsString(currentMonth)
								+ "-" + id 
								+ "-" + "false" 
								+ "-" + "false"
								+ "-" + "false");
					} else {
						buttonInfoList.set(id, String.valueOf(i) 
								+ "-WHITE"
								+ "-" + yy  
								+ "-" + getMonthAsString(currentMonth)
								+ "-" + id 
								+ "-" + checked[0] 
								+ "-" + checked[1] 
								+ "-" + checked[2]);
					}
				}
			id++;
		}
		
		// Leading Month days
		for (int i = 1; id<totalGrid ; i++) {
			buttonInfoList.set(id, String.valueOf(i) 
					+ "-GREY" 
					+ "-" + getMonthAsString(nextMonth) 
					+ "-" + nextYear 
					+ "-" + id 
					+ "-" + "false" 
					+ "-" + "false" 
					+ "-" + "false");
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
	private HashMap<String, Integer> findNumberOfEventsPerMonth() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		return map;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private View refreshMonthInfoOnButton(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.screen_gridcell, parent, false);
		}

		// Get a reference to the Day gridcell
		gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
		gridcell.setOnClickListener(this);
		
		// 擷取
		String[] buttonInfo = buttonInfoList.get(position).split("-");
		
		if ( buttonInfo == null || buttonInfo[0].equals(""))
			return row;
		
		String day 		= buttonInfo[0];
		//Strind dayColr = day_color[1];
		String year 	= buttonInfo[2];
		String month 	= buttonInfo[3];
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
			gridcell.setTextColor(context.getResources().getColor(R.color.lightgray));
		} else if (buttonInfo[1].equals("WHITE")) {
			gridcell.setTextColor(context.getResources().getColor(R.color.lightgray02));
		} else  { 
			gridcell.setTextColor(context.getResources().getColor(R.color.lightgray02));
		}
		
		
		// 設定按鈕顏色
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
	
		return row;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return refreshMonthInfoOnButton(position, convertView, parent);
	}

	public void openOptionsDialog_selectConsultingHour(final int gridID)  
	{
    	AlertDialog.Builder builder = new AlertDialog.Builder(schedulingActivity);
    	builder.setTitle("選擇看診時間");
    	final String[] workTimeDefine = {"早上","下午","晚上"};
    	final String day = String.valueOf(gridID - monthStartID);
    	final CharSequence[] session = new CharSequence[3];
    	// 取得星期幾
    	final int dayOfWeek = (gridID - 1) % 7; 
    	final String[] workTime = monthInfo.get("week"+String.valueOf(dayOfWeek)).split("-");
    	String consulting = monthInfo.get(day);
    	final String[] info;
    	final boolean[] checked = new boolean[3];
    	AlertDialog dialog = null;
 
    	if ( consulting == null ) {
    		info = new String[3];
    		info[0] = info[1] = info[2] = "false";
    	} else {
    		info = consulting.split("-");
    	}
    		
    	for ( int i=0; i<3; i++ ) {
    		if ( workTime[i].equals("true")) {
    			session[i] = workTimeDefine[i];
    			checked[i] = (info[i] == null || info[i].equals("false")) ? false:true;
    		} else {
    			session[i] = workTimeDefine[i]+"休診";
    			checked[i] = false;
    		}
    	}
    	
    	builder.setMultiChoiceItems(session, 
    			null, 
    			new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if ( session[which].equals(workTimeDefine[which]+"休診") ) {
					isChecked = false; 
					((AlertDialog) dialog).getListView().setItemChecked(which, isChecked);
				}
				
				String consulting = monthInfo.get(day);
            	String[] info;
            	if ( consulting == null ) {
            		info = new String[3];
            		info[0] = info[1] = info[2] = "false";
            	} else {
            		info = consulting.split("-");
            	}
            	
			    boolean morningChecked = (info[0] == null || info[0].equals("false")) ? false:true;
			    boolean moonChecked    = (info[1] == null || info[1].equals("false")) ? false:true;
			    boolean nightChecked   = (info[2] == null || info[2].equals("false")) ? false:true;
				 
				String status = "";		
				if ( which == 0 )  
					status = isChecked + "-" + moonChecked + "-" + nightChecked;
				else if ( which == 1 ) 
					status = morningChecked + "-" + isChecked + "-" + nightChecked;
				else 
					status = morningChecked + "-" + moonChecked + "-" + isChecked;
			
				monthInfo.put(day, status);
			}
		});
    	
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String consulting = monthInfo.get(day);
            	String[] info;
            	if ( consulting == null ) {
            		info = new String[3];
            		info[0] = info[1] = info[2] = "false";
            	} else {
            		info = consulting.split("-");
            	}
            	
		    	boolean morningChecked = (info[0] == null || info[0].equals("false")) ? false:true;
		    	boolean moonChecked    = (info[1] == null || info[1].equals("false")) ? false:true;
		    	boolean nightChecked   = (info[2] == null || info[2].equals("false")) ? false:true;
            	
		    	setListContentByChoice(gridID,morningChecked,moonChecked, nightChecked);                
                notifyDataSetChanged();
           }
        });
    	
    	dialog = builder.show();
    	// Initial checked value
    	for ( int i=0; i<3; i++ )
    		dialog.getListView().setItemChecked(i, checked[i]);
    }	
	
	@Override
	public void onClick(View view) {
		String viewTag = (String) view.getTag();
		
		if (viewTag == null) {
			return ;
		}
		
		int id  = Integer.valueOf(viewTag.split("-")[3]);

		if (( id < monthStartID) || id >= monthStopID) {
			
		} else {
			//String status = monthInfo.get(id - monthStartID);
				openOptionsDialog_selectConsultingHour(id);
		}
	}
	
	public void setListContentByChoice(int position,boolean morn, boolean noon, boolean night){
		String[] buttonInfo = buttonInfoList.get(position).split("-");
		String tmp = buttonInfo[0]+"-"+buttonInfo[1]+"-"+buttonInfo[2]+"-"+buttonInfo[3]+"-"+buttonInfo[4];
		tmp += "-" + morn + "-" + noon + "-" + night;
		buttonInfoList.set(position, tmp);
	}

	public int getCurrentDayOfMonth() {
		return currentDayOfMonth;
	}
	
	private String getMonthAsString(int i) {
		return months[i];
	}

	public int getNumberOfDaysOfMonth(int i) {
		return daysOfMonth[i];
	}

	@Override
	public String getItem(int position) {
		return buttonInfoList.get(position);
	}
	
	@Override	
	public int getCount() {
		return buttonInfoList.size();
	}
	
}