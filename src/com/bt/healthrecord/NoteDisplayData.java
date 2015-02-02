package com.bt.healthrecord;

import com.bt.healthrecordDAO.NoteDAO;
import com.bt.healthrecordMODEL.Note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDisplayData extends CommonActivity {

	TextView title, noteTextView, dateCreated, dateUpdated;
	ImageView state;
	Note note;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_display_data);

		getResource();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		note = (Note) extras.getSerializable("note");

		title.setText(note.getTitle());
		noteTextView.setText(note.getNote());
		dateCreated.setText(note.getDateCreated());
		dateUpdated.setText(note.getDateUpdated());
		// STATE
		if (note.getState() == 0) {
			state.setImageResource(R.drawable.ic_states_ok);
		} else if (note.getState() == 1) {
			state.setImageResource(R.drawable.ic_states);
		} else {
			state.setVisibility(View.GONE);
		}
	}

	public void getResource() {
		title = (TextView) findViewById(R.id.noteTitleTxtV);
		noteTextView = (TextView) findViewById(R.id.noteTxtV);
		dateCreated = (TextView) findViewById(R.id.noteDataAddTxtV);
		dateUpdated = (TextView) findViewById(R.id.noteUpdateTxtV);
		state = (ImageView) findViewById(R.id.noteState);
	}

	public void onClick (View v) {
		switch (v.getId()) {
		case R.id.noteDataDeleteBtn:
			showDeleteAlertDialog();
			break;

		case R.id.noteEditBtn:
			Intent objIndent = new Intent(getApplicationContext(),NoteEditActivity.class);
			objIndent.putExtra("note", note);
			startActivity(objIndent); 
			break;
		}
	}


	/* Launch the alert dialog when the user click on the pictureBtn */
	public void showDeleteAlertDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteDisplayData.this);

		// Setting Dialog Title
		alertDialog.setTitle(R.string.note_sure);

		// Setting Positive "confirmer" Btn
		alertDialog.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				deletedata();
				Intent intent = new Intent(getApplicationContext(),NoteMainActivity.class);
				startActivity(intent); 		
				Toast.makeText(getApplicationContext(), R.string.record_delete, Toast.LENGTH_LONG).show();
			}
		});

		// Setting Negative "Annuler" Btn
		alertDialog.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				Toast.makeText(getApplicationContext(), R.string.record_cancel, Toast.LENGTH_LONG).show();
			}
		});

		// Showing Alert Dialog
		alertDialog.show();

	}

	public void deletedata (){
		/* Note table Delete */
		NoteDAO noteDAO = new NoteDAO(this);
		noteDAO.open();
		noteDAO.deleteNote(note.getId());
		noteDAO.close();
	}

	@Override
	public int getTitleActionBar() {
		// TODO Auto-generated method stub
		return R.string.note;
	}

	@Override
	public int getIconActionBar() {
		// TODO Auto-generated method stub
		return R.drawable.ic_notepad;
	}

	@Override
	public int getColorActionBar() {
		// TODO Auto-generated method stub
		return R.color.noteBg;
	}

	@Override
	public boolean getHomeButtonActionBar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getUpNavigation() {
		// TODO Auto-generated method stub
		return true;
	}
}
