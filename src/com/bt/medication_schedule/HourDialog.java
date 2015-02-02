package com.bt.medication_schedule;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import org.joda.time.LocalTime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.MedicationSchedule;

public class HourDialog extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	public interface HourDialogListener {
		void onFinishEditHourDialog(TextView hours, LocalTime inputText);
	}

	String hour_str = "";
	String minute_str = "";
	TextView hours = null;
	Boolean already_exist = false;
	Integer once = 0;
	HourDialogListener activity;

	public void setHours(TextView hours) {
		this.hours = hours;
	}

	public HourDialog() {
		// Empty constructor required for DialogFragment
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

		if (hours == null)
			time_picker_dialog.setTitle("Add new hour");
		else
			time_picker_dialog.setTitle("Change hour : "
					+ hours.getText().toString());

		// Create a new instance of TimePickerDialog and return it
		return time_picker_dialog;
	}
	
	public void setFragmentParent(HourDialogListener activity){
		this.activity = activity;
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user

		

		once += 1;
		// if (view.is24HourView()){
		NumberFormat formatter = null;
		formatter = new DecimalFormat("#02");
		hour_str = formatter.format(hourOfDay);
		minute_str = formatter.format(minute);

		if (once == 1) {
			  //activity = (HourDialogListener) getActivity().getSupportFragmentManager().findFragmentByTag("unique_tag");

			MedicationSchedule medication_schedule = FeedMedicine.getInstance()
					.getMedication().getSchedule_medication();

			LocalTime hour = new LocalTime(hourOfDay,minute);
			if (!medication_schedule.isMedicationHourExist(hour))
				activity.onFinishEditHourDialog(this.hours,hour);
			else
				already_exist = true;

			if (already_exist) {
				Toast.makeText(getActivity(),
						"Vous avez deja selectionné cette heure",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/*
	 * @Override public boolean onEditorAction(TextView v, int actionId,
	 * KeyEvent event) { if (EditorInfo.IME_ACTION_DONE == actionId) { // Return
	 * input text to activity EditNameDialogListener activity =
	 * (EditNameDialogListener) getActivity();
	 * activity.onFinishEditDialog(mEditText.getText().toString());
	 * this.dismiss(); return true; } return false; }
	 */
}