package edu.mcscheduling.controller;

import edu.mcscheduling.R;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;



public class UtilitySpinnerOnItemSelectedListener implements OnItemSelectedListener {
	
    protected String memberInformationPage_selectedPasswordTip = null;
    protected String memberInformationPage_selectedWorkPattern = null;
    protected String hospitalInformationPage_selectedMedicalGroup = null;
    protected String hospitalInformationPage_selectedMornStartTime = null;
    protected String hospitalInformationPage_selectedMornEndTime = null;
    protected String hospitalInformationPage_selectedNoonStartTime = null;
    protected String hospitalInformationPage_selectedNoonEndTime = null;  
    protected String hospitalInformationPage_selectedNightStartTime = null;
    protected String hospitalInformationPage_selectedNightEndTime = null;  
    protected String newDoctorInformationPage_selectedMedicalDepartment = null;  
    protected String schedulePage_medicalDepartment = null;  
    protected String schedulePage_doctorName = null;  
    protected String schedulePage_week = null;  
    protected String DoctorSchedulingCalendarPage_selectedMedicalDepartment = null;  
    protected String DoctorSchedulingCalendarPage_doctorName = null;  

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        
        String selectedItem = parent.getItemAtPosition(pos).toString();
        
        //check which spinner triggered the listener
        switch (parent.getId()) {
 
	        case R.id.Spinner_MemberInformationPage_passwordTip:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(memberInformationPage_selectedPasswordTip != null){
	                   Toast.makeText(parent.getContext(), "你選擇的密碼提問是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	               memberInformationPage_selectedPasswordTip = selectedItem;
	            
	            break;
            
	        case R.id.Spinner_MemberInformationPage_workPattern:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(memberInformationPage_selectedWorkPattern != null){
	                   Toast.makeText(parent.getContext(), "你選擇的工作型態是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   memberInformationPage_selectedWorkPattern = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_medicalGroup:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedMedicalGroup != null){
	                   Toast.makeText(parent.getContext(), "你選擇的醫療群是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedMedicalGroup = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_mornStartTime:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedMornStartTime != null){
	                   Toast.makeText(parent.getContext(), "你選擇的mornStart時間是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedMornStartTime = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_mornEndTime:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedMornEndTime != null){
	                   Toast.makeText(parent.getContext(), "你選擇的mornEnd時間是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedMornEndTime = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_noonStartTime:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedNoonStartTime != null){
	                   Toast.makeText(parent.getContext(), "你選擇的noonStart時間是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedNoonStartTime = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_noonEndTime:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedNoonEndTime != null){
	                   Toast.makeText(parent.getContext(), "你選擇的noonEnd時間是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedNoonEndTime = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_nightStartTime:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedNightStartTime != null){
	                   Toast.makeText(parent.getContext(), "你選擇的nightStart時間是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedNightStartTime = selectedItem;
	            
	            break;
	        case R.id.Spinner_HospitalInformationPage_nightEndTime:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(hospitalInformationPage_selectedNightEndTime != null){
	                   Toast.makeText(parent.getContext(), "你選擇的nightEnd時間是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   hospitalInformationPage_selectedNightEndTime = selectedItem;
	            
	            break;
	        case R.id.Spinner_newDoctorInformationPage_medicalDepartment:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(newDoctorInformationPage_selectedMedicalDepartment != null){
	                   Toast.makeText(parent.getContext(), "你選擇的科別是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   newDoctorInformationPage_selectedMedicalDepartment = selectedItem;
	            
	            break;
	        case R.id.Spinner_SchedulePage_medicalDepartment:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(schedulePage_medicalDepartment != null){
	                   Toast.makeText(parent.getContext(), "你選擇的科別是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   schedulePage_medicalDepartment = selectedItem;
	            
	            break;	   
	        case R.id.Spinner_SchedulePage_doctorName:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(schedulePage_doctorName != null){
	                   Toast.makeText(parent.getContext(), "你選擇的醫生是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   schedulePage_doctorName = selectedItem;
	            
	            break;	 	            
	        case R.id.Spinner_SchedulePage_week:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(schedulePage_week != null){
	                   Toast.makeText(parent.getContext(), "你選擇的週數是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   schedulePage_week = selectedItem;
	            
	            break;	 	
	        case R.id.Spinner_DoctorSchedulingCalendarPage_medicalDepartment:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(DoctorSchedulingCalendarPage_selectedMedicalDepartment != null){
	                   Toast.makeText(parent.getContext(), "你選擇的科別是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   DoctorSchedulingCalendarPage_selectedMedicalDepartment = selectedItem;
	            
	            break;	 
	        case R.id.Spinner_DoctorSchedulingCalendarPage_doctorName:
	            //make sure the country was already selected during the onCreate
	 
	        	   if(DoctorSchedulingCalendarPage_doctorName != null){
	                   Toast.makeText(parent.getContext(), "你選擇的醫生是: " + selectedItem,
	                   Toast.LENGTH_LONG).show();
	               }
	        	   DoctorSchedulingCalendarPage_doctorName = selectedItem;
	            
	            break;	 
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }
}
