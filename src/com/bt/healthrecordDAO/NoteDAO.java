package com.bt.healthrecordDAO;

import java.util.ArrayList;
import java.util.List;

import com.bt.healthrecordMODEL.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NoteDAO extends DBController {

	public NoteDAO(Context applicationcontext) {
		super(applicationcontext);
	}

	public static final String TABLE_NAME = "note";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String NOTE = "note";
	public static final String DATE_CREATED = "dateCreated";
	public static final String DATE_UPDATED = "dateUpdated";
	public static final String STATE = "state";

	public static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY, "+TITLE+" TEXT, "
			+NOTE+" TEXT, "+DATE_CREATED+" TEXT, "+DATE_UPDATED+" TEXT, "+STATE+" INTEGER)";

	private SQLiteDatabase database;

	public int insertNote(Note note) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(TITLE, note.getTitle());
		values.put(NOTE, note.getNote());
		values.put(DATE_CREATED, note.getDateCreated());
		values.put(DATE_UPDATED, note.getDateUpdated());
		values.put(STATE, note.getState());

		long id = database.insert(TABLE_NAME, null, values);
		return (int)id;
	}

	public void open(){
		if((database==null)||(!database.isOpen()))
		database = getWritableDatabase();
	}

	public void close(){
		if((database!=null)&&(database.isOpen())){
			database.close();
		}
	}

	public int updateNote(Note note) {
		ContentValues values = new ContentValues();

		values.put(TITLE, note.getTitle());
		values.put(NOTE, note.getNote());
		//values.put(DATE_CREATED, note.getDateCreated());
		values.put(DATE_UPDATED, note.getDateUpdated());
		values.put(STATE, note.getState());

		return database.update(TABLE_NAME, values, ID + " = ?", new String[] { note.getId().toString() });
	}

	public void deleteNote(Integer id) {
		String deleteQuery = "DELETE FROM "+TABLE_NAME+" where "+ID+"="+ id;
		Log.d("query",deleteQuery); // LOG
		database.execSQL(deleteQuery);
	}

	public List<Note> getAllNote() {
		ArrayList<Note> list=new ArrayList<Note>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY ID DESC";
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	Note note = cursorToNote(cursor);
	        	list.add(note);
	        } while (cursor.moveToNext());
	    }
	    return list;
	}

	public Note getInfoNote(Integer id) {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + ID
				+ " = " + id;
		Cursor cursor = database.rawQuery(selectQuery, null);
		Note note = null;
		if (cursor != null && cursor.moveToFirst()) {
			note = cursorToNote(cursor);
		}
		return note;
	}

	private static Note cursorToNote(Cursor cursor) {
		return new Note (cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getInt(5)) ;
	}
}
