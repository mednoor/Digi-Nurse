package com.bt.healthrecord;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class ProfileAddNewDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private TextView date;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		/* Use the current date as the default date in the picker */
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		

		/* Create a new instance of DatePickerDialog and return it */
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {

		/*
		 * Get the language of the phone
		 * Return the language code
		 */
		String languagePhone = Locale.getDefault().getLanguage();

		if (languagePhone.toString().equals("fr")) {
			// Return the data of the datePicker to the parent activity in French format
			String dateSelected = day+"/"+(month+1)+"/"+year;
			date.setText(dateSelected);
		} else {
			// Return the data of the datePicker to the parent activity in English format
			String dateSelected = (month+1)+"/"+day+"/"+year;
			date.setText(dateSelected);
		}
	}

	public TextView getDate() {
		return date;
	}

	public void setDate(TextView date) {
		this.date = date;
	}

}
