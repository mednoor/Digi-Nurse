package com.bt.consultation;

import java.util.Calendar;

import org.joda.time.LocalTime;

import com.bt.medication_schedule.HourDialog.HourDialogListener;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class HourAppointmentDialog extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	
	
	HourAppointmentDialogListener activity;

	public interface HourAppointmentDialogListener {
		void onFinishEditHourDialog(LocalTime hour);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		// mEditText = (EditText)
		// getActivity().findViewById(R.id.txt_your_name);

		// Create a new instance of TimePickerDialog and return it
		TimePickerDialog time_picker_dialog = new TimePickerDialog(
				getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));

		// Create a new instance of TimePickerDialog and return it
		return time_picker_dialog;
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user

		LocalTime hour = new LocalTime(hourOfDay, minute);

		activity.onFinishEditHourDialog( hour);

	}
	
	public void setFragmentParent(HourAppointmentDialogListener activity){
		this.activity = activity;
	}

}