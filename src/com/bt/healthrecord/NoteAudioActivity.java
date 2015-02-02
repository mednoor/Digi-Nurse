package com.bt.healthrecord;

import java.util.List;

import com.bt.healthrecordDAO.AudioDAO;
import com.bt.healthrecordMODEL.Audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NoteAudioActivity extends Fragment {

	ImageView delete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.note_audio, container, false);
	}

	@Override
	public void onResume() {
		AudioDAO audioDao = new AudioDAO(getActivity().getApplicationContext());
		audioDao.open();
		List<Audio> audioList =  audioDao.getAllAudio();
		audioDao.close();
		if(audioList!=null) {
			ListView lv = (ListView) getActivity().findViewById(R.id.audio_listView);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override 
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					Audio audio = (Audio) parent.getItemAtPosition(position);
					Intent objIndent = new Intent(getActivity().getApplicationContext(),AudioDisplayData.class);
					objIndent.putExtra("audio", audio);
					startActivity(objIndent); 
				}
			});

			NoteAudioCustomArrayAdapter adapter = new NoteAudioCustomArrayAdapter(getActivity(),audioList); 
			lv.setAdapter(adapter);

		}
		// TODO Auto-generated method stub
		super.onResume();
	}

}
