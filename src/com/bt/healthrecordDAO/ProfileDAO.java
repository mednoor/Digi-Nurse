package com.bt.healthrecordDAO;

import com.bt.healthrecordMODEL.Profile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProfileDAO extends DBController {

	public ProfileDAO(Context applicationcontext) {
		super(applicationcontext);
	}

	public static final String TABLE_NAME="profile";
	public static final String ID="id";
	public static final String FIRSTNAME="firstName";
	public static final String LASTNAME="lastName";
	public static final String BIRTHDAY="birthDay";
	public static final String BLOODTYPE = "bloodType";
	public static final String DOCTOR = "doctor";
	public static final String IMGLINK = "imgLink"; // Image user link
	public static final String CIVILITY="civility";

	public static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY, "+FIRSTNAME+" TEXT, "
			+LASTNAME+" TEXT, "+BIRTHDAY+" TEXT, "+BLOODTYPE+" TEXT, "+DOCTOR+" TEXT, "+IMGLINK+" TEXT, "+CIVILITY+" TEXT)";
	private SQLiteDatabase database;

	public int insertProfile(Profile profile) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(FIRSTNAME, profile.getFirstName());
		values.put(LASTNAME, profile.getLastName());
		values.put(BIRTHDAY, profile.getBirthDay());
		values.put(BLOODTYPE, profile.getBloodType());
		values.put(DOCTOR, profile.getDoctor());
		values.put(IMGLINK, profile.getImgLink());
		values.put(CIVILITY, profile.getCivility());

		long id = database.insert(TABLE_NAME, null, values);
		// database.close();
		return (int)id;
	}

	public void open(){
		database = getWritableDatabase();
	}

	public void close(){
		if(database!=null){
			database.close();
		}
	}

	public int updateProfile(Profile profile) {
		ContentValues values = new ContentValues();

		values.put(FIRSTNAME, profile.getFirstName());
		values.put(LASTNAME, profile.getLastName());
		values.put(BIRTHDAY, profile.getBirthDay());
		values.put(BLOODTYPE, profile.getBloodType());
		values.put(DOCTOR, profile.getDoctor());
		values.put(IMGLINK, profile.getImgLink());
		values.put(CIVILITY, profile.getCivility());

		return database.update(TABLE_NAME, values, ID + " = ?", new String[] { profile.getId().toString() });
	}

	public void deleteProfile(Integer id) {
		String deleteQuery = "DELETE FROM "+TABLE_NAME+" where "+ID+"="+ id;
		Log.d("query",deleteQuery); // LOG
		database.execSQL(deleteQuery);
	} 

	public Profile getInfoProfile() {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " order by " + ID
				+ " DESC limit 1";
		Cursor cursor = database.rawQuery(selectQuery, null);
		Profile profile = null;
		if (cursor != null && cursor.moveToFirst()) {
			profile = cursorToProfile(cursor);
		}
		cursor.close(); // a verifier
		return profile;
	}

	public static Profile cursorToProfile(Cursor cursor) {
		return new Profile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5),cursor.getString(6), cursor.getString(7));
	}
}
