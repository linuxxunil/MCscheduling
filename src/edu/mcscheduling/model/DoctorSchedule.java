package edu.mcscheduling.model;

public class DoctorSchedule {
	private DatabaseDriver db;
	private boolean exist = false;
	
	public DoctorSchedule(DatabaseDriver db) {
		if ( db != null ) {
			this.db = db;
			exist = true;
		} 
	}
}
