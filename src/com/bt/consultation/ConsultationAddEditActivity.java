package com.bt.consultation;

import java.net.URI;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.consultation.HourAppointmentDialog.HourAppointmentDialogListener;
import com.bt.consultation_dao.AppointmentDao;
import com.bt.consultation_model.Appointment;
import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;
import com.bt.medication_model.Medication;
import com.bt.utils.Utils;

public class ConsultationAddEditActivity extends CommonActivity implements HourAppointmentDialogListener {

    DateTime date_selected;
    private final int PICK_CONTACT = 66;

    Button bt_list_contact_doctor;
    Button bt_new_contact_doctor;

    LinearLayout linearLayout_info_doctor;
    Button button_name_doctor;

    LinearLayout linearlayout_hour_appointment;
    TextView textView_hour_appointment;

    LinearLayout linearlayout_info_doctor;
    LinearLayout linearlayout_speciality;

    Spinner spinner_speciality;

    CustomConsultationSpecialityArrayAdapter adapterSpinner;

    EditText editText_comment_appointment;
    EditText editText_other_speciality;
    QuickContactBadge badgeDoctor;

    Boolean editionMode = false;

    Appointment appointment;
    String[] specialities;
    Integer id_speciality_other = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.consultation_add_edit);
	appointment = new Appointment();

	Bundle bundle = getIntent().getExtras();
	date_selected = (DateTime) bundle.get("date_selected");

	Appointment appointment_edit = (Appointment) getIntent().getSerializableExtra("Appointment");
	if (appointment_edit == null) {
	    appointment.setDateAppointment(new DateTime(date_selected));
	} else {
	    editionMode = true;
	    appointment = appointment_edit;
	}

	ActionBar actionBar = getActionBar();
	actionBar.setTitle(Utils.displayDate(appointment.getDateAppointment(), getApplicationContext()));

	editText_comment_appointment = (EditText) findViewById(R.id.editText_comment_appointment);
	editText_other_speciality = (EditText) findViewById(R.id.editText_other_speciality);

	linearLayout_info_doctor = (LinearLayout) findViewById(R.id.linearLayout_info_doctor);
	linearlayout_hour_appointment = (LinearLayout) findViewById(R.id.linearlayout_hour_appointment);
	linearlayout_info_doctor = (LinearLayout) findViewById(R.id.linearlayout_info_doctor);
	linearlayout_speciality = (LinearLayout) findViewById(R.id.linearlayout_speciality);
	linearlayout_hour_appointment.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showTimePickerDialog(v);

	    }
	});

	button_name_doctor = (Button) findViewById(R.id.button_name_doctor);
	button_name_doctor.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		badgeDoctor.callOnClick();

	    }
	});
	textView_hour_appointment = (TextView) findViewById(R.id.textView_hour_appointment);

	textView_hour_appointment.setText(Utils.displayHour(appointment.getHourAppointment(), getApplicationContext()));

	bt_list_contact_doctor = (Button) findViewById(R.id.button_search_doctor);
	bt_list_contact_doctor.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		goToContactPicker();
	    }
	});

	bt_new_contact_doctor = (Button) findViewById(R.id.button_add_doctor);

	bt_new_contact_doctor.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		goToAddContactPicker();
	    }
	});

	spinner_speciality = (Spinner) findViewById(R.id.spinner_speciality);
	badgeDoctor = (QuickContactBadge) findViewById(R.id.quickContactBadge1);
	badgeDoctor.setMode(ContactsContract.QuickContact.MODE_MEDIUM);
	loadSpecialitySpinner();
	loadComment();
	displayInfosDoctor();

    }

    public void loadComment() {
	editText_comment_appointment.setText(appointment.getComment());
    }

    public void loadBadgeDoctor() {
	Uri uri_contact_doctor = Uri.parse(appointment.getUriContactDdoctor());
	badgeDoctor.assignContactUri(uri_contact_doctor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu_appointment, menu);
	menu.findItem(R.id.appointment_add).setVisible(false);
	menu.findItem(R.id.appointment_edit).setVisible(false);
	menu.findItem(R.id.appointment_delete).setVisible(false);

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
	switch (item.getItemId()) {
	case R.id.appointment_save:
	    if (isFormValid())
		saveAppointement();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    public boolean isFormValid() {
	Boolean is_valid = true;
	if (appointment.getUriContactDdoctor().isEmpty()) {
	    is_valid = false;
	    linearlayout_info_doctor.setBackgroundResource(R.drawable.border_error);
	    Toast.makeText(getApplicationContext(), "Veuillez selectionner un médecin dans vos contacts",
		    Toast.LENGTH_SHORT).show();
	} else {
	    linearlayout_info_doctor.setBackgroundResource(R.drawable.medication_schedule_border);
	}

	if (appointment.getSpeciality().equals(id_speciality_other)
		&& editText_other_speciality.getText().toString().isEmpty()) {
	    is_valid = false;
	    linearlayout_speciality.setBackgroundResource(R.drawable.border_error);
	    Toast.makeText(getApplicationContext(), "Veuilley introduire le nom de la spécialité", Toast.LENGTH_SHORT)
		    .show();
	} else {
	    linearlayout_speciality.setBackgroundResource(R.drawable.medication_schedule_border);
	}

	return is_valid;
    }

    public void saveAppointement() {

	appointment.setComment(editText_comment_appointment.getText().toString());
	appointment.setOtherSpecialtity(editText_other_speciality.getText().toString());

	// inserting or updating appointment
	AppointmentDao appointmentDao = new AppointmentDao(getApplicationContext());
	appointmentDao.open();
	if (editionMode)
	    appointmentDao.update(appointment);
	else
	    appointmentDao.insertAppointment(appointment);
	appointmentDao.close();

	goToListAppointmentViewByDay();
    }

    public void loadSpecialitySpinner() {

	specialities = getResources().getStringArray(R.array.Speciality);

	adapterSpinner = new CustomConsultationSpecialityArrayAdapter(this, specialities);
	spinner_speciality.setAdapter(adapterSpinner);

	spinner_speciality.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		saveSpeciality(position);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {

	    }
	});
	
	if (editionMode) {
	    if (appointment.getSpeciality().equals(id_speciality_other))
		spinner_speciality.setSelection(specialities.length-1);
	    else
		spinner_speciality.setSelection(appointment.getSpeciality()-1);
	}

	
    }

    public void saveSpeciality(int position) {
	if (position == specialities.length - 1) {
	    appointment.setSpeciality(id_speciality_other);
	    editText_other_speciality.setText(appointment.getOtherSpecialtity());
	    editText_other_speciality.setVisibility(View.VISIBLE);
	} else {
	    appointment.setSpeciality(position + 1);// because 0 is other
	    appointment.setOtherSpecialtity("");
	    editText_other_speciality.setText("");
	    editText_other_speciality.setVisibility(View.GONE);
	}
    }

    public void showTimePickerDialog(View v) {
	HourAppointmentDialog hourAppointmentDialog = new HourAppointmentDialog();
	hourAppointmentDialog.setFragmentParent(this);
	hourAppointmentDialog.show(getSupportFragmentManager(), "timePicker");
    }

    public void goToAddContactPicker() {
	Intent intent = new Intent(Intent.ACTION_INSERT);
	intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
	startActivityForResult(intent, PICK_CONTACT);

    }

    public void goToContactPicker() {
	Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	startActivityForResult(intent, PICK_CONTACT);

    }

    public void goToListAppointmentViewByDay() {
	Intent intent = new Intent(ConsultationAddEditActivity.this, ConsultationViewByDayActivity.class);
	intent.putExtra("date_selected", appointment.getDateAppointment());
	startActivity(intent);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
	super.onActivityResult(reqCode, resultCode, data);

	switch (reqCode) {
	case (PICK_CONTACT):
	    if (resultCode == Activity.RESULT_OK) {
		Uri uri_contact_doctor = data.getData();
		appointment.setUriContactDoctor(uri_contact_doctor.toString());
		displayInfosDoctor();
	    }
	    break;
	}
    }

    public void displayInfosDoctor() {

	if (!appointment.getUriContactDdoctor().isEmpty()) {
	    Uri uri_contact_doctor = Uri.parse(appointment.getUriContactDdoctor());
	    Cursor c = getContentResolver().query(uri_contact_doctor, null, null, null, null);

	    if (c.moveToFirst()) {
		String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		button_name_doctor.setText(name);

		// long id =
		// c.getLong(c.getColumnIndex(ContactsContract.Contacts._ID));
		c.close();

		linearLayout_info_doctor.setVisibility(View.VISIBLE);
		badgeDoctor.setVisibility(View.VISIBLE);
		loadBadgeDoctor();
	    }
	}

    }

    /*
     * public void displayPostalAddress(long id_contact) { Cursor c =
     * getContentResolver().query( ContactsContract.Data.CONTENT_URI, new
     * String[] { StructuredPostal.STREET, StructuredPostal.CITY, // add more
     * coluns from StructuredPostal if // you need them StructuredPostal.POBOX
     * }, ContactsContract.Data.CONTACT_ID + "=? AND " +
     * StructuredPostal.MIMETYPE + "=?", new String[] {
     * String.valueOf(id_contact), StructuredPostal.CONTENT_ITEM_TYPE }, null);
     * 
     * boolean find_address = c.moveToFirst(); String street, pobox = "", city =
     * ""; if (find_address) { street =
     * c.getString(c.getColumnIndex(StructuredPostal.STREET)); pobox =
     * c.getString(c.getColumnIndex(StructuredPostal.POBOX)); city =
     * c.getString(c.getColumnIndex(StructuredPostal.CITY));
     * 
     * pobox = (pobox == null) ? "" : pobox; city = (city == null) ? "" : city;
     * 
     * } else { street = "Non spécifié"; }
     * 
     * linearLayout_info_doctor.setVisibility(View.VISIBLE);
     * textView_address_appointment.setText(street + "\n" + pobox + "\n" +
     * city);
     * 
     * }
     */

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

    @Override
    public void onFinishEditHourDialog(LocalTime hour) {

	appointment.setHourAppointment(hour);
	textView_hour_appointment.setText(Utils.displayHour(appointment.getHourAppointment(), getApplicationContext()));
    }

}
