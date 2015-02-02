package com.bt.medication_byday;

import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.LocalTime;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.healthrecord.R;
import com.bt.medication_dao.MedicationTakenDao;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.medication_model.MedicationTaken;
import com.bt.stickylistheaders.StickyListHeadersAdapter;
import com.bt.utils.Utils;

public class CustomMedicationDayArrayAdapter extends ArrayAdapter<MedicationHourDosage> implements
	StickyListHeadersAdapter {

    private final List<MedicationHourDosage> list;
    private final Activity context;
    private LayoutInflater inflater;
    private DateMidnight date;

    public CustomMedicationDayArrayAdapter(Activity context, List<MedicationHourDosage> list, DateMidnight date) {
	super(context, R.layout.medication_day_custom_row, list);
	this.context = context;
	this.list = list;
	this.date = date;
	inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
	protected TextView text_name;
	protected TextView text_hour;
	protected TextView text_food_instruction;
	protected TextView text_dosage;
	protected TextView text_comment_instruction;
	protected CheckBox taken;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	View view = null;
	MedicationHourDosage medicationHourDosage = list.get(position);

	if (convertView == null) {
	    LayoutInflater inflator = context.getLayoutInflater();
	    view = inflator.inflate(R.layout.medication_day_custom_row, null);
	    final ViewHolder viewHolder = new ViewHolder();
	    viewHolder.text_name = (TextView) view.findViewById(R.id.textView_name_of_medication);
	    viewHolder.text_hour = (TextView) view.findViewById(R.id.textView_hour_taken);
	    viewHolder.text_food_instruction = (TextView) view.findViewById(R.id.textView_food_instruction);
	    viewHolder.text_dosage = (TextView) view.findViewById(R.id.textView_dosage);
	    viewHolder.text_comment_instruction = (TextView) view.findViewById(R.id.textView_comment_instruction);
	    viewHolder.taken = (CheckBox) view.findViewById(R.id.taken);

	    view.setTag(viewHolder);
	} else {
	    view = convertView;
	}

	ViewHolder holder = (ViewHolder) view.getTag();

	String display_hour = Utils.displayHour(medicationHourDosage.getHour(), context);

	holder.text_name.setText(medicationHourDosage.getMedication_schedule().getMedication().getName());
	holder.text_hour.setText(display_hour);
	holder.text_dosage.setText(medicationHourDosage.getQuantity().toString());
	holder.text_comment_instruction.setText(medicationHourDosage.getMedication_schedule().getMedication()
		.getInstruction_comment());
	String message_food_instruction = Utils.getStringResourceByName(context, medicationHourDosage
		.getMedication_schedule().getMedication().getFood_instruction()
		+ "_short");
	holder.text_food_instruction.setText(message_food_instruction);

	holder.taken.setTag(medicationHourDosage);
	holder.taken.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	        final boolean isChecked = ((CompoundButton) v).isChecked();
	        
		MedicationHourDosage medicationHourDosage =  (MedicationHourDosage) v.getTag();
		MedicationTakenDao medicationTakenDao = new MedicationTakenDao(context);
		medicationTakenDao.open();
		MedicationTaken medicationTaken = new MedicationTaken(date, isChecked, medicationHourDosage);
		medicationTakenDao.insertMedicationTaken(medicationTaken);
		medicationTakenDao.close();	        
	        // Do something here.
	    }
	});
	
	MedicationTakenDao medicationTakenDao = new MedicationTakenDao(context);
	medicationTakenDao.open();
	MedicationTaken medicationTaken = medicationTakenDao.getMedicationTaken(medicationHourDosage, date);
	medicationTakenDao.close();

	if (medicationTaken != null){
	    holder.taken.setChecked(medicationTaken.getTaken());
	    Log.d("toto",medicationHourDosage.getHour().toString());
	}else{
	    holder.taken.setChecked(false);
	}

	// view.setTag(medicationHourDosage.getMedication_schedule().getMedication());
	return view;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

	View view = null;
	MedicationHourDosage medicationHourDosage = list.get(position);
	if (convertView == null) {
	    LayoutInflater inflator = context.getLayoutInflater();
	    view = inflator.inflate(R.layout.header, null);
	    final HeaderViewHolder holder = new HeaderViewHolder();
	    holder.textView = (TextView) view.findViewById(R.id.header_text);
	    holder.imageView = (ImageView) view.findViewById(R.id.header_imageView);
	    view.setTag(holder);
	} else {
	    view = convertView;
	}
	HeaderViewHolder holder = (HeaderViewHolder) view.getTag();
	switch (dispatchHour(medicationHourDosage)) {
	case Utils.MATIN:
	    holder.imageView.setImageResource(R.drawable.morning);
	    holder.textView.setText("Matin");
	    break;
	case Utils.MIDDAY:
	    holder.imageView.setImageResource(R.drawable.midday);
	    holder.textView.setText("Midi");
	    break;
	case Utils.EVENING:
	    holder.imageView.setImageResource(R.drawable.evening);
	    holder.textView.setText("Soir");
	    break;
	default:
	    break;
	}
	return view;
    }

    private Integer dispatchHour(MedicationHourDosage medicationHourDosage) {
	LocalTime hour_morning_start = new LocalTime(0, 0);
	LocalTime hour_morning_end = new LocalTime(12, 0);

	LocalTime hour_midday_start = new LocalTime(12, 0);
	LocalTime hour_midday_end = new LocalTime(19, 0);

	LocalTime hour_evening_start = new LocalTime(19, 0);
	LocalTime hour_evening_end = new LocalTime(23, 59);

	if (medicationHourDosage.getHour().compareTo(hour_morning_start) >= 0
		&& medicationHourDosage.getHour().compareTo(hour_morning_end) <= 0) {
	    Log.d("dispatch", "dispatch ==> MATIN");
	    return Utils.MATIN;
	}
	if (medicationHourDosage.getHour().compareTo(hour_midday_start) >= 0
		&& medicationHourDosage.getHour().compareTo(hour_midday_end) <= 0) {
	    Log.d("dispatch", "dispatch ==> MIDI");
	    return Utils.MIDDAY;
	}
	/*
	 * if (medicationHourDosage.getHour().compareTo(hour_evening_start) >= 0
	 * && medicationHourDosage.getHour().compareTo(hour_evening_end) <= 0) {
	 * return EVENING; }
	 */
	Log.d("dispatch", "dispatch ==> SOIR");
	return Utils.EVENING;
    }

    class HeaderViewHolder {
	protected TextView textView;
	protected ImageView imageView;
    }

    @Override
    public long getHeaderId(int position) {
	// return the first character of the country as ID because this is what
	// headers are based upon
	return dispatchHour(list.get(position));
    }

}
