package com.bt.healthrecordDAO;

import com.bt.healthrecordMODEL.Weight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WeightDAO extends DBController {

	public WeightDAO(Context applicationcontext) {
		super(applicationcontext);
		// TODO Auto-generated constructor stub
	}

	public static final String TABLE_NAME = "weightHistory";
	public static final String ID = "id";
	public static final String WEIGHT = "weight";
	public static final String DATE = "date";
	public static final String UNIT = "unit";

	public static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY, "+WEIGHT+" TEXT, "+DATE+" TEXT, "+UNIT+" TEXT)";
	private SQLiteDatabase database;

	public int insertWeight(Weight weight) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(WEIGHT, weight.getWeight());
		values.put(DATE, weight.getDate());
		values.put(UNIT, weight.getUnit());

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

	public int updateWeight(Weight weight) {
		ContentValues values = new ContentValues();

		values.put(WEIGHT, weight.getWeight());
		values.put(DATE, weight.getDate());
		values.put(UNIT, weight.getUnit());

		return database.update(TABLE_NAME, values, ID + " = ?", new String[] { weight.getId().toString() });
	}

	public void deleteWeight(Integer id) {
		String deleteQuery = "DELETE FROM "+TABLE_NAME+" where "+ID+"="+ id;
		Log.d("query",deleteQuery); // LOG
		database.execSQL(deleteQuery);
	}

	public Weight getInfoWeight() {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " order by " + ID
				+ " DESC limit 1";
		Cursor cursor = database.rawQuery(selectQuery, null);
		Weight weight = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				weight = cursorToWeight(cursor);
			} while (cursor.moveToNext());
		}
		cursor.close(); // a verifier
		return weight;
	}

	private Weight cursorToWeight(Cursor cursor) {
		// Cursor Weight
		return new Weight(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
	}
}
