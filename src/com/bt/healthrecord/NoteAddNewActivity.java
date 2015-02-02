package com.bt.healthrecord;


import java.util.Calendar;
import java.util.Locale;

import com.bt.healthrecord.R;
import com.bt.healthrecordDAO.NoteDAO;
import com.bt.healthrecordMODEL.Note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteAddNewActivity extends CommonActivity {

	/* Declare resources */
	Button noteAddSaveBtn;
	EditText titleEditText, noteEditText;
	String currentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_add_new);

		getResource();

	}
	private void getResource() {
		noteAddSaveBtn = (Button) findViewById(R.id.noteAddSave);

		titleEditText = (EditText) findViewById(R.id.noteTitleEditTxt);
		noteEditText = (EditText) findViewById(R.id.noteEditTxt);
	}

	public void onClick (View view) {
		switch (view.getId()) {
		case R.id.noteAddSave:
			String titleEditTextString = titleEditText.getText().toString(); // Retrieve title text
			String noteEditTextString = noteEditText.getText().toString(); // Retrieve note text
			String ErrorForm = getResources().getString(R.string.error); // Retrieve error
			/* 
			 * Check if the input filed is empty
			 * Show the error alert if true
			 */
			if ( titleEditTextString.length() == 0 )
				titleEditText.setError(ErrorForm);
			else if ( noteEditTextString.length() == 0 )
				noteEditText.setError(ErrorForm);
			else {
				/* Get current date */
				getCurrentDate();

				/* Add New Note */
				Note note = new Note(0, titleEditTextString, noteEditTextString, currentDate.toString(), currentDate.toString(), 1); // STATE 0 if OK
				NoteDAO noteDao = new NoteDAO(getApplicationContext());
				noteDao.open();
				noteDao.insertNote(note);
				noteDao.close();
				this.callHomeActivity(view);
				Toast.makeText(getApplicationContext(), R.string.record_add, Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	private void getCurrentDate () {

		/* Use the current date as the default date in the picker */
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		String languagePhone = Locale.getDefault().getLanguage();

		if (languagePhone.toString().equals("fr")) {
			// Return the data of the datePicker to the parent activity in French format
			currentDate = day+"/"+(month+1)+"/"+year;
		} else {
			// Return the data of the datePicker to the parent activity in English format
			currentDate = (month+1)+"/"+day+"/"+year;
		}
	}

	/* Return to parent activity */
	public void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(), NoteMainActivity.class);
		startActivity(objIntent);
	}

	@Override
	public int getTitleActionBar() {
		return R.string.new_note;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_notepad;
	}

	@Override
	public int getColorActionBar() {
		return R.color.noteBg;
	}

	@Override
	public boolean getHomeButtonActionBar() {
		return true;
	}

	@Override
	public boolean getUpNavigation() {
		return true;
	}
}
