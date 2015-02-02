package com.bt.healthrecord;

import java.util.Calendar;
import java.util.Locale;

import com.bt.healthrecordDAO.NoteDAO;
import com.bt.healthrecordMODEL.Note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NoteEditActivity extends CommonActivity {

	Button saveNoteEdited;
	EditText titleNoteEdited, noteEdited;
	RadioGroup groupState;

	Integer state;

	String currentDate;

	Note note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);

		getResource();

		/* get object Note table */
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		note = (Note) extras.getSerializable("note");

		titleNoteEdited.setText(note.getTitle());
		noteEdited.setText(note.getNote());
	}

	private void getResource() {

		/* Retrieve resource from own layout edit note */
		saveNoteEdited = (Button) findViewById(R.id.noteSavedBtn);
		titleNoteEdited = (EditText) findViewById(R.id.noteTitleEditTxtV);
		noteEdited = (EditText) findViewById(R.id.noteEditedTxt);
		groupState = (RadioGroup) findViewById(R.id.groupState);

	}

	public void onClick (View view) {
		switch (view.getId()) {
		case R.id.noteSavedBtn:

			String titleNoteEditedString = titleNoteEdited.getText().toString(); // convert to string
			String noteEditedString = noteEdited.getText().toString(); // convert to string
			String ErrorForm = getResources().getString(R.string.error); // Retrieve message error

			/* 
			 * Check if the input filed is empty
			 * Show the error alert if true
			 */

			if (titleNoteEditedString.length() == 0) {
				titleNoteEdited.setError(ErrorForm);
			}else if (noteEditedString.length() == 0) {
				noteEdited.setError(ErrorForm);
			}else {
				/* Get current date */
				getCurrentDate();

				/* State */
				if (groupState.getCheckedRadioButtonId() == R.id.resolved) {
					state = 0;
				} else if (groupState.getCheckedRadioButtonId() == R.id.treating) {
					state = 1;
				}

				/* Edit Note */
				updateData();
				
				/* Back to the previous activity and send object note */
				Intent objIntent = new Intent(getApplicationContext(),NoteDisplayData.class);
				objIntent.putExtra("note", note);
				startActivity(objIntent); 				
			}

			break;
		}
	}

	private void updateData() {
		/* Note table Update */
		NoteDAO noteDAO = new NoteDAO(this);
		noteDAO.open();
		
		note.setTitle(titleNoteEdited.getText().toString());
		note.setDateUpdated(currentDate);
		note.setNote(noteEdited.getText().toString());
		note.setState(state);
		
		noteDAO.updateNote(note);
		noteDAO.close();
		Toast.makeText(getApplicationContext(), R.string.note_update, Toast.LENGTH_LONG).show();
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

	@Override
	public int getTitleActionBar() {
		return R.string.edit_note;
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
		return false;
	}

	@Override
	public boolean getUpNavigation() {
		return false;
	}

}
