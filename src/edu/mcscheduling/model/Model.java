package edu.mcscheduling.model;

public class Model {
	private boolean checkParm(String parm) {
		if ( parm == null || parm.isEmpty() )
			return true;
		return false;
	}
	
	private boolean checkParm(boolean parm) {
		if ( parm == false )
			return true;
		return false;
	}
}
