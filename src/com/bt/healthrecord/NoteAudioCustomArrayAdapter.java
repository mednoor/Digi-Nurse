package com.bt.healthrecord;

import java.util.List;

import com.bt.healthrecordMODEL.Audio;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoteAudioCustomArrayAdapter extends ArrayAdapter <Audio> {

	private final List<Audio> list;
	private final Activity context;

	public NoteAudioCustomArrayAdapter(Activity context, List<Audio> list) {
		super(context, R.layout.note_audio_custoncell, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView title, dateCreated;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.note_audio_custoncell, null);

			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.dateCreated = (TextView) view.findViewById(R.id.date_created);
			view.setTag(viewHolder);

		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		Audio audio = list.get(position);
		holder.title.setText(audio.getTitle());
		//holder.deleteImageView.setTag(audio);
		holder.dateCreated.setText(audio.getDate());

		return view;
	}


}
