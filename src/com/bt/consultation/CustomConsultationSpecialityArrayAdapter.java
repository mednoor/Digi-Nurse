package com.bt.consultation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.utils.Utils;

public class CustomConsultationSpecialityArrayAdapter extends ArrayAdapter<String> {

    private final String[] list_specialities;
    private final Activity context;

    public CustomConsultationSpecialityArrayAdapter(Activity context, String[] specialities) {
	super(context, R.layout.consultation_speciality_custom_row, specialities);
	this.context = context;
	//Collections.reverse(Arrays.asList(specialities));
	this.list_specialities = specialities;
    }

    static class ViewHolderSpeciality {
	protected TextView textView_speciality;
	protected ImageView imageView_speciality;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	return getcustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	return getcustomView(position, convertView, parent);
    }

    public View getcustomView(int position, View convertView, ViewGroup parent) {
	View view = null;
	String speciality = list_specialities[position];

	int id_speciality_icon = Utils.drawables[position];

	if (convertView == null) {
	    LayoutInflater inflator = context.getLayoutInflater();
	    view = inflator.inflate(R.layout.consultation_speciality_custom_row, null);
	    final ViewHolderSpeciality viewHolder = new ViewHolderSpeciality();
	    viewHolder.textView_speciality = (TextView) view.findViewById(R.id.textView_speciality);
	    viewHolder.imageView_speciality = (ImageView) view.findViewById(R.id.imageView_speciality);
	    view.setTag(viewHolder);
	} else {
	    view = convertView;
	}

	ViewHolderSpeciality holder = (ViewHolderSpeciality) view.getTag();

	holder.textView_speciality.setText(speciality);

	holder.imageView_speciality.setBackgroundResource(id_speciality_icon);

	return view;
    }

}
