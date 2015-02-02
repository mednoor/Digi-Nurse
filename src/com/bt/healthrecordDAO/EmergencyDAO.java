package com.bt.healthrecordDAO;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EmergencyDAO extends DBController {

	public EmergencyDAO(Context applicationcontext) {
		super(applicationcontext);
	}
	
	public static final String TABLE_NAME = "emergency";
	public static final String ID = "id";
	public static final String PROFIL = "profil";
	public static final String NAME = "name";
	public static final String TEL = "tel";
	
	public static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY, "+PROFIL+" TEXT, "+NAME+" TEXT, "+TEL+" TEXT)";
	private SQLiteDatabase database;
	
	public int insertEmergency(Emergency emergency) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(PROFIL, emergency.getProfil());
		values.put(NAME, emergency.getName());
		values.put(TEL, emergency.getTel());

		long id = database.insert(TABLE_NAME, null, values);
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
	
	public void deleteEmergency(Integer id) {
		String deleteQuery = "DELETE FROM "+TABLE_NAME+" where "+ID+"="+ id;
		Log.d("query",deleteQuery); // LOG
		database.execSQL(deleteQuery);
	}

	public List<Emergency> getAllEmergency() {
		ArrayList<Emergency> list=new ArrayList<Emergency>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Emergency emergency = cursorToEmergency(cursor);
				list.add(emergency);
			} while (cursor.moveToNext());
		}
		cursor.close(); // a verifier
		return list;
	}

	public Emergency getInfoEmergency(Integer id) {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + ID
				+ " = " + id;
		Cursor cursor = database.rawQuery(selectQuery, null);
		Emergency emergency = null;
		if (cursor != null && cursor.moveToFirst()) {
			emergency = cursorToEmergency(cursor);
		}
		cursor.close(); // a verifier
		return emergency;
	}
	
	private static Emergency cursorToEmergency(Cursor cursor) {
		return new Emergency(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
	}

}
