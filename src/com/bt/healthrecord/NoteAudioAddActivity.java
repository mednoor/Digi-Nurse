package com.bt.healthrecord;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import com.bt.healthrecordDAO.AudioDAO;
import com.bt.healthrecordMODEL.Audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class NoteAudioAddActivity extends CommonActivity {

	MediaRecorder mRecorder = new MediaRecorder();
	MediaPlayer mPlayer = new MediaPlayer();

	boolean isRecording = false;
	boolean isPlaying = false;
	String curruentCalendar, path, currentDate;

	ImageButton playImgBtn, stopImgBtn, recordImgBtn;
	Button saveBtn, cancelBtn;

	LinearLayout confirmationLinear;
	TableRow tableRowConfirmation;

	EditText AudioTitleEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_record_audio);

		getResource();

		/* Disable Buttons and Linear */
		playImgBtn.setEnabled(false);
		stopImgBtn.setEnabled(false);
		saveBtn.setEnabled(false);
		cancelBtn.setEnabled(false);
		confirmationLinear.setVisibility(LinearLayout.GONE);
		tableRowConfirmation.setVisibility(TableRow.GONE);

	}

	public void onClick (View view) {

		/* Get current date */
		getCurrentDate();

		switch (view.getId()) {
		case R.id.noteRecordBtn:

			try {

				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				path = Environment.getExternalStorageDirectory() + "/" + curruentCalendar + ".3gp";
				mRecorder.setOutputFile(path);
				mRecorder.prepare();
				mRecorder.start();

				Toast.makeText(getApplicationContext(), R.string.record_recording, Toast.LENGTH_LONG).show();

				isRecording = true;

				stopImgBtn.setEnabled(true); // Enable Stop Button

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), R.string.record_error_playing, Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.noteStopBtn:

			if (isRecording) {

				isRecording = false;
				//mRecorder.stop();
				mRecorder.reset();
				Toast.makeText(getApplicationContext(), R.string.record_recorded, Toast.LENGTH_LONG).show();

				playImgBtn.setEnabled(true);
				saveBtn.setEnabled(true);
				cancelBtn.setEnabled(true);
				confirmationLinear.setVisibility(LinearLayout.VISIBLE);
				tableRowConfirmation.setVisibility(TableRow.VISIBLE);

				recordImgBtn.setEnabled(false); // Disable Record Btn
				Log.d("Path", path); // Log

			}else if (isPlaying) {

				isPlaying = false;
				mPlayer.stop();

				Toast.makeText(getApplicationContext(), R.string.record_stop, Toast.LENGTH_LONG).show();

			}

			break;

		case R.id.notePlayBtn:

			if (!isRecording && !mPlayer.isPlaying()) {
				try {

					mPlayer.reset();
					mPlayer.setDataSource(path);
					mPlayer.prepare();
					mPlayer.start();

					isPlaying = true;

					Toast.makeText(getApplicationContext(), R.string.record_play, Toast.LENGTH_LONG).show();

				} catch (Exception e) {

					Toast.makeText(getApplicationContext(), R.string.record_error, Toast.LENGTH_LONG).show();

				}
			}

			break;

		case R.id.noteConfirmationBtn:

			String AudioTitleEditString = AudioTitleEdit.getText().toString();
			String ErrorForm = getResources().getString(R.string.error); // Retrieve error

			/* 
			 * Check if the input filed is empty
			 * Show the error alert if true
			 */
			if ( AudioTitleEditString.length() == 0 ) {
				AudioTitleEdit.setError(ErrorForm);
			} else {

				/* Add New Audio */
				Audio audio = new Audio(0, path, currentDate, AudioTitleEditString);
				AudioDAO audioDao = new AudioDAO(getApplicationContext());
				audioDao.open();
				audioDao.insertAudio(audio);
				audioDao.close();

				this.callHomeActivity(view);

				Toast.makeText(getApplicationContext(), R.string.record_add, Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.noteCancelConfBtn:

			File file = new File(path);
			file.delete();

			this.callHomeActivity(view);

			Toast.makeText(getApplicationContext(), R.string.record_cancel, Toast.LENGTH_LONG).show();

			break;
		}
	}

	private void getResource() {
		playImgBtn = (ImageButton) findViewById(R.id.notePlayBtn);
		stopImgBtn = (ImageButton) findViewById(R.id.noteStopBtn);
		recordImgBtn = (ImageButton) findViewById(R.id.noteRecordBtn);

		saveBtn = (Button) findViewById(R.id.noteConfirmationBtn);
		cancelBtn = (Button) findViewById(R.id.noteCancelConfBtn);

		confirmationLinear = (LinearLayout) findViewById(R.id.confirmationLinear);
		tableRowConfirmation = (TableRow) findViewById(R.id.tableRowConfirmation);

		AudioTitleEdit = (EditText) findViewById(R.id.AudioTitleEdit);

	}

	private void getCurrentDate () {

		/* Use the current date */
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		curruentCalendar = (month+1) + "-" + day + "-" + year + "-" + hour + "-" + minute + "-" + second;

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
		// TODO Auto-generated method stub
		return R.string.new_record;
	}

	@Override
	public int getIconActionBar() {
		// TODO Auto-generated method stub
		return R.drawable.ic_record;
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
