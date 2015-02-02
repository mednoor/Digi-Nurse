package com.bt.healthrecord;

import java.util.List;

import com.bt.healthrecordDAO.Emergency;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EmergencyCustomArrayAdapter extends ArrayAdapter <Emergency> {

	private final List<Emergency> list;
	private final Activity context;

	public EmergencyCustomArrayAdapter(Activity context, List<Emergency> list) {
		super(context, R.layout.emergency_custoncell, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView nom, profil, tel;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.emergency_custoncell, null);

			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.nom = (TextView) view.findViewById(R.id.emergencyNameCustom);
			viewHolder.profil = (TextView) view.findViewById(R.id.emergencyProfilCustom);
			viewHolder.tel = (TextView) view.findViewById(R.id.emergencyTelCustom);
			view.setTag(viewHolder);

		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		Emergency emergency = list.get(position);
		holder.nom.setText(emergency.getName());
		holder.profil.setText(emergency.getProfil());
		holder.tel.setText(emergency.getTel());
		
		//holder.deleteImageView.setTag(audio);

		return view;
	}
	

}
