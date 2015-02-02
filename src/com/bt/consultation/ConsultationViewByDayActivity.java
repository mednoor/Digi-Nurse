package com.bt.consultation;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import android.app.ActionBar;
import android.content.Intent;
import android.media.TimedText;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.bt.consultation_dao.AppointmentDao;
import com.bt.consultation_model.Appointment;
import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;
import com.bt.medication_listing.CustomMedicationListingArrayAdapter;
import com.bt.medication_listing.MedicationListing;
import com.bt.medication_listing.MedicationListing.ItemMedicationClick;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_schedule.ScheduleMedicationActivity;
import com.bt.utils.Utils;

public class ConsultationViewByDayActivity extends CommonActivity {
	
	DateTime date_selected;
	ListView listView_consultation_of_day;
	CustomAppointmentsListingArrayAdapter adapterListview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consultation_view_by_day);

		Bundle bundle = getIntent().getExtras();
		date_selected = (DateTime) bundle.get("date_selected");
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(Utils.displayDate(date_selected,getApplicationContext()));
		
		listView_consultation_of_day = (ListView) findViewById(R.id.listView_consultation_of_day);
		loadListAppointmentsByDay();
		
	}
	
	
	public void loadListAppointmentsByDay() {

	    	AppointmentDao appointmentDao = new AppointmentDao(getApplicationContext());
	    	appointmentDao.open();
		ArrayList<Appointment> listingApppointments =  appointmentDao.getAllAppointmentByDay(date_selected);
		appointmentDao.close();

		adapterListview = new CustomAppointmentsListingArrayAdapter(
			ConsultationViewByDayActivity.this, listingApppointments);
		listView_consultation_of_day.setAdapter(adapterListview);
		listView_consultation_of_day.setOnItemClickListener(new ItemAppointmentClick());
	}
	
	
	public class ItemAppointmentClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		    Appointment appointment_selected = (Appointment) parent
					.getItemAtPosition(position);

			Intent i = new Intent(getApplicationContext(),
					ConsultationDetailsActivity.class);
			i.putExtra("Appointment", appointment_selected);
			startActivity(i);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_appointment, menu);
		
		menu.findItem(R.id.appointment_save).setVisible(false);
		menu.findItem(R.id.appointment_edit).setVisible(false);
		menu.findItem(R.id.appointment_delete).setVisible(false);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.appointment_add:
			goToAddAppointment();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void goToAddAppointment() {
		Intent intent = new Intent(ConsultationViewByDayActivity.this,
				ConsultationAddEditActivity.class);
		intent.putExtra("date_selected", date_selected);
		startActivity(intent);
	}

	@Override
	public int getTitleActionBar() {
		return R.string.consultation;
	}

	@Override
	public int getIconActionBar() {
		return R.drawable.ic_consultation;
	}

	@Override
	public int getColorActionBar() {
		return R.color.consultationBg;
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
