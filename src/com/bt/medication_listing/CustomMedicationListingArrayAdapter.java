package com.bt.medication_listing;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.medication_model.Medication;

public class CustomMedicationListingArrayAdapter extends
		ArrayAdapter<Medication> {

	private final List<Medication> list;
	private final Activity context;

	public CustomMedicationListingArrayAdapter(Activity context,
			List<Medication> list) {
		super(context, R.layout.medication_listing_custom_row, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolderListing {
		protected TextView text_name;
		protected ImageView picture_medication;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		Medication medication = list.get(position);

		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.medication_listing_custom_row,
					null);
			final ViewHolderListing viewHolder = new ViewHolderListing();
			viewHolder.text_name = (TextView) view
					.findViewById(R.id.textView_name_of_medication);
			viewHolder.picture_medication = (ImageView) view
					.findViewById(R.id.imageView_listing_name_medication);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		ViewHolderListing holder = (ViewHolderListing) view.getTag();

		holder.text_name.setText(medication.getName());

		if (medication.getPicture().isEmpty()) {
			holder.picture_medication.setImageBitmap(null);
			holder.picture_medication
					.setBackgroundResource(R.drawable.pill_default);
		} else {
			medication.createBitmap();
			holder.picture_medication.setBackgroundResource(0);
			holder.picture_medication.setImageBitmap(medication.getBitmap());
		}

		// view.setTag(medicationHourDosage.getMedication_schedule().getMedication());
		return view;
	}
}
