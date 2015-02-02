package com.bt.medication_dao;

import java.util.ArrayList;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bt.healthrecordDAO.DBController;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationFrequency;
import com.bt.medication_model.MedicationSchedule;

public class MedicationDao extends DBController {

    Context context;

    public MedicationDao(Context applicationcontext) {
	super(applicationcontext);
	context = applicationcontext;
	// TODO Auto-generated constructor stub
    }

    public static final String TABLE_NAME = "medication";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PICTURE = "picture";
    public static final String INSCTRUCTION_COMMENT = "insctruction_comment";
    public static final String FOOD_INSCTUCTION = "food_insctruction";
    public static final String DELETED = "deleted";
    public static final String ID_MEDICATION_SCHEDULE = "id_medication_schedule";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY, "
	    + NAME + " TEXT, " + PICTURE + " TEXT , " + INSCTRUCTION_COMMENT + " TEXT , " + FOOD_INSCTUCTION
	    + " TEXT , " + ID_MEDICATION_SCHEDULE + " INTEGER , " + DELETED + " INTEGER)";
    private SQLiteDatabase database;

    public Medication insertMedication(Medication medication) {
	SQLiteDatabase database = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(NAME, medication.getName());
	values.put(PICTURE, medication.getPicture());
	values.put(INSCTRUCTION_COMMENT, medication.getInstruction_comment());
	values.put(FOOD_INSCTUCTION, medication.getFood_instruction());
	values.put(DELETED, medication.getDeleted());
	values.put(ID_MEDICATION_SCHEDULE, medication.getSchedule_medication().getId());
	long id = database.insert(TABLE_NAME, null, values);
	database.close();
	medication.setId((int) id);
	return medication;
    }

    public int delete(Integer id_medication) {
	ContentValues values = new ContentValues();
	values.put(DELETED, 1);
	return database.update(TABLE_NAME, values, ID + " = ?", new String[] { id_medication.toString() });
    }

    public void open() {
	database = getWritableDatabase();
    }

    public ArrayList<Medication> getPotentialMedicationEveryDay(DateMidnight date) {

	String format_date = "yyyy-MM-dd HH:mm:ss";
	String date_formatted = date.toString(DateTimeFormat.forPattern(format_date));

	String selectQuery = "SELECT * from medication "
		+ "JOIN medication_schedule on medication.id_medication_schedule = medication_schedule.id "
		+ "JOIN medication_frequency ON medication_schedule.id_medication_frequency = medication_frequency.id "
		+ "WHERE "+DELETED+" = 0 AND medication_frequency.is_every_day = 1 AND ("
		+ "'"
		+ date_formatted
		+ "' BETWEEN medication_schedule.date_start AND datetime(medication_schedule.date_start, medication_schedule.number_days ||' DAYS')"
		+ " OR ( medication_schedule.is_continious = 1 " + " AND medication_schedule.date_start <= " + "'"
		+ date_formatted + "')" + ")";

	return getListMedication(selectQuery);
    }

    public ArrayList<Medication> getPotentialMedicationDayWeek(DateMidnight date) {

	String format_date = "yyyy-MM-dd HH:mm:ss";
	String date_formatted = date.toString(DateTimeFormat.forPattern(format_date));
	Integer day_of_week = date.getDayOfWeek();

	String selectQuery = "SELECT * from medication "
		+ "JOIN medication_schedule on medication.id_medication_schedule = medication_schedule.id "
		+ "JOIN medication_frequency ON medication_schedule.id_medication_frequency = medication_frequency.id "
		+ "WHERE "+DELETED+" = 0 AND medication_frequency.days_week LIKE '%"
		+ day_of_week
		+ "%' AND ("
		+ "'"
		+ date_formatted
		+ "' BETWEEN medication_schedule.date_start AND datetime(medication_schedule.date_start, medication_schedule.number_days ||' DAYS')"
		+ " OR ( medication_schedule.is_continious = 1 " + " AND medication_schedule.date_start <= " + "'"
		+ date_formatted + "')" + ")";

	return getListMedication(selectQuery);
    }

    public ArrayList<Medication> getPotentialMedicationIntervalDay(DateMidnight date) {

	String format_date = "yyyy-MM-dd HH:mm:ss";
	String date_formatted = date.toString(DateTimeFormat.forPattern(format_date));

	String selectQuery = "SELECT * from medication "
		+ "JOIN medication_schedule on medication.id_medication_schedule = medication_schedule.id "
		+ "JOIN medication_frequency ON medication_schedule.id_medication_frequency = medication_frequency.id "
		+ "WHERE "+DELETED+" = 0 AND medication_frequency.interval_day != 0 AND ("
		+ "'"
		+ date_formatted
		+ "' BETWEEN medication_schedule.date_start AND datetime(medication_schedule.date_start, medication_schedule.number_days ||' DAYS')"
		+ " OR ( medication_schedule.is_continious = 1 " + " AND medication_schedule.date_start <= " + "'"
		+ date_formatted + "')" + ")";

	return getListMedication(selectQuery);
    }
    
    public ArrayList<Medication> getAllMedication() {
	
	String selectQuery = "SELECT * from medication "
		+ "JOIN medication_schedule on medication.id_medication_schedule = medication_schedule.id "
		+ "JOIN medication_frequency ON medication_schedule.id_medication_frequency = medication_frequency.id "
		+ "WHERE "+DELETED+" = 0 ";

	return getListMedication(selectQuery);
    }

    public int update(Medication medication) {
	ContentValues values = new ContentValues();
	values.put(NAME, medication.getName());
	values.put(PICTURE, medication.getPicture());
	values.put(INSCTRUCTION_COMMENT, medication.getInstruction_comment());
	values.put(FOOD_INSCTUCTION, medication.getFood_instruction());
	return database.update(TABLE_NAME, values, ID + " = ?", new String[] { medication.getId().toString() });
    }

    private ArrayList<Medication> getListMedication(String selectQuery) {

	ArrayList<Medication> list_medication = new ArrayList<Medication>();
	Cursor cursor = database.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) {
	    do {
		Medication medication = cursorToMedication(cursor);
		MedicationSchedule medicationSchedule = MedicationScheduleDao.cursorToMedicationSchedule(cursor);
		MedicationFrequency medicationFrequency = MedicationFrequencyDao.cursorToMedicationFrequency(cursor);
		medicationSchedule.setMedication_frequency(medicationFrequency);
		medicationSchedule.setMedication(medication);

		MedicationHourDosageDao medicationHourDosageDao = new MedicationHourDosageDao(context);
		medicationHourDosageDao.open();
		medicationSchedule.setMedication_hour_dosage_list(medicationHourDosageDao
			.getListMedicationHourDosage(medicationSchedule));
		medicationHourDosageDao.close();
		medication.setSchedule_medication(medicationSchedule);

		list_medication.add(medication);
	    } while (cursor.moveToNext());
	}
	return list_medication;
    }

    public static Medication cursorToMedication(Cursor cursor) {
	return new Medication(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(cursor
		.getColumnIndex(FOOD_INSCTUCTION)), cursor.getString(cursor.getColumnIndex(INSCTRUCTION_COMMENT)));
    }

    

 

    public void close() {
	if (database != null) {
	    database.close();
	}
    }

}
