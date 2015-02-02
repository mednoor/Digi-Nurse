package com.bt.medication_dao;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bt.healthrecordDAO.DBController;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationFrequency;
import com.bt.medication_model.MedicationSchedule;

public class MedicationScheduleDao extends DBController {

	public MedicationScheduleDao(Context applicationcontext) {
		super(applicationcontext);
		// TODO Auto-generated constructor stub
	}

	public static final String TABLE_NAME = "medication_schedule";
	public static final String ID = "id";
	public static final String DATE_START = "date_start";
	public static final String NUMBER_DAYS = "number_days";
	public static final String IS_CONTINIOUS = "is_continious";
	public static final String ID_FREQUENCY = "id_medication_frequency";

	public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
			+ " ( " + ID + " INTEGER PRIMARY KEY, " + DATE_START
			+ " DATETIME, " + NUMBER_DAYS + " INTEGER , " + IS_CONTINIOUS
			+ " INTEGER , " + ID_FREQUENCY + " INTEGER)";
	private SQLiteDatabase database;

	public MedicationSchedule insertMedicationSchedule(
			MedicationSchedule medication_schedule) {
		SQLiteDatabase database = this.getWritableDatabase();
		DateMidnight dd = medication_schedule.getDate_start()
				.withZoneRetainFields(DateTimeZone.UTC);
		long sqliteUnixEpoch = dd.getMillis() / 1000;
		Integer is_continious = (medication_schedule.getIs_continuous() == true ? 1
				: 0);

		String query_insert = "INSERT INTO " + TABLE_NAME + " (" + DATE_START
				+ ", " + NUMBER_DAYS + ", " + IS_CONTINIOUS + ", "
				+ ID_FREQUENCY + " ) VALUES ( datetime(" + sqliteUnixEpoch
				+ ", 'unixepoch') , " + medication_schedule.getNumber_of_days()
				+ ", " + is_continious + " , "
				+ medication_schedule.getMedication_frequency().getId() + ");";

		database.execSQL(query_insert);

		long lastId = getHighestID();
		medication_schedule.setId((int) lastId);

		database.close();

		return medication_schedule;
	}

	public void update(MedicationSchedule medication_schedule) {
		DateMidnight dd = medication_schedule.getDate_start()
				.withZoneRetainFields(DateTimeZone.UTC);
		long sqliteUnixEpoch = dd.getMillis() / 1000;
		Integer is_continious = (medication_schedule.getIs_continuous() == true ? 1
				: 0);

		String query_update = "UPDATE " + TABLE_NAME + " SET " + DATE_START
				+ " = datetime(" + sqliteUnixEpoch + ", 'unixepoch') , "
				+ NUMBER_DAYS + " =  "
				+ medication_schedule.getNumber_of_days() + " , "
				+ IS_CONTINIOUS + " = " + is_continious + " WHERE ID = "
				+ medication_schedule.getId();

		database.execSQL(query_update);
	}

	public int getHighestID() {
		final String MY_QUERY = "SELECT MAX(id) FROM "
				+ MedicationScheduleDao.TABLE_NAME;
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

	public static MedicationSchedule cursorToMedicationSchedule(Cursor cursor) {
		DateMidnight date_start = DateMidnight.parse(cursor.getString(8),
				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
		date_start = date_start.withZoneRetainFields(DateTimeZone.UTC);
		Boolean is_continious = cursor.getInt(10) == 1 ? true : false;
		return new MedicationSchedule(cursor.getInt(7), date_start,
				cursor.getInt(9), is_continious);
	}

}
