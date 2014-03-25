package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;



public class UtilitySpinnerOnItemSelectedListener implements OnItemSelectedListener {
	
    private String selectedPasswordTip = null;
    private String selectedWorkPattern = null;
    
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        
        String selectedItem = parent.getItemAtPosition(pos).toString();
        
        //check which spinner triggered the listener
        switch (parent.getId()) {
 
	        case R.id.Spinner_MemberInformationPage_passwordTip:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(selectedPasswordTip != null){
	                   Toast.makeText(parent.getContext(), "你選擇的密碼提問是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	               selectedPasswordTip = selectedItem;
	            
	            break;
            
	        case R.id.Spinner_MemberInformationPage_workPattern:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(selectedWorkPattern != null){
	                   Toast.makeText(parent.getContext(), "你選擇的工作型態是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   selectedWorkPattern = selectedItem;
	            
	            break;
        }

        
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }
}
