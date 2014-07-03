package edu.mcscheduling.model;


import android.content.ContentValues;

public class MsContentValues implements java.io.Serializable { 
	public int status;
	public ContentValues[] cv = null;
	
	public MsContentValues() {}	
	public MsContentValues(int status) {
		this.status = status;
	}
	public MsContentValues(ContentValues[] cv) {
		status = 0;
		this.cv = cv;
	}
}
