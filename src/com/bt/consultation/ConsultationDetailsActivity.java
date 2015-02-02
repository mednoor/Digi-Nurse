package com.bt.consultation;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.bt.consultation_dao.AppointmentDao;
import com.bt.consultation_model.Appointment;
import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;
import com.bt.utils.Utils;

public class ConsultationDetailsActivity extends CommonActivity {

    TextView textView_appointment_when;
    TextView textView_appointment_at;
    TextView textView_appointment_speciality;
    TextView textView_appointment_notes;
    Button button_appointment_whith;

    ImageView imageView_appointment_speciality;
    QuickContactBadge quickContactBadge_appointment_whith;

    Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.consultation_details);

	appointment = (Appointment) getIntent().getSerializableExtra("Appointment");

	getRessources();
	intiElementsActivity();
	initListeners();

    }

    public void getRessources() {
	textView_appointment_when = (TextView) findViewById(R.id.textView_appointment_when);
	textView_appointment_at = (TextView) findViewById(R.id.textView_appointment_at);
	textView_appointment_speciality = (TextView) findViewById(R.id.textView_appointment_speciality);
	textView_appointment_notes = (TextView) findViewById(R.id.textView_appointment_notes);
	button_appointment_whith = (Button) findViewById(R.id.button_appointment_whith);

	imageView_appointment_speciality = (ImageView) findViewById(R.id.imageView_appointment_speciality);
	quickContactBadge_appointment_whith = (QuickContactBadge) findViewById(R.id.quickContactBadge_appointment_whith);
    }

    public void intiElementsActivity() {

	textView_appointment_when.setText(Utils.displayDate(appointment.getDateAppointment(), getApplicationContext()));
	textView_appointment_at.setText(Utils.displayHour(appointment.getHourAppointment(), getApplicationContext()));
	textView_appointment_speciality.setText(appointment.getNameSpeciality(getApplicationContext()));
	textView_appointment_notes.setText(appointment.getComment());
	initInfosDoctor();
	imageView_appointment_speciality.setBackgroundResource(appointment.getIconSpeciality(getApplicationContext()));

    }

    public void initInfosDoctor() {

	Uri uri_contact_doctor = Uri.parse(appointment.getUriContactDdoctor());
	Cursor c = getContentResolver().query(uri_contact_doctor, null, null, null, null);

	if (c.moveToFirst()) {
	    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	    button_appointment_whith.setText(name);
	    c.close();
	    initBadgeDoctor();
	}
    }

    public void initBadgeDoctor() {
	Uri uri_contact_doctor = Uri.parse(appointment.getUriContactDdoctor());
	quickContactBadge_appointment_whith.assignContactUri(uri_contact_doctor);
    }

    public void initListeners() {
	button_appointment_whith.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		quickContactBadge_appointment_whith.callOnClick();

	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu_appointment, menu);
	menu.findItem(R.id.appointment_add).setVisible(false);
	menu.findItem(R.id.appointment_save).setVisible(false);

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
	switch (item.getItemId()) {
	case R.id.appointment_edit:
	    goToEditAppointment();
	    return true;
	case R.id.appointment_delete:
	    createDialogDeleteAppointment();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    public void createDialogDeleteAppointment() {
	// Create out AlterDialog
	Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Are you sure you want to delete?");
	builder.setTitle("Deleting appointment");
	builder.setCancelable(true);
	builder.setPositiveButton("Yes", new OkDeletingOnClickListener());
	builder.setNegativeButton("No", new CancelDeletingOnClickListener());
	AlertDialog dialog = builder.create();
	dialog.show();
    }

    private final class CancelDeletingOnClickListener implements DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
	    dialog.dismiss();
	}
    }

    private final class OkDeletingOnClickListener implements DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
	    deleteAppointment();
	}
    }

    public void deleteAppointment() {
	AppointmentDao appointmentDao = new AppointmentDao(getApplicationContext());
	appointmentDao.open();
	appointmentDao.delete(appointment);
	appointmentDao.close();
	goToListAppointmentDay();

    }

    public void goToListAppointmentDay() {
	Intent intent = new Intent(ConsultationDetailsActivity.this, ConsultationViewByDayActivity.class);
	intent.putExtra("date_selected", appointment.getDateAppointment());
	startActivity(intent);
    }

    public void goToEditAppointment() {
	Intent intent = new Intent(ConsultationDetailsActivity.this, ConsultationAddEditActivity.class);
	intent.putExtra("Appointment", appointment);
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
