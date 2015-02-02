package com.bt.medication_dao;

import java.util.ArrayList;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bt.healthrecordDAO.DBController;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.medication_model.MedicationSchedule;
import com.bt.medication_model.MedicationTaken;

public class MedicationTakenDao extends DBController {

    public MedicationTakenDao(Context applicationcontext) {
	super(applicationcontext);
	// TODO Auto-generated constructor stub
    }

    public static final String TABLE_NAME = "medication_taken";
    public static final String ID = "id";
    public static final String DATE_TAKEN = "date";
    public static final String TAKEN = "taken";
    public static final String ID_MEDICATION_HOUR_DOSAGE = "id_medication_hour_dosage";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY, "
	    + DATE_TAKEN + " DATETIME, " + TAKEN + " INTEGER , " + ID_MEDICATION_HOUR_DOSAGE + " INTEGER)";
    private SQLiteDatabase database;

    public void insertMedicationTaken(MedicationTaken medication_taken) {

	MedicationTaken medicationTakenUpdate = getMedicationTaken(medication_taken.getMedicationHourDosage(), medication_taken.getDate_taken());
	if (medicationTakenUpdate == null) {
	    SQLiteDatabase database = this.getWritableDatabase();
	    DateMidnight dd = medication_taken.getDate_taken().withZoneRetainFields(DateTimeZone.UTC);
	    long sqliteUnixEpoch = dd.getMillis() / 1000;
	    Integer taken = (medication_taken.getTaken() == true ? 1 : 0);

	    String query_insert = "INSERT INTO " + TABLE_NAME + " (" + DATE_TAKEN + ", " + TAKEN + ", "
		    + ID_MEDICATION_HOUR_DOSAGE + " ) VALUES ( datetime(" + sqliteUnixEpoch + ", 'unixepoch') , "
		    + taken + ", " + medication_taken.getMedicationHourDosage().getId() + ");";

	    database.execSQL(query_insert);

	    database.close();
	} else {
	    medication_taken.setId(medicationTakenUpdate.getId());
	    update(medication_taken);
	}

    }

    public int update(MedicationTaken medication_taken) {
	ContentValues values = new ContentValues();
	Integer taken = (medication_taken.getTaken() == true) ? 1 : 0;
	values.put(TAKEN, taken);
	return database.update(TABLE_NAME, values, ID + " = ?", new String[] { medication_taken.getId().toString() });
    }

    public void open() {
	database = getWritableDatabase();
    }

    public void close() {
	if (database != null) {
	    database.close();
	}
    }

    public static MedicationTaken cursorToMedicationTaken(Cursor cursor) {

	DateMidnight dateTaken = DateMidnight.parse(cursor.getString(1),
		DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
	dateTaken = dateTaken.withZoneRetainFields(DateTimeZone.UTC);
	Boolean isTaken = cursor.getInt(2) == 1 ? true : false;

	MedicationTaken medicationTaken = new MedicationTaken();
	medicationTaken.setId(cursor.getInt(0));
	medicationTaken.setTaken(isTaken);
	medicationTaken.setDate_taken(dateTaken);
	return medicationTaken;
    }

    public MedicationTaken getMedicationTaken(MedicationHourDosage medicationHourDosage, DateMidnight date) {

	String format_date = "yyyy-MM-dd HH:mm:ss";
	String date_formatted = date.toString(DateTimeFormat.forPattern(format_date));

	String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + ID_MEDICATION_HOUR_DOSAGE + "="
		+ medicationHourDosage.getId() + " AND " + DATE_TAKEN + "= '" + date_formatted + "'";
	Cursor cursor = database.rawQuery(selectQuery, null);

	MedicationTaken medicationTaken = null;
	if (cursor.moveToFirst()) {
	    medicationTaken = cursorToMedicationTaken(cursor);
	}
	return medicationTaken;

    }

}
