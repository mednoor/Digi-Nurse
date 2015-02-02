package com.bt.healthrecordDAO;

import com.bt.consultation_dao.AppointmentDao;
import com.bt.medication_dao.MedicationDao;
import com.bt.medication_dao.MedicationFrequencyDao;
import com.bt.medication_dao.MedicationHourDosageDao;
import com.bt.medication_dao.MedicationScheduleDao;
import com.bt.medication_dao.MedicationTakenDao;
import com.bt.medication_model.MedicationTaken;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {

	private static final String LOGCAT = null;
	private static String name = "healthrecord.db"; // Name DB

	private static Integer version = 28; // Version
 

	/* Constructor */
	public DBController(Context applicationcontext) {
		super(applicationcontext, name, null, version);
		Log.d(LOGCAT, "DB healtRecord Created"); // LOG
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		/* Profile */
		database.execSQL(ProfileDAO.TABLE_CREATE);
		Log.d(LOGCAT, "Profile table Created"); // LOG

		/* Size */
		database.execSQL(SizeDAO.TABLE_CREATE);
		Log.d(LOGCAT, "Size table Created"); // LOG

		/* Weight */
		database.execSQL(WeightDAO.TABLE_CREATE);
		Log.d(LOGCAT, "Weight table Created"); // LOG

		/* Note */
		database.execSQL(NoteDAO.TABLE_CREATE);
		Log.d(LOGCAT, "Note table Created"); // LOG

		/* Audio Note */
		database.execSQL(AudioDAO.TABLE_CREATE);
		Log.d(LOGCAT, "Audio table Created"); // LOG

		/* Audio Emergency */
		database.execSQL(EmergencyDAO.TABLE_CREATE);
		Log.d(LOGCAT, "Emergency table Created"); // LOG

		/* Tables for medication */
		database.execSQL(MedicationDao.TABLE_CREATE);
		database.execSQL(MedicationScheduleDao.TABLE_CREATE);
		database.execSQL(MedicationFrequencyDao.TABLE_CREATE);
		database.execSQL(MedicationHourDosageDao.TABLE_CREATE);
		database.execSQL(MedicationTakenDao.TABLE_CREATE);

		/* Tables for consultation */
		database.execSQL(AppointmentDao.TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// Upgrade table Profile
		String query = "DROP TABLE IF EXISTS " + ProfileDAO.TABLE_NAME;
		database.execSQL(query);

		// Upgrade table Weight
		query = "DROP TABLE IF EXISTS " + WeightDAO.TABLE_NAME;
		database.execSQL(query);

		// Upgrade table Size
		query = "DROP TABLE IF EXISTS " + SizeDAO.TABLE_NAME;
		database.execSQL(query);

		// Upgrade table Note
		query = "DROP TABLE IF EXISTS " + NoteDAO.TABLE_NAME;
		database.execSQL(query);

		// Upgrade table Note
		query = "DROP TABLE IF EXISTS " + AudioDAO.TABLE_NAME;
		database.execSQL(query);

		// Upgrade table Emergency
		query = "DROP TABLE IF EXISTS " + EmergencyDAO.TABLE_NAME;
		database.execSQL(query);

		/* Upgrade all tables Medication */
		query = "DROP TABLE IF EXISTS " + MedicationDao.TABLE_NAME;
		database.execSQL(query);
		query = "DROP TABLE IF EXISTS " + MedicationScheduleDao.TABLE_NAME;
		database.execSQL(query);
		query = "DROP TABLE IF EXISTS " + MedicationFrequencyDao.TABLE_NAME;
		database.execSQL(query);
		query = "DROP TABLE IF EXISTS " + MedicationHourDosageDao.TABLE_NAME;
		database.execSQL(query);
		query = "DROP TABLE IF EXISTS " + MedicationTakenDao.TABLE_NAME;
		database.execSQL(query);
		

		// Upgrade table Picture
		query = "DROP TABLE IF EXISTS " + AppointmentDao.TABLE_NAME;
		database.execSQL(query);

		onCreate(database);

	}

}

