package com.bt.consultation;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.consultation_model.Appointment;
import com.bt.healthrecord.R;
import com.bt.utils.Utils;

public class CustomAppointmentsListingArrayAdapter extends ArrayAdapter<Appointment> {

    private final List<Appointment> list;
    private final Activity context;

    public CustomAppointmentsListingArrayAdapter(Activity context, List<Appointment> list) {
	super(context, R.layout.consultation_listing_custom_row, list);
	this.context = context;
	this.list = list;
    }

    static class ViewHolderListing {
	protected TextView textView_hour_appointment;
	protected TextView textView_name_speciality;
	protected ImageView imageView_speciality;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	View view = null;
	Appointment appointment = list.get(position);

	if (convertView == null) {
	    LayoutInflater inflator = context.getLayoutInflater();
	    view = inflator.inflate(R.layout.consultation_listing_custom_row, null);
	    final ViewHolderListing viewHolder = new ViewHolderListing();
	    viewHolder.textView_hour_appointment = (TextView) view.findViewById(R.id.textView_hour_appointment);
	    viewHolder.textView_name_speciality = (TextView) view.findViewById(R.id.textView_name_speciality);
	    viewHolder.imageView_speciality = (ImageView) view.findViewById(R.id.imageView_speciality);
	    view.setTag(viewHolder);
	} else {
	    view = convertView;
	}

	ViewHolderListing holder = (ViewHolderListing) view.getTag();

	holder.textView_hour_appointment.setText(Utils.displayHour(appointment.getHourAppointment(), context));
	holder.textView_name_speciality.setText(appointment.getNameSpeciality(context));
	holder.imageView_speciality.setBackgroundResource(appointment.getIconSpeciality(context));
	return view;
    }
}
