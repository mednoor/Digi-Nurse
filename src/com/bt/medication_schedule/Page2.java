package com.bt.medication_schedule;

import java.util.Calendar;

import org.joda.time.DateMidnight;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationSchedule;
import com.bt.utils.Utils;

public class Page2 extends Fragment {

	static TextView date_start_text;
	LinearLayout container_number_days;
	LinearLayout linearLayoutDateStart;
	CheckBox check;
	NumberPicker number_days;
	static DateMidnight date_selected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment2, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		date_start_text = (TextView) getActivity().findViewById(
				R.id.date_start_step1);
		date_selected = new DateMidnight();

		number_days = (NumberPicker) getActivity().findViewById(
				R.id.number_days);

		container_number_days = (LinearLayout) getActivity().findViewById(
				R.id.container_number_days);
		linearLayoutDateStart= (LinearLayout) getActivity().findViewById(
			R.id.linearLayoutDateStart);
		container_number_days.setVisibility(View.GONE);

		check = (CheckBox) getActivity().findViewById(R.id.checkBox1);

		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					container_number_days.setVisibility(View.GONE);
				} else {
					container_number_days.setVisibility(View.VISIBLE);
				}
			}
		});

		linearLayoutDateStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(v);
			}
		});
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getActivity().getSupportFragmentManager(),
				"datePicker");
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			date_selected = new DateMidnight(year, month + 1, day);
			String date = Utils.displayDate(date_selected, getActivity()
					.getApplicationContext());

			date_start_text.setText(date);
		}
	}

	public void save() {
		// TODO Auto-generated method stub
	    getActivity();
		Medication medication = FeedMedicine.getInstance().getMedication();
		MedicationSchedule medicationSchedule = medication
				.getSchedule_medication();

		medicationSchedule.setMedication(medication);

		medicationSchedule.setDate_start(date_selected);
		medicationSchedule.setIs_continuous(check.isChecked());
		medicationSchedule.setNumber_of_days(number_days.getValue().intValue());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Medication medication = FeedMedicine.getInstance().getMedication();
		if (medication != null) {
			MedicationSchedule medicationSchedule = medication
					.getSchedule_medication();
			if (medicationSchedule != null) {
				String date = Utils
						.displayDate(medicationSchedule.getDate_start(),
								getActivity().getApplicationContext());
				date_start_text.setText(date);
				check.setChecked(medicationSchedule.getIs_continuous());
				number_days.setValue(medicationSchedule.getNumber_of_days()
						.doubleValue());
			}
		}
	}

}
