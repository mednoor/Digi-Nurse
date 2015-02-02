package com.bt.healthrecord;

import java.util.List;

import com.bt.healthrecord.R;
import com.bt.healthrecordMODEL.Note;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteCustomArrayAdapter extends ArrayAdapter<Note> {
	
	private final List<Note> list;
	private final Activity context;

	public NoteCustomArrayAdapter(Activity context, List<Note> list) {
		super(context, R.layout.note_customcell, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView title, dateCreated, dateUpdated;
		protected ImageView stateImageView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.note_customcell, null);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.dateCreated = (TextView) view.findViewById(R.id.date_created);
			viewHolder.dateUpdated = (TextView) view.findViewById(R.id.date_updated);
			viewHolder.stateImageView = (ImageView) view.findViewById(R.id.stateImageView);
			view.setTag(viewHolder);

		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		Note note = list.get(position);
		holder.title.setText(note.getTitle());
		holder.dateCreated.setText(note.getDateCreated());
		holder.dateUpdated.setText(note.getDateUpdated());
		/* different states of note */
		if(note.getState() == 0){
			holder.stateImageView.setImageResource(R.drawable.ic_states_ok);
		}else if (note.getState() == 1){
			holder.stateImageView.setImageResource(R.drawable.ic_states);
		} else {
			holder.stateImageView.setVisibility(View.GONE);
		}
		return view;
	}

}
