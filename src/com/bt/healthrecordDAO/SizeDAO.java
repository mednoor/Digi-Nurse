package com.bt.healthrecordDAO;

import com.bt.healthrecordMODEL.Size;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SizeDAO extends DBController {

	public SizeDAO(Context applicationcontext) {
		super(applicationcontext);
		// TODO Auto-generated constructor stub
	}

	public static final String TABLE_NAME = "sizeHistory";
	public static final String ID = "id";
	public static final String SIZE = "size";
	public static final String DATE = "date";
	public static final String UNIT = "unit";

	public static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY, "+SIZE+" TEXT, "+DATE+" TEXT, "+UNIT+" TEXT)";
	private SQLiteDatabase database;

	public int insertSize(Size size) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(SIZE, size.getSize());
		values.put(DATE, size.getDate());
		values.put(UNIT, size.getUnit());

		long id = database.insert(TABLE_NAME, null, values);
		database.close();
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

	public int updateSize(Size size) {
		ContentValues values = new ContentValues();

		values.put(SIZE, size.getSize());
		values.put(DATE, size.getDate());
		values.put(UNIT, size.getUnit());

		return database.update(TABLE_NAME, values, ID + " = ?", new String[] { size.getId().toString() });
	}

	public void deleteSize(Integer id) {
		String deleteQuery = "DELETE FROM "+TABLE_NAME+" where "+ID+"="+ id;
		Log.d("query",deleteQuery); // LOG
		database.execSQL(deleteQuery);
	}

	public Size getInfoSize() {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " order by " + ID
				+ " DESC limit 1";
		Cursor cursor = database.rawQuery(selectQuery, null);
		Size size = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				size = cursorToSize(cursor);
			} while (cursor.moveToNext());
		}
		cursor.close(); // --------
		return size;
	}

	private Size cursorToSize(Cursor cursor) {
		// Cursor Size
		return new Size(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
	}
}
