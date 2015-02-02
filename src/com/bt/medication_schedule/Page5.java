package com.bt.medication_schedule;

import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.LocalTime;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.utils.Utils;

public class Page5 extends Fragment {

	OnRecapSelectedListener mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment5, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		/*
		 * Button btn_save = (Button) getActivity().findViewById(
		 * R.id.save_medication); btn_save.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub saveScheduleMedication(); } });
		 */

		// MainActivity my = (MainActivity) getActivity();
		// my.getPage1().addObservateur(this);

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
		Log.d("tabulation", "reloadData");
		// if fragment loaded
		if (mCallback != null
				&& getActivity().findViewById(R.id.fragment_step5) != null) {
			Medication medication = FeedMedicine.getInstance().getMedication();

			// recap name and appareance
			LinearLayout layout_recap_name_appareance = (LinearLayout) getActivity()
					.findViewById(R.id.linearlayout_recap_name_appareance);
			layout_recap_name_appareance.removeAllViews();
			TextView tv_name = new TextView(getActivity());
			ImageView imageViewMedication = new ImageView(getActivity());
			LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT,
					20);
			imageViewMedication.setLayoutParams(lpView);
			imageViewMedication.getLayoutParams().height = 128;
			imageViewMedication.getLayoutParams().width = 128;
			tv_name.setText(medication.getName());
			if (medication.getPicture().isEmpty()) {// if no image, put image by
				// default question market
				imageViewMedication
						.setBackgroundResource(R.drawable.pill_default);
			} else
				imageViewMedication.setImageBitmap(medication.getBitmap());

			layout_recap_name_appareance.addView(tv_name);
			layout_recap_name_appareance.addView(imageViewMedication);

			// recap duration
			LinearLayout layout_recap_duration = (LinearLayout) getActivity()
					.findViewById(R.id.linearlayout_recap_duration);
			layout_recap_duration.removeAllViews();
			String date = Utils.displayDate(medication.getSchedule_medication()
					.getDate_start(), getActivity().getApplicationContext());
			TextView tv_date_start = new TextView(getActivity());
			TextView tv_duration = new TextView(getActivity());

			tv_date_start.setText(date);
			if (medication.getSchedule_medication().getIs_continuous()) {
				tv_duration.setText("Continious");
			} else {
				tv_duration.setText(medication.getSchedule_medication()
						.getNumber_of_days() + " jours");
			}
			layout_recap_duration.addView(tv_date_start);
			layout_recap_duration.addView(tv_duration);

			// recap time & days
			LinearLayout layout_recap_time_days = (LinearLayout) getActivity()
					.findViewById(R.id.linearlayout_recap_time_days);
			layout_recap_time_days.removeAllViews();

			TextView tv_frequency = new TextView(getActivity());
			TextView tv_list_hours = new TextView(getActivity());

			String frequency_str = "";
			if ((medication.getSchedule_medication().getMedication_frequency()
					.getIs_every_day())) {
				frequency_str = "Tout les jours";
			}
			if (medication.getSchedule_medication().getMedication_frequency()
					.getInterval_day() != 0) {
				String strMeatFormat = getResources().getString(
						R.string.every_x_days);
				frequency_str = String.format(strMeatFormat, medication
						.getSchedule_medication().getMedication_frequency()
						.getInterval_day());
			}
			if (!medication.getSchedule_medication().getMedication_frequency()
					.getDays_week().isEmpty()) {
				String day_of_week = medication.getSchedule_medication()
						.getMedication_frequency().getDays_week();
				frequency_str = Utils
						.displayDayWeek(getActivity(), day_of_week);
			}

			ArrayList<LocalTime> medication_hour_dosage_list = FeedMedicine
					.getInstance().getMedication().getSchedule_medication()
					.getListHours();

			ArrayList<String> list_hours_to_display = new ArrayList<String>();
			for (LocalTime hour_of_list : medication_hour_dosage_list) {
				list_hours_to_display.add(Utils.displayHour(hour_of_list,
						getActivity().getApplicationContext()));
			}

			tv_list_hours.setText(Utils.implode(", ", list_hours_to_display));
			tv_frequency.setText(frequency_str);
			layout_recap_time_days.addView(tv_frequency);
			layout_recap_time_days.addView(tv_list_hours);

			reloadSummaryInstructions();

			layout_recap_name_appareance
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mCallback.goToStep(Utils.FRAG_PHOTO);
						}
					});

			layout_recap_duration.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCallback.goToStep(Utils.FRAG_DURATION);
				}
			});
			layout_recap_time_days.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCallback.goToStep(Utils.FRAG_HOURS);
				}
			});

		}
	}

	public void reloadSummaryInstructions() {
		// recap instructions
		LinearLayout layout_recap_instructions = (LinearLayout) getActivity()
				.findViewById(R.id.linearlayout_recap_instructions);
		layout_recap_instructions.removeAllViews();

		// LinearLayout layout_container_hour_dosage = new
		// LinearLayout(getActivity());
		// layout_container_hour_dosage.setOrientation(LinearLayout.HORIZONTAL);
		// LinearLayout.LayoutParams params = new
		// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// layout_container_hour_dosage.setLayoutParams(params);

		ArrayList<MedicationHourDosage> medication_hour_dosage_list = FeedMedicine
				.getInstance().getMedication().getSchedule_medication()
				.getMedication_hour_dosage_list();

		Iterator<MedicationHourDosage> it = medication_hour_dosage_list
				.iterator();

		MedicationHourDosage medicationHourDosage = new MedicationHourDosage();
		// récupération de l'itérateur
		while (it.hasNext()) {

			medicationHourDosage = (MedicationHourDosage) it.next();

			LinearLayout layout = new LinearLayout(getActivity());
			// Setting the orientation to vertical
			layout.setOrientation(LinearLayout.HORIZONTAL);

			TextView tv_hour = new TextView(getActivity());
			TextView tv_dosage = new TextView(getActivity());

			LocalTime hour_str = medicationHourDosage.getHour();
			tv_hour.setText(Utils.displayHour(hour_str, getActivity()
					.getApplicationContext()));

			String dosage_str = medicationHourDosage.getQuantity() + "";
			tv_dosage.setText("(" + dosage_str + ")");
			tv_dosage.setTextColor(0xFF99CC00);

			layout.addView(tv_hour);
			layout.addView(tv_dosage);
			layout_recap_instructions.addView(layout);

		}

		TextView tv_food_instruction = new TextView(getActivity());
		tv_food_instruction.setText(Utils.getStringResourceByName(
				getActivity(), FeedMedicine.getInstance().getMedication()
						.getFood_instruction()));
		layout_recap_instructions.addView(tv_food_instruction);
		if (!FeedMedicine.getInstance().getMedication()
				.getInstruction_comment().isEmpty()) {
			TextView tv_comment_food_instruction = new TextView(getActivity());
			tv_comment_food_instruction.setText(FeedMedicine.getInstance()
					.getMedication().getInstruction_comment());
			layout_recap_instructions.addView(tv_comment_food_instruction);
		}

		LinearLayout layout_container_summary_instructions = (LinearLayout) getActivity()
				.findViewById(R.id.linearlayout_container_summary_instructions);
		layout_container_summary_instructions
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mCallback.goToStep(Utils.FRAG_QUANTITY);
					}
				});

	}

	// Container Activity must implement this interface
	public interface OnRecapSelectedListener {
		public void goToStep(int number_of_step);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnRecapSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnRecapSelectedListener");
		}
		reloadData();
	}

}
