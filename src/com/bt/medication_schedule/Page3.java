package com.bt.medication_schedule;

import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.LocalTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationFrequency;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.medication_model.MedicationSchedule;
import com.bt.medication_schedule.HourDialog.HourDialogListener;
import com.bt.utils.Utils;

public class Page3 extends Fragment implements HourDialogListener {

	private ArrayList<Integer> week_days_selected;
	Integer nbr_days_interval;

	RadioButton radiobtn_every_day;
	RadioButton radiobtn_choose_week_day;
	RadioButton radiobtn_days_interval;
	Button button_add_hours;
	TextView first_hour;
	HourDialog hourDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment3, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		week_days_selected = new ArrayList<Integer>();
		nbr_days_interval = Integer.valueOf(0);

		// FeedMedicine.getInstance().addHours("08:00");

		radiobtn_every_day = (RadioButton) getActivity().findViewById(
				R.id.radio0);
		radiobtn_choose_week_day = (RadioButton) getActivity().findViewById(
				R.id.radio1);
		radiobtn_days_interval = (RadioButton) getActivity().findViewById(
				R.id.radio2);
		button_add_hours = (Button) getActivity().findViewById(
				R.id.button_add_hours);

		first_hour = (TextView) getActivity().findViewById(R.id.first_hour);

		radiobtn_every_day.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				week_days_selected.clear();
				nbr_days_interval = 0;
			}
		});

		first_hour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFirstHour((TextView) v);
			}

		});

		button_add_hours.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addHours();
			}
		});

		handleBlockDays();

	}

	public void addFirstHour(TextView fisrt_hour) {
		HourDialog hourDialog = new HourDialog();
		hourDialog.setHours(fisrt_hour);
		hourDialog.setFragmentParent((HourDialogListener) this);
		hourDialog
				.show(getActivity().getSupportFragmentManager(), "timePicker");
	}

	private void handleBlockDays() {

		OnClickListener radiobtn_listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				RadioButton rb = (RadioButton) v;
				manageRadioGroup(rb.getId());

			}
		};
		radiobtn_choose_week_day.setOnClickListener(radiobtn_listener);
		radiobtn_days_interval.setOnClickListener(radiobtn_listener);
	}

	private void manageRadioGroup(int id_check_box) {
		switch (id_check_box) {
		case R.id.radio1:
			showSetWeekDays();
			break;
		case R.id.radio2:
			showDaysInterval();
			break;
		}
	}

	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// START DIALOG DAYS INTERVAL
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************

	public void showDaysInterval() {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				getActivity());

		// Setting Dialog Title
		alertDialog2.setTitle("Choisissez le nombre de jours entre les doses");

		// Setting Icon to Dialog
		alertDialog2.setIcon(R.drawable.ic_launcher);

		final View view = getActivity().getLayoutInflater().inflate(
				R.layout.days_interval, null);
		alertDialog2.setView(view);

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog
						NumberPicker number_picker_days_interval = (NumberPicker) (view
								.findViewById(R.id.picker_days_interval));
						nbr_days_interval = number_picker_days_interval
								.getValue().intValue();

						week_days_selected.clear();

					}
				});
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog
						nbr_days_interval = 0;
						dialog.cancel();
					}
				});

		// Showing Alert Dialog
		alertDialog2.show();

	}

	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// END DIALOG DAYS INTERVAL
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************

	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// START DIALOG DAYS WEEK
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************

	public void showSetWeekDays() {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				getActivity());

		// Setting Dialog Title
		alertDialog2.setTitle("Set");

		// Setting Icon to Dialog
		alertDialog2.setIcon(R.drawable.ic_launcher);

		final View view = getActivity().getLayoutInflater().inflate(
				R.layout.week_day, null);
		alertDialog2.setView(view);

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("Picks Days",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog

						Integer[] id_checkbox_week = new Integer[] {
								R.id.CheckBox_sun, R.id.CheckBox_mon,
								R.id.CheckBox_tue, R.id.CheckBox_wed,
								R.id.CheckBox_thu, R.id.CheckBox_fri,
								R.id.CheckBox_sat };
						Integer[] code_week = new Integer[] { 7, 1, 2, 3, 4, 5,
								6 };
						for (int i = 0; i < id_checkbox_week.length; i++) {
							CheckBox chekbox = (CheckBox) (view
									.findViewById(id_checkbox_week[i]));
							if (chekbox.isChecked())
								week_days_selected.add(code_week[i]);
						}

					}
				});
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog

						dialog.cancel();
					}
				});

		// Showing Alert Dialog
		alertDialog2.show();

	}

	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// END DIALOG DAYS WEEK
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************

	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// START HANDLE HOURS
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************

	public void addHours() {
		hourDialog = new HourDialog();
		hourDialog.setFragmentParent(this);
		hourDialog
				.show(getActivity().getSupportFragmentManager(), "timePicker");

	}

	public void addHoursLayout(TextView hours, LocalTime heure) {

		LinearLayout layout_container = (LinearLayout) getActivity()
				.findViewById(R.id.linearlayout_hours);

		MedicationSchedule medication_schedule = FeedMedicine.getInstance()
				.getMedication().getSchedule_medication();

		if (hours == null) {

			hours = new TextView(getActivity());
			hours.setTextSize(40);
			hours.setTypeface(null, Typeface.BOLD_ITALIC);
			hours.setGravity(Gravity.CENTER);
			hours.setPadding(10, 10, 20, 10);
			hours.setTag(heure);

			LinearLayout linear_layout_line_hours = new LinearLayout(
					getActivity());
			linear_layout_line_hours.setOrientation(LinearLayout.HORIZONTAL);

			TextView btn_delete = new TextView(getActivity());
			btn_delete.setBackgroundResource(R.drawable.delete);
			btn_delete.setTag(heure);
			LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			btn_delete.setLayoutParams(lpView);
			btn_delete.setGravity(Gravity.CENTER_VERTICAL);

			linear_layout_line_hours.addView(hours);
			linear_layout_line_hours.addView(btn_delete);

			layout_container.addView(linear_layout_line_hours);
			btn_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) v;
					LocalTime hours_to_delete = (LocalTime) tv.getTag();
					LinearLayout container = ((LinearLayout) v.getParent());
					FeedMedicine.getInstance().getMedication()
							.getSchedule_medication()
							.deleteMedicationHour(hours_to_delete);
					container.removeAllViews();
				}
			});

			medication_schedule.addMedicationHour(heure);

		} else {
			medication_schedule.addMedicationHour(heure);
			// delete hour to replace
			medication_schedule
					.deleteMedicationHour((LocalTime) hours.getTag());
		}
		hours.setText(Utils.displayHour(heure, getActivity()
				.getApplicationContext()));
		hours.setTag(heure);

		hours.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFirstHour((TextView) v);

			}
		});
	}

	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// END HANDLE WITH HOURS
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************
	// ************************************************************************************************************************************************

	public void save() {
		Medication medication = FeedMedicine.getInstance().getMedication();
		MedicationFrequency medicationFequency = medication
				.getSchedule_medication().getMedication_frequency();

		if (medicationFequency == null) {
			medicationFequency = new MedicationFrequency();
			medication.getSchedule_medication().setMedication_frequency(
					medicationFequency);
		}

		if (radiobtn_every_day.isChecked() || week_days_selected.size() == 6
				|| nbr_days_interval == 1)
			medicationFequency.setIs_every_day(true);
		if (week_days_selected.size() != 0) {
			medicationFequency.setDays_week(Utils.implode(",",
					week_days_selected));
		}
		if (nbr_days_interval != 0) {
			medicationFequency.setInterval_day(nbr_days_interval);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reloadData();
	}

	@Override
	public void setMenuVisibility(final boolean visible) {
		super.setMenuVisibility(visible);
		if (visible) {
			reloadData();
		}
	}

	public void reloadData() {
		// if fragment loaded
		if (getActivity() != null
				&& getActivity().findViewById(R.id.fragment_step3) != null) {
			Medication medication = FeedMedicine.getInstance().getMedication();
			if (medication != null) {
				MedicationFrequency medicationFequency = medication
						.getSchedule_medication().getMedication_frequency();
				if (medicationFequency != null) {
					if (medicationFequency.getIs_every_day())
						radiobtn_every_day.setChecked(true);
					if (medicationFequency.getInterval_day() != 0)
						radiobtn_days_interval.setChecked(true);
					if (!medicationFequency.getDays_week().isEmpty())
						radiobtn_choose_week_day.setChecked(true);

				}
			}

			ArrayList<MedicationHourDosage> medication_hour_dosage_list = FeedMedicine
					.getInstance().getMedication().getSchedule_medication()
					.getMedication_hour_dosage_list();

			if (!medication_hour_dosage_list.isEmpty()) {
				MedicationHourDosage medicationHourDosage = medication_hour_dosage_list
						.get(0);
				first_hour.setText(Utils.displayHour(medicationHourDosage
						.getHour(), getActivity().getApplicationContext()));
				first_hour.setTag(medicationHourDosage.getHour());
				Iterator it = medication_hour_dosage_list.iterator();
				it.next();
				// récupération de l'itérateur
				LinearLayout layout_container = (LinearLayout) getActivity()
						.findViewById(R.id.linearlayout_hours);
				// if more than 1 hour displayed
				layout_container.removeViewsInLayout(1,
						layout_container.getChildCount() - 1);
				while (it.hasNext()) {
					// itération de la liste
					medicationHourDosage = (MedicationHourDosage) it.next();
					addHoursLayout(null, medicationHourDosage.getHour());
					// récupération de l'objet se trouvant à l'index courant de
					// la
					// liste
				}
			} else { // the first time there is 08:00 displayed
				LocalTime first_hour_by_default = new LocalTime(8, 0);
				first_hour.setText(Utils.displayHour(first_hour_by_default,
						getActivity().getApplicationContext()));
				first_hour.setTag(first_hour_by_default);
				FeedMedicine.getInstance().getMedication()
						.getSchedule_medication()
						.addMedicationHour(first_hour_by_default);
			}
		}
	}

	@Override
	public void onFinishEditHourDialog(TextView hours, LocalTime inputText) {
		// TODO Auto-generated method stub
		addHoursLayout(hours, inputText);
	}

}
