package com.bt.medication_byday;

import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateMidnight;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bt.healthrecord.R;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.medication_schedule.ScheduleMedicationActivity;
import com.bt.stickylistheaders.StickyListHeadersListView;

public class PageFragment extends Fragment {
    StickyListHeadersListView stickyList;
    CustomMedicationDayArrayAdapter adapterListview;

    public static PageFragment newInstance(DateMidnight date_choosed) {

	PageFragment pageFragment = new PageFragment();
	Bundle bundle = new Bundle();
	bundle.putSerializable("date_choosed", date_choosed);
	pageFragment.setArguments(bundle);
	return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	View view = inflater.inflate(R.layout.medication_by_day, container, false);
	stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);

	return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	loadListMedication();

    }

    public void loadListMedication() {

	DateMidnight date = (DateMidnight) getArguments().get("date_choosed");
	ArrayList<Medication> listMedicationToTaken = FeedMedicine.getInstance().getMedicationForDay(date,
		getActivity());

	ArrayList<MedicationHourDosage> listAllMedicationHourDosage = new ArrayList<MedicationHourDosage>();
	for (Medication medication : listMedicationToTaken) {
	    for (MedicationHourDosage medicationHourDosage : medication.getSchedule_medication()
		    .getMedication_hour_dosage_list()) {

		listAllMedicationHourDosage.add(medicationHourDosage);

	    }

	}

	stickyList.setOnItemClickListener(new ItemMedicationClick());
	Collections.sort(listAllMedicationHourDosage);
	adapterListview = new CustomMedicationDayArrayAdapter(getActivity(), listAllMedicationHourDosage, date);
	stickyList.setAdapter(adapterListview);

    }

    public class ItemMedicationClick implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	    MedicationHourDosage medicationHourDosage = (MedicationHourDosage) parent.getItemAtPosition(position);

	    Medication medication_selected = medicationHourDosage.getMedication_schedule().getMedication();

	    Intent i = new Intent(getActivity(), ScheduleMedicationActivity.class);
	    i.putExtra("Medication", medication_selected);
	    startActivity(i);

	}

    }

}