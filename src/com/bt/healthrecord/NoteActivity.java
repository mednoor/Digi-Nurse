package com.bt.healthrecord;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bt.healthrecord.R;
import com.bt.healthrecordDAO.NoteDAO;
import com.bt.healthrecordMODEL.Note;

public class NoteActivity extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.note_activity, container, false);
	}

	@Override
	public void onResume() {
		NoteDAO noteDao = new NoteDAO(getActivity().getApplicationContext());
		noteDao.open();
		List<Note> noteList =  noteDao.getAllNote();
		noteDao.close();
		if(noteList!=null) {
			ListView lv = (ListView) getActivity().findViewById(R.id.note_listView);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				  @Override 
				  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					  Note note = (Note) parent.getItemAtPosition(position);
					  Intent objIndent = new Intent(getActivity().getApplicationContext(),NoteDisplayData.class);
					  objIndent.putExtra("note", note);
					  startActivity(objIndent); 
				  }
			});
			NoteCustomArrayAdapter adapter = new NoteCustomArrayAdapter(getActivity(),noteList); 
			lv.setAdapter(adapter);
		}
		// TODO Auto-generated method stub
		super.onResume();
	}

	
}
