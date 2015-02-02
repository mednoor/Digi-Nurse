package com.bt.healthrecord;

import java.io.File;

import com.bt.healthrecordDAO.AudioDAO;
import com.bt.healthrecordMODEL.Audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AudioDisplayData extends CommonActivity {

	MediaPlayer mPlayer = new MediaPlayer();

	boolean isPlaying = false;

	TextView titleView, dateView;
	ImageButton playBtnDisplay, stopBtnDisplay;
	Button yesBtnDisplay, nonBtnDisplay;

	Audio audio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit_audio);

		getResource();

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		audio = (Audio) extras.getSerializable("audio");

		titleView.setText(audio.getTitle());
		dateView.setText(audio.getDate());

		stopBtnDisplay.setEnabled(false);

	}

	public void onClick (View view) {
		switch (view.getId()) {
		case R.id.audioPlayBtn:

			if (!mPlayer.isPlaying()) {
				try {

					mPlayer.reset();
					mPlayer.setDataSource(audio.getAudioLink());
					mPlayer.prepare();
					mPlayer.start();

					isPlaying = true;

					Toast.makeText(getApplicationContext(), R.string.record_play, Toast.LENGTH_LONG).show();

					playBtnDisplay.setEnabled(false);
					stopBtnDisplay.setEnabled(true);

				} catch (Exception e) {

					Toast.makeText(getApplicationContext(), R.string.record_error, Toast.LENGTH_LONG).show();

				}
			}

			break;

		case R.id.audioStopBtn:

			if (isPlaying) {

				isPlaying = false;
				mPlayer.stop();

				Toast.makeText(getApplicationContext(), R.string.record_stop, Toast.LENGTH_LONG).show();
				
				playBtnDisplay.setEnabled(true);
				stopBtnDisplay.setEnabled(false);
			}

			break;

		case R.id.yesAudioBtn:
			
			deleteAudio();
			
			File file = new File(audio.getAudioLink());
			file.delete();
			
			this.callHomeActivity(view);
			Toast.makeText(getApplicationContext(), R.string.record_delete, Toast.LENGTH_LONG).show();
			
			break;

		case R.id.nonAudioBtn:
			
			this.callHomeActivity(view);
			
			break;
		}
	}

	public void getResource ()  {
		titleView = (TextView) findViewById(R.id.AudioTitleView);
		dateView = (TextView) findViewById(R.id.AudioDateView);

		playBtnDisplay = (ImageButton) findViewById(R.id.audioPlayBtn);
		stopBtnDisplay = (ImageButton) findViewById(R.id.audioStopBtn);

		yesBtnDisplay = (Button) findViewById(R.id.yesAudioBtn);
		nonBtnDisplay = (Button) findViewById(R.id.nonAudioBtn);
	}

	public void deleteAudio (){
		/* Audio table Delete */
		AudioDAO audioDAO = new AudioDAO(this);
		audioDAO.open();
		audioDAO.deleteAudio(audio.getId());
		audioDAO.close();
	}

	/* Return to parent activity */
	public void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(), NoteMainActivity.class);
		startActivity(objIntent);
	}

	@Override
	public int getTitleActionBar() {
		// TODO Auto-generated method stub
		return R.string.edit_record;
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
