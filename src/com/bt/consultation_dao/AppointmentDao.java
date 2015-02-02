package com.bt.consultation_dao;

import java.util.ArrayList;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bt.consultation_model.Appointment;
import com.bt.healthrecordDAO.DBController;
import com.bt.medication_model.MedicationSchedule;

public class AppointmentDao extends DBController {

    Context context;

    public AppointmentDao(Context applicationcontext) {
	super(applicationcontext);
	context = applicationcontext;
	// TODO Auto-generated constructor stub
    }

    public static final String TABLE_NAME = "appointment";
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String URI_CONTACT_DOCTOR = "uri_contact_doctor";
    public static final String COMMENT = "comment";
    public static final String SPECIALITY = "speciality";
    public static final String OTHER_SPECIALITY = "other_speciality";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY, "
	    + DATE + " DATETIME, " + URI_CONTACT_DOCTOR + " TEXT , " + COMMENT + " TEXT , " + SPECIALITY
	    + " INTEGER , " + OTHER_SPECIALITY + " TEXT )";
    private SQLiteDatabase database;

    public Appointment insertAppointment(Appointment appointment) {
	SQLiteDatabase database = this.getWritableDatabase();

	DateTime date_appointment = appointment.getDateAppointment().withZoneRetainFields(DateTimeZone.UTC);
	long sqliteUnixEpoch = date_appointment.getMillis() / 1000;

	int indice_speciality = (appointment.getSpeciality() == null) ? 0 : appointment.getSpeciality();
	String query_insert = "INSERT INTO " + TABLE_NAME + " (" + DATE + ", " + URI_CONTACT_DOCTOR + ", " + SPECIALITY
		+ ", " + OTHER_SPECIALITY + ", " + COMMENT + " ) VALUES ( datetime(" + sqliteUnixEpoch
		+ ", 'unixepoch') , '" + appointment.getUriContactDdoctor() + "', " + indice_speciality + " , '"
		+ appointment.getOtherSpecialtity() + "' , '" + appointment.getComment() + "')";

	Log.d("domi", query_insert);
	database.execSQL(query_insert);

	long lastId = getHighestID();
	appointment.setId((int) lastId);

	return appointment;
    }

    public void update(Appointment appointment) {
	DateTime date_appointment = appointment.getDateAppointment().withZoneRetainFields(DateTimeZone.UTC);
	long sqliteUnixEpoch = date_appointment.getMillis() / 1000;
	int indice_speciality = (appointment.getSpeciality() == null) ? 0 : appointment.getSpeciality();
	String query_update = "UPDATE " + TABLE_NAME + " SET " + DATE + " = datetime(" + sqliteUnixEpoch
		+ ", 'unixepoch') , " + URI_CONTACT_DOCTOR + " =  '" + appointment.getUriContactDdoctor() + "' , "
		+ SPECIALITY + " = " + indice_speciality + " , " + OTHER_SPECIALITY + " = '"
		+ appointment.getOtherSpecialtity() + "' , " + COMMENT + " = '" + appointment.getComment() + "' "
		+ " WHERE ID = " + appointment.getId();

	database.execSQL(query_update);
    }

    public void delete(Appointment appointment) {
	String deleteQuery = "DELETE FROM " + TABLE_NAME + " where " + ID + "=" + appointment.getId();
	database.execSQL(deleteQuery);
    }

    public ArrayList<Appointment> getAllAppointmentByDay(DateTime date_appointment) {
	ArrayList<Appointment> list = new ArrayList<Appointment>();
	String format_date = "yyyy-MM-dd";
	String date_compare = date_appointment.toString(DateTimeFormat.forPattern(format_date));

	String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE date(date) =  '" + date_compare + "'"
		+ " ORDER BY date ASC";
	Cursor cursor = database.rawQuery(selectQuery, null);

	if (cursor.moveToFirst()) {
	    do {
		Appointment appointment = cursorToAppointment(cursor);

		list.add(appointment);
	    } while (cursor.moveToNext());
	}

	return list;

    }

    public ArrayList<Appointment> getAllAppointmentByMonth(int month, int year) {
	ArrayList<Appointment> list = new ArrayList<Appointment>();

	String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE   strftime('%m', " + DATE + ") = '"
		+ String.format("%02d", month) + "'" + " AND strftime('%Y', " + DATE + ") = '" + year + "'"
		+ " ORDER BY date ASC";
	Cursor cursor = database.rawQuery(selectQuery, null);

	if (cursor.moveToFirst()) {
	    do {
		Appointment appointment = cursorToAppointment(cursor);

		list.add(appointment);
	    } while (cursor.moveToNext());
	}

	return list;

    }
    
    public Appointment getNextAppointment() {
	DateTime today = (new DateTime()).withZoneRetainFields(DateTimeZone.UTC);
	long sqliteUnixEpoch = today.getMillis() / 1000;

	Appointment appointment = null;
	String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE date >  datetime (" + sqliteUnixEpoch+ ",'unixepoch') "
		+ " ORDER BY date ASC"
		+ " LIMIT 1";
	Cursor cursor = database.rawQuery(selectQuery, null);

	if (cursor.moveToFirst()) {
	    do {
		appointment = cursorToAppointment(cursor);
	    } while (cursor.moveToNext());
	}

	return appointment;

    }    

    public Appointment cursorToAppointment(Cursor cursor) {

	DateTime date_appointment = DateTime.parse(cursor.getString(1),
		DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
	date_appointment = date_appointment.withZoneRetainFields(DateTimeZone.UTC);

	return new Appointment(cursor.getInt(0), cursor.getString(2), cursor.getInt(4), cursor.getString(5),
		date_appointment, cursor.getString(3));
    }

    public int getHighestID() {
	final String MY_QUERY = "SELECT MAX(id) FROM " + AppointmentDao.TABLE_NAME;
	Cursor cur = database.rawQuery(MY_QUERY, null);
	cur.moveToFirst();
	int ID = cur.getInt(0);
	cur.close();
	return ID;
    }

    public void open() {
	database = getWritableDatabase();
    }

    public void close() {
	if (database != null) {
	    database.close();
	}
    }

}
