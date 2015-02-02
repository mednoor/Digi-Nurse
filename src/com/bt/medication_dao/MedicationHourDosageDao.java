package com.bt.medication_dao;

import java.util.ArrayList;

import org.joda.time.LocalTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bt.healthrecordDAO.DBController;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationFrequency;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.medication_model.MedicationSchedule;

public class MedicationHourDosageDao extends DBController {

    public MedicationHourDosageDao(Context applicationcontext) {
	super(applicationcontext);
	// TODO Auto-generated constructor stub
    }

    public static final String TABLE_NAME = "medication_hour_dosage";
    public static final String ID = "id";
    public static final String HOUR = "hour";
    public static final String QUANTTITY = "quantity";
    public static final String ID_SCHEDULE_MEDICATION = "id_schedule_medication";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY, "
	    + HOUR + " INTEGER, " + QUANTTITY + " INTEGER , " 
	    + ID_SCHEDULE_MEDICATION + " INTEGER )";
    private SQLiteDatabase database;

    public MedicationHourDosage insertMedicationHourDosage(MedicationHourDosage medication_hour_dosage) {
	SQLiteDatabase database = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(HOUR, medication_hour_dosage.getMinutesFromHour());
	values.put(QUANTTITY, medication_hour_dosage.getQuantity());
	values.put(ID_SCHEDULE_MEDICATION, medication_hour_dosage.getMedication_schedule().getId());

	long id = database.insert(TABLE_NAME, null, values);
	medication_hour_dosage.setId((int) id);
	database.close();
	return medication_hour_dosage;
    }
    
    public void delete(Integer id_medication_schedule) {
	String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_SCHEDULE_MEDICATION + "="
		+ id_medication_schedule;
	Log.d("query", deleteQuery);
	database.execSQL(deleteQuery);
    }
    
  

    public ArrayList<MedicationHourDosage> getListMedicationHourDosage(MedicationSchedule schedule_medication) {
	String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + ID_SCHEDULE_MEDICATION + "="
		+ schedule_medication.getId()+" ORDER BY "+HOUR+"";
	Cursor cursor = database.rawQuery(selectQuery, null);
	ArrayList<MedicationHourDosage> listHourDosage = new ArrayList<MedicationHourDosage>();
	if (cursor.moveToFirst()) {
	    do {
		MedicationHourDosage medicationHourDosage = cursorToMedicationHourDosage(cursor);
		medicationHourDosage.setMedication_schedule(schedule_medication);
		listHourDosage.add(medicationHourDosage);
	    } while (cursor.moveToNext());
	}
	return listHourDosage;
    }

    public MedicationHourDosage cursorToMedicationHourDosage(Cursor cursor) {
	Integer hourInMinutes = cursor.getInt(1);
	LocalTime hour = new LocalTime(hourInMinutes/60,hourInMinutes%60);
	return new MedicationHourDosage(cursor.getInt(0), hour, cursor.getDouble(2) );
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
