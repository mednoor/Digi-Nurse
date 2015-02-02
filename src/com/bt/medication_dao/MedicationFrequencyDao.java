package com.bt.medication_dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bt.healthrecordDAO.DBController;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationFrequency;
import com.bt.medication_model.MedicationSchedule;

public class MedicationFrequencyDao extends DBController {

    public MedicationFrequencyDao(Context applicationcontext) {
	super(applicationcontext);
	// TODO Auto-generated constructor stub
    }

    public static final String TABLE_NAME = "medication_frequency";
    public static final String ID = "id";
    public static final String IS_EVERY_DAY = "is_every_day";
    public static final String DAYS_WEEK = "days_week";
    public static final String INTERVAL_DAY = "interval_day";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY, "
	    + IS_EVERY_DAY + " TEXT, " + DAYS_WEEK + " TEXT , " + INTERVAL_DAY + " INTEGER )";
    private SQLiteDatabase database;

    public MedicationFrequency insertMedicationFrequency(MedicationFrequency medication_frequency) {
	SQLiteDatabase database = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(IS_EVERY_DAY, medication_frequency.getIs_every_day());
	values.put(DAYS_WEEK, medication_frequency.getDays_week());
	values.put(INTERVAL_DAY, medication_frequency.getInterval_day());

	long id = database.insert(TABLE_NAME, null, values);
	medication_frequency.setId((int) id);
	database.close();
	return medication_frequency;
    }

    public int update(MedicationFrequency medication_frequency) {
	ContentValues values = new ContentValues();
	values.put(IS_EVERY_DAY, medication_frequency.getIs_every_day());
	values.put(DAYS_WEEK, medication_frequency.getDays_week());
	values.put(INTERVAL_DAY, medication_frequency.getInterval_day());
	return database.update(TABLE_NAME, values, ID + " = ?",
		new String[] { medication_frequency.getId().toString() });
    }

    public void open() {
	database = getWritableDatabase();
    }

    public void close() {
	if (database != null) {
	    database.close();
	}
    }
    
    public static MedicationFrequency cursorToMedicationFrequency(Cursor cursor) {
 	Boolean is_every_day = cursor.getInt(13) == 1 ? true : false;
 	return new MedicationFrequency(cursor.getInt(12), is_every_day, cursor.getString(14), cursor.getInt(15));
     }

}
