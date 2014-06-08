package edu.mcscheduling.database;

import java.sql.SQLException;


public interface Transation {
	Object execute() throws SQLException;
}
