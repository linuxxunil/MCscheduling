package edu.mcscheduling.model;

import java.sql.SQLException;

import edu.mcscheduling.database.SqliteDriver;

import android.content.ContentValues;
import android.database.Cursor;

public class TestDB {
	private static final String dbPath = "/sdcard/data/cscheduling/cscheduling.db";
	private static SqliteDriver db;

	public TestDB() {

		try {

			db = new SqliteDriver(dbPath);
			db.onConnect();
			db.createTable(DatabaseTable.Hospital.create());
			db.createTable(DatabaseTable.CodeFile.create());
			db.createTable(DatabaseTable.Doctor.create());
			db.createTable(DatabaseTable.Department.create());
			db.createTable(DatabaseTable.DoctorSchedule.create());
			db.createTable(DatabaseTable.User.create());
			ContentValues[] content = null;
			/*
			Account account = new Account(db);
			System.out.println("Register: " + account.register("a@a.com", "jesse", "1qazxcvb"));
			System.out.println("Login: "+ account.login("a@a.com", "1qazxcvb"));
			System.out.println("ChangePasswd: " +account.changePasswd("a@a.com","1qazxcvb", "1qazxcvb2"));
			System.out.println("Login: " + account.login("a@a.com", "1qazxcvb"));
			
			
			Hospital hospital = new Hospital(db);
			System.out.println(hospital.setHospital("a@a.com", "hospitalName", 
					"areaID", "0929238459", "hospitalAddress", "contactName", 
					"contactPhone", "depName", "opdSt1", "opdEt1", "opdSt2", "opdEt2", 
					"opdSt3", "opdEt3", "hispitalSchedule", "1", "picPath"));
			System.out.println(hospital.setHospital("b@a.com", "hospitalName", 
					"areaID", "0929238459", "hospitalAddress", "contactName", 
					"contactPhone", "depName", "opdSt1", "opdEt1", "opdSt2", "opdEt2", 
					"opdSt3", "opdEt3", "hispitalSchedule", "1", "picPath"));
			
	 		content = hospital.getHospital("a@a.com");
			
			if ( content != null ) {
				System.out.println("colHospitalNo: "+content[0].get(DatabaseTable.Hospital.colHospitalNo));
				System.out.println("colHospitalName: "+content[0].get(DatabaseTable.Hospital.colHospitalName));
				System.out.println("colAreaID: "+content[0].get(DatabaseTable.Hospital.colAreaID));
				System.out.println("colHospitalPhone: "+content[0].get(DatabaseTable.Hospital.colHospitalPhone));
				System.out.println("colHospitalAddress: "+content[0].get(DatabaseTable.Hospital.colHospitalAddress));
				System.out.println("colContactName: "+content[0].get(DatabaseTable.Hospital.colContactName));
				System.out.println("colContactPhone: "+content[0].get(DatabaseTable.Hospital.colContactPhone));
				System.out.println("colUpdateID: "+content[0].get(DatabaseTable.Hospital.colUpdateID));
				System.out.println("colUpdateDate: "+content[0].get(DatabaseTable.Hospital.colUpdateDate));
				System.out.println("colDesc: "+content[0].get(DatabaseTable.Hospital.colDesc));
				System.out.println("colDepName: "+content[0].get(DatabaseTable.Hospital.colDepName));
				System.out.println("colOPD_ST1: "+content[0].get(DatabaseTable.Hospital.colOPD_ST1));
				System.out.println("colOPD_ET1: "+content[0].get(DatabaseTable.Hospital.colOPD_ET1));
				System.out.println("colOPD_ST2: "+content[0].get(DatabaseTable.Hospital.colOPD_ST2));
				System.out.println("colOPD_ET2: "+content[0].get(DatabaseTable.Hospital.colOPD_ET2));
				System.out.println("colOPD_ST3: "+content[0].get(DatabaseTable.Hospital.colOPD_ST3));
				System.out.println("colOPD_ET3: "+content[0].get(DatabaseTable.Hospital.colOPD_ET3));
				System.out.println("colHospitalschedule: "+content[0].get(DatabaseTable.Hospital.colHospitalschedule));
				System.out.println("colhospitalstate: "+content[0].get(DatabaseTable.Hospital.colhospitalstate));
				System.out.println("colPicPath: "+content[0].get(DatabaseTable.Hospital.colPicPath));
			}
			
			
			Department depart = new Department(db);
			System.out.println(depart.setDepartment("a@a.com","bbb,ccc","NULL"));
			
			content = depart.getDepartment("a@a.com");
			
			if ( content != null ) {
				for (int i=0; i<content.length; i++ ) {
					System.out.println("colHospitalNo: "+content[i].get(DatabaseTable.Department.colHospitalNo));
					System.out.println("colUpdateID: "+content[i].get(DatabaseTable.Department.colUpdateID));
					System.out.println("colDepName: "+content[i].get(DatabaseTable.Department.colDepName));
					System.out.println("colUpdateDate: "+content[i].get(DatabaseTable.Department.colUpdateDate));
					System.out.println("colDesc: "+content[i].get(DatabaseTable.Department.colDesc));
				}
			}
			
			*/
			/*
			Doctor doctor = new Doctor(db);
			doctor.deleteDoctorAll("a@a.com");
			
			System.out.println(doctor.addDoctor("a@a.com", "depName1", "dorName1", "telephone", 
					"jobTitle", "history", "subject", "desc", "picPath"));
			
			System.out.println(doctor.addDoctor("a@a.com", "depName1", "dorName2", "telephone", 
					"jobTitle", "history", "subject", "desc", "picPath"));
			
			System.out.println(doctor.addDoctor("b@a.com", "depName3", "dorName3", "telephone", 
					"jobTitle", "history", "subject", "desc", "picPath"));
			
			System.out.println(doctor.updateDoctor("a@a.com", "00001", "depNameQQ", "dorName", "telephone",
					"jobTitle", "history", "subject", "desc", "picPath"));
			
			System.out.println(doctor.deleteDoctor("a@a.com", "00002"));
			
			System.out.println(doctor.addDoctor("a@a.com", "depName1", "dorName2", "telephone", 
					"jobTitle", "history", "subject", "desc", "picPath"));
			
			content = doctor.getDoctor("a@a.com");
			
			if ( content != null ) {
				for ( int i=0; i<content.length; i++ ) {
					System.out.println("colHospitalNo: "+content[i].get(DatabaseTable.Doctor.colHospitalNo));
					System.out.println("colDepName: "+content[i].get(DatabaseTable.Doctor.colDepName));
					System.out.println("colDorNo: "+content[i].get(DatabaseTable.Doctor.colDorNo));
					System.out.println("colDorName: "+content[i].get(DatabaseTable.Doctor.colDorName));
					System.out.println("colTelephone: "+content[i].get(DatabaseTable.Doctor.colTelephone));
					System.out.println("colJobTitle: "+content[i].get(DatabaseTable.Doctor.colJobTitle));
					System.out.println("colHistory: "+content[i].get(DatabaseTable.Doctor.colHistory));
					System.out.println("colSubject: "+content[i].get(DatabaseTable.Doctor.colSubject));
					System.out.println("colValid: "+content[i].get(DatabaseTable.Doctor.colValid));
					System.out.println("colUpdateID: "+content[i].get(DatabaseTable.Doctor.colUpdateID));
					System.out.println("colUpdateDate: "+content[i].get(DatabaseTable.Doctor.colUpdateDate));
					System.out.println("colDesc: "+content[i].get(DatabaseTable.Doctor.colDesc));
					System.out.println("colPicPath: "+content[i].get(DatabaseTable.Doctor.colPicPath));
				}
			}
			*/
			
			/*
			DoctorSchedule doctorSchedule = new DoctorSchedule(db);
			
			doctorSchedule.deleteDoctorScheduleAll("");
			
			System.out.println(doctorSchedule.addDoctorSchedule("a@a.com", "depName1", "schYear", "schMonth", "schedule", "desc"));
			
			System.out.println(doctorSchedule.addDoctorSchedule("a@a.com", "depName2", "schYear", "schMonth", "schedule", "desc"));
			
			System.out.println(doctorSchedule.addDoctorSchedule("b@a.com", "depName3", "schYear", "schMonth", "schedule", "desc"));
			
			System.out.println(doctorSchedule.updateDoctorSchedule("a@a.com", "00001", "depNameQQ", "schYear", "schMonth", "schedule", "desc"));
			
			System.out.println(doctorSchedule.deleteDoctorSchedule("a@a.com", "00002"));
			
			System.out.println(doctorSchedule.addDoctorSchedule("a@a.com", "depName1", "schYear", "schMonth", "schedule", "desc"));
			
			content = doctorSchedule.getDoctorSchedule("a@a.com");
			if ( content != null ) {
				for ( int i=0; i< content.length; i++ ) {
					System.out.println("colHospitalNo: "+content[i].get(DatabaseTable.DoctorSchedule.colHospitalNo));
					System.out.println("colUpdateID: "+content[i].get(DatabaseTable.DoctorSchedule.colUpdateID));
					System.out.println("colDorNo: "+content[i].get(DatabaseTable.DoctorSchedule.colDorNo));
					System.out.println("colSchMonth: "+content[i].get(DatabaseTable.DoctorSchedule.colSchMonth));
					System.out.println("colSchYear: "+content[i].get(DatabaseTable.DoctorSchedule.colSchYear));
					System.out.println("colUpdateID: "+content[i].get(DatabaseTable.DoctorSchedule.colUpdateID));
					System.out.println("colUpdateDate: "+content[i].get(DatabaseTable.DoctorSchedule.colUpdateDate));
					System.out.println("colDesc: "+content[i].get(DatabaseTable.DoctorSchedule.colDesc));
				}
			}
			*/
			
		} catch (Exception e) {

			 System.out.println(e.getMessage() );
		}

	}

}
