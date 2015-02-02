package com.bt.healthrecordDAO;


import java.util.ArrayList;
import java.util.List;

import com.bt.healthrecordMODEL.Audio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AudioDAO extends DBController {

	public AudioDAO(Context applicationcontext) {
		super(applicationcontext);
	}

	public static final String TABLE_NAME = "audio";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String AUDIO_LINK = "audiolink";
	public static final String DATE = "date";
	
	public static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY, "+AUDIO_LINK+" TEXT, "+DATE+" TEXT, "+TITLE+" TEXT)";
	private SQLiteDatabase database;
	
	public int insertAudio(Audio audio) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(AUDIO_LINK, audio.getAudioLink());
		values.put(DATE, audio.getDate());
		values.put(TITLE, audio.getTitle());

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
	
	public void deleteAudio(Integer id) {
		String deleteQuery = "DELETE FROM "+TABLE_NAME+" where "+ID+"="+ id;
		Log.d("query",deleteQuery); // LOG
		database.execSQL(deleteQuery);
	}

	public List<Audio> getAllAudio() {
		ArrayList<Audio> list=new ArrayList<Audio>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Audio audio = cursorToAudio(cursor);
				list.add(audio);
			} while (cursor.moveToNext());
		}
		cursor.close(); // a verifier
		return list;
	}

	public Audio getInfoAudio(Integer id) {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + ID
				+ " = " + id;
		Cursor cursor = database.rawQuery(selectQuery, null);
		Audio audio = null;
		if (cursor != null && cursor.moveToFirst()) {
			audio = cursorToAudio(cursor);
		}
		cursor.close(); // a verifier
		return audio;
	}
	
	private static Audio cursorToAudio(Cursor cursor) {
		return new Audio(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
	}
}
