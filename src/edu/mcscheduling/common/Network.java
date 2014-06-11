package edu.mcscheduling.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
	static private Context _context = null;
	
	static public void setContext(Context context) {
		_context = context;
	}
	
	static public boolean isNetworkAvailable() {
		if ( _context == null ) {
			StatusCode.ERR_NETWORK_DONT_SET_CONTEXT();
			return false ;
		}
		ConnectivityManager connectivityManager 
        		= (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
  		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
