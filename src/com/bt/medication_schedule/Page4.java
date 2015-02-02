package com.bt.medication_schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.joda.time.LocalTime;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bt.healthrecord.R;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.utils.Utils;

public class Page4 extends Fragment {

	// private HashMap<String, NumberPicker> hours_dosage;

	private HashMap<String, LinearLayout> hours_dosage;

	private HashMap<String, Integer> match_rb_food_instruction;

	private HashMap<LocalTime, NumberPicker> hours_dosage_value;

	RadioGroup radio_group_food_instructions;
	TextView free_text_instructions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment4, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		free_text_instructions = (TextView) getActivity().findViewById(
				R.id.free_text_instructions);

		hours_dosage = new HashMap<String, LinearLayout>();
		hours_dosage_value = new HashMap<LocalTime, NumberPicker>();

		radio_group_food_instructions = (RadioGroup) getActivity()
				.findViewById(R.id.radioGroup_food_instructions);

		match_rb_food_instruction = new HashMap<String, Integer>();
		match_rb_food_instruction.put("before_food", R.id.radio_before_food);
		match_rb_food_instruction.put("with_food", R.id.radio_with_food);
		match_rb_food_instruction.put("after_food", R.id.radio_after_food);
		match_rb_food_instruction.put("no_food_instructions",
				R.id.radio_no_food_instructions);

	}

	public void save() {

		int id_radio_button_checked = radio_group_food_instructions
				.getCheckedRadioButtonId();
		RadioButton radio_button_checked = (RadioButton) getActivity()
				.findViewById(id_radio_button_checked);

		Medication medication = FeedMedicine.getInstance().getMedication();
		medication
				.setFood_instruction(radio_button_checked.getTag().toString());
		medication.setInstruction_comment(free_text_instructions.getText()
				.toString());

		for (Entry<LocalTime, NumberPicker> entry : hours_dosage_value
				.entrySet()) {
			NumberPicker nb_picker = entry.getValue();
			LocalTime hour = entry.getKey();
			Double dosage = nb_picker.getValue();
			FeedMedicine.getInstance().getMedication().getSchedule_medication()
					.addMedicationHourDosage(hour, dosage);
		}

	}

	@Override
	public void setMenuVisibility(final boolean visible) {
		super.setMenuVisibility(visible);
		if (visible) {
			reloadData();
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadData();

	}

	private Integer getIdRadioButtonInstructions(String food_instruction) {
		return match_rb_food_instruction.get(food_instruction);
	}

	public void reloadData() {
		ArrayList<MedicationHourDosage> medication_hour_dosage_list = FeedMedicine
				.getInstance().getMedication().getSchedule_medication()
				.getMedication_hour_dosage_list();

		Iterator<MedicationHourDosage> it = medication_hour_dosage_list
				.iterator();
		LinearLayout layout_container = (LinearLayout) getActivity()
				.findViewById(R.id.linearlayout_container);
		if (layout_container != null) {
			layout_container.removeAllViews();
			hours_dosage_value.clear();

			// load radiobutton + comment instruction
			free_text_instructions.setText(FeedMedicine.getInstance()
					.getMedication().getInstruction_comment());
			radio_group_food_instructions
					.check(getIdRadioButtonInstructions(FeedMedicine
							.getInstance().getMedication()
							.getFood_instruction()));
			MedicationHourDosage medicationHourDosage;
			// récupération de l'itérateur
			while (it.hasNext()) {
				// itération de la liste
				medicationHourDosage = (MedicationHourDosage) it.next();

				NumberPicker picker;

				LinearLayout layout = new LinearLayout(getActivity());
				// Setting the orientation to vertical
				layout.setOrientation(LinearLayout.HORIZONTAL);
				// Creating a new TextView
				TextView tv = new TextView(getActivity());
				LocalTime hour_str = medicationHourDosage.getHour();
				tv.setText(Utils.displayHour(hour_str, getActivity()
						.getApplicationContext()));
				// tv.setBackgroundColor(0xFFFF00FF);
				tv.setTextColor(0xFF99CC00);
				tv.setTextSize(25);
				tv.setPadding(10, 10, 10, 10);
				tv.setTypeface(null, Typeface.BOLD);
				tv.setGravity(Gravity.CENTER_HORIZONTAL);

				// NumberPicker
				picker = new NumberPicker(getActivity(), null);
				picker.setValue(medicationHourDosage.getQuantity());
				layout.addView(tv);
				layout.addView(picker);
				layout_container.addView(layout);
				// picker.setValue(dosage_hours.get(hour_receive));
				hours_dosage_value.put(hour_str, picker);

				// récupération de l'objet se trouvant à l'index courant de
				// la
				// liste
			}

		}
	}

}
