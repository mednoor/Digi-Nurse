package com.bt.medication_listing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;
import com.bt.medication_listing.CustomMedicationListingArrayAdapter.ViewHolderListing;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_schedule.ScheduleMedicationActivity;

public class MedicationListing extends CommonActivity {

	ListView lvListingMedication;

	TextView textViewDateChoosed;

	CustomMedicationListingArrayAdapter adapterListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.medication_listing);

		lvListingMedication = (ListView) findViewById(R.id.listView_listing_medication);
		lvListingMedication.setOnItemClickListener(new ItemMedicationClick());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadListMedication();
	}

	public void loadListMedication() {

		ArrayList<Medication> listingMedication = FeedMedicine.getInstance()
				.getAllMedication(getApplicationContext());

		adapterListview = new CustomMedicationListingArrayAdapter(
				MedicationListing.this, listingMedication);
		lvListingMedication.setAdapter(adapterListview);

	}

	public class ItemMedicationClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Medication medication_selected = (Medication) parent
					.getItemAtPosition(position);

			Intent i = new Intent(getApplicationContext(),
					ScheduleMedicationActivity.class);
			medication_selected.setBitmap(null);
			i.putExtra("Medication", medication_selected);
			startActivity(i);

		}

	}

	@Override
	public int getTitleActionBar() {
		return R.string.drug;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_pillbox;
	}

	@Override
	public int getColorActionBar() {
		return R.color.drugBg;
	}

	@Override
	public boolean getHomeButtonActionBar() {
		return true;
	}

	@Override
	public boolean getUpNavigation() {
		return true;
	}

}
