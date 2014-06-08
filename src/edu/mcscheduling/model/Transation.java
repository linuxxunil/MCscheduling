package edu.mcscheduling.model;

import java.sql.SQLException;


public interface Transation {
	Object execute() throws SQLException;
}
