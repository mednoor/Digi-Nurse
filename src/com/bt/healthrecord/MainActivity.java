package com.bt.healthrecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import com.bt.consultation.ConsultationCalendarActivity;
import com.bt.consultation.ConsultationDetailsActivity;
import com.bt.consultation_dao.AppointmentDao;
import com.bt.consultation_model.Appointment;
import com.bt.healthrecordDAO.ProfileDAO;
import com.bt.healthrecordMODEL.Profile;
import com.bt.medication_byday.MedicationToTake;
import com.bt.medication_model.FeedMedicine;
import com.bt.medication_model.Medication;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.utils.Utils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnClickListener {

    /* Declare buttons main activity */
    RelativeLayout profileBtn, consultationBtn, drugBtn, noteBtn;
    LinearLayout emergencyLytBtn;

    Profile profile;
    ViewFlipper viewFlipper1;
    List<ParseObject> adviceList1;
    TextView textViewAdvice;
    final Handler myHandler = new Handler();

    LinearLayout linearlayout_next_appointment;
    LinearLayout linearlayout_details_next_appointment;
    TextView textViewNoNextAppointment;
    TextView textViewDateNextAppointment;
    TextView textViewHourNextAppointment;
    TextView textViewNameSpecialityNextAppointment;
    ImageView imageViewIconSpeciality;
    Appointment next_appointment;

    LinearLayout linearlayout_next_medication_today;
    LinearLayout linearlayout_details_next_medication;
    TextView textViewNoNextMedication;
    TextView textViewNameNextMedication;
    TextView textViewHourNextMedication;
    TextView textViewDosageNextMedication;
    ImageView imageViewNextMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	/* Only Mode portrait for this activity */
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	/* Hide the action bar for this activity */
	ActionBar actionBar = getActionBar();
	actionBar.hide();

	/* Retrieve buttons and layout from layout */
	getButtons();

	/* Listener for buttons intent */
	profileBtn.setOnClickListener(this);
	consultationBtn.setOnClickListener(this);
	drugBtn.setOnClickListener(this);
	noteBtn.setOnClickListener(this);
	emergencyLytBtn.setOnClickListener(this);

	Parse.initialize(this, "NLrWUYzwmmsPKWeB7HVosDTdEZNT07IlNUFuPPtv", "YoCLXJIFQbVe1R7tgqT1uHFkCn9Np70wiJ7WB1QK");

	textViewAdvice = (TextView) findViewById(R.id.textViewAdvice);
	// Retrieving Objects
	showPreferences("advices");
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Advice");
	query.findInBackground(new FindCallback<ParseObject>() {
	    public void done(List<ParseObject> adviceList, ParseException e) {
		if (e == null) {
		    adviceList1 = adviceList;
		    SavePreferences("advices",adviceList1);
		    launchTimer();
		    /*
		     * for (ParseObject advice : adviceList) { String
		     * descriptionAdvice = advice.getString("description");
		     * Log.d("advice", "Retrieved " + descriptionAdvice);
		     * next_appointment.setText(descriptionAdvice); }
		     */
		} else {
		    Log.d("advice", "Error: " + e.getMessage());
		}
	    }
	});

	viewFlipper1 = (ViewFlipper) findViewById(R.id.viewFlipper1);
	viewFlipper1.setFlipInterval(7000);
	viewFlipper1.setInAnimation(this, R.anim.slide_in_left);
	viewFlipper1.setOutAnimation(this, R.anim.slide_out_right);
	viewFlipper1.startFlipping();

	// preview next appointment
	getRessourcesNextAppointement();
	loadNextAppointement();

	FeedMedicine.getInstance().prepareRepeatingAlamrs(this);

	LinearLayout linearlayout_next_medication_today = (LinearLayout) findViewById(R.id.linearlayout_next_medication_today);
	linearlayout_next_medication_today.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(MainActivity.this, MedicationToTake.class);
		startActivity(intent);

	    }
	});

	// preview next medication
	getRessourcesNextMedication();
	loadNextMedication();

    }

    private void SavePreferences(String key, List<ParseObject> list) {
	SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	SharedPreferences.Editor editor = sharedPreferences.edit();
	
	ArrayList<String> listDescription =new ArrayList<String>();
	for(ParseObject description:list){
	    listDescription.add(description.getString("description"));
	}
	Set<String> set = new HashSet<String>(listDescription);
	editor.putStringSet(key, set);
	editor.commit();
    }

    private List<String> showPreferences(String key) {
	
	SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	Long date_advices = sharedPreferences.getLong("date_advices", 0);
	/*DateMidnight today = DateMidnight.now().withZoneRetainFields(DateTimeZone.UTC);
	DateMidnight date_preference = DateMidnight.parse(today.toString(),
		DateTimeFormat.forPattern("yyyy-MM-dd"));
	
	Integer nbr_days = Days.daysBetween(today, date_preference).getDays();*/
	
	
	Set<String> set = null;
	set = sharedPreferences.getStringSet(key, set);
	List<String> list = null;
	if (set != null)
	    list = new ArrayList<String>(set);
	
	return list;
    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();

	loadNextAppointement();
	loadNextMedication();
    }

    private void getRessourcesNextAppointement() {
	linearlayout_next_appointment = (LinearLayout) findViewById(R.id.linearlayout_next_appointment);
	linearlayout_details_next_appointment = (LinearLayout) findViewById(R.id.linearlayout_details_next_appointment);
	textViewNoNextAppointment = (TextView) findViewById(R.id.textViewNoNextAppointment);
	textViewDateNextAppointment = (TextView) findViewById(R.id.textViewDateNextAppointment);
	textViewHourNextAppointment = (TextView) findViewById(R.id.textViewHourNextAppointment);
	textViewNameSpecialityNextAppointment = (TextView) findViewById(R.id.textViewNameSpecialityNextAppointment);
	imageViewIconSpeciality = (ImageView) findViewById(R.id.imageViewIconSpeciality);

    }

    private void loadNextAppointement() {

	AppointmentDao appointmentDao = new AppointmentDao(getApplicationContext());
	appointmentDao.open();
	next_appointment = appointmentDao.getNextAppointment();
	appointmentDao.close();

	if (next_appointment == null) {
	    linearlayout_details_next_appointment.setVisibility(View.GONE);
	    textViewNoNextAppointment.setVisibility(View.VISIBLE);
	} else {
	    textViewDateNextAppointment.setText(Utils.displayDate(next_appointment.getDateAppointment(), this));
	    textViewHourNextAppointment.setText(Utils.displayHour(next_appointment.getHourAppointment(), this));
	    textViewNameSpecialityNextAppointment.setText(next_appointment.getNameSpeciality(this));
	    imageViewIconSpeciality.setBackgroundResource(next_appointment.getIconSpeciality(this));
	    linearlayout_next_appointment.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    goToDetailsNextAppointment();
		}
	    });
	}
    }

    private void getRessourcesNextMedication() {
	linearlayout_next_medication_today = (LinearLayout) findViewById(R.id.linearlayout_next_medication_today);
	linearlayout_details_next_medication = (LinearLayout) findViewById(R.id.linearlayout_details_next_medication);
	textViewNoNextMedication = (TextView) findViewById(R.id.textViewNoNextMedication);
	textViewNameNextMedication = (TextView) findViewById(R.id.textViewNameNextMedication);
	textViewHourNextMedication = (TextView) findViewById(R.id.textViewHourNextMedication);
	textViewDosageNextMedication = (TextView) findViewById(R.id.textViewDosageNextMedication);
	imageViewNextMedication = (ImageView) findViewById(R.id.imageViewNextMedication);

    }

    private void loadNextMedication() {

	MedicationHourDosage medicationHourDosage = FeedMedicine.getInstance().getNextMedicationToday(
		getApplicationContext());

	if (medicationHourDosage == null) {
	    linearlayout_details_next_medication.setVisibility(View.GONE);
	    textViewNoNextMedication.setVisibility(View.VISIBLE);
	} else {
	    textViewNameNextMedication.setText(medicationHourDosage.getMedication_schedule().getMedication().getName());
	    textViewHourNextMedication.setText(Utils.displayHour(medicationHourDosage.getHour(), this));
	    textViewDosageNextMedication.setText(medicationHourDosage.getQuantity().toString());
	    medicationHourDosage.getMedication_schedule().getMedication().createBitmap();
	    if (medicationHourDosage.getMedication_schedule().getMedication().getBitmap() != null) {
		imageViewNextMedication.setImageBitmap(medicationHourDosage.getMedication_schedule().getMedication()
			.getBitmap());
	    }

	    linearlayout_next_medication_today.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    goToListMedication();
		}
	    });
	}
    }

    private void goToListMedication() {
	Intent intent = new Intent(this, MedicationToTake.class);
	startActivity(intent);
    }

    private void goToDetailsNextAppointment() {
	Intent intent = new Intent(this, ConsultationDetailsActivity.class);
	intent.putExtra("Appointment", next_appointment);
	startActivity(intent);
    }

    private void launchTimer() {
	Timer myTimer = new Timer();
	myTimer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		UpdateGUI();
	    }
	}, 0, 7000);
    }

    private void UpdateGUI() {
	// tv.setText(String.valueOf(i));
	myHandler.post(myRunnable);
    }

    final Runnable myRunnable = new Runnable() {
	public void run() {
	    displayAdvices();
	}
    };

    private void displayAdvices() {
	int random = (int) (Math.random() * adviceList1.size());
	String descriptionAdvice = adviceList1.get(random).getString("description");
	textViewAdvice.setText(descriptionAdvice);
    }

    private void getButtons() {
	/* Get buttons */
	profileBtn = (RelativeLayout) findViewById(R.id.profileBtn);
	consultationBtn = (RelativeLayout) findViewById(R.id.consultationBtn);
	drugBtn = (RelativeLayout) findViewById(R.id.drugBtn);
	noteBtn = (RelativeLayout) findViewById(R.id.noteBtn);

	/* Retrieve layout button from layout */
	emergencyLytBtn = (LinearLayout) findViewById(R.id.emergencyLayoutBtn);
    }

    @Override
    public void onClick(View v) {
	/* Start activity when we click on one of the buttons */
	switch (v.getId()) {

	case R.id.profileBtn:
	    /* Profile table */
	    ProfileDAO profileDAO = new ProfileDAO(this);
	    profileDAO.open();
	    profile = profileDAO.getInfoProfile();
	    profileDAO.close();

	    if (profile == null) {
		createProfile();
	    } else {
		goToProfile();
	    }
	    break;

	case R.id.consultationBtn:
	    goToconsultation();
	    break;

	case R.id.drugBtn:
	    goToDrug();
	    break;

	case R.id.noteBtn:
	    goToNote();
	    break;

	case R.id.emergencyLayoutBtn:
	    goToEmergency();
	    break;
	}
    }

    private void createProfile() {
	/* Go to add new profile activity */
	Intent intent = new Intent(this, ProfileAddNewActivity.class);
	startActivity(intent);
    }

    private void goToProfile() {
	/* Go to profile activity */
	Intent intent = new Intent(this, ProfileActivity.class);
	startActivity(intent);
	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void goToconsultation() {
	/* Go to consultation activity */
	Intent intent = new Intent(this, ConsultationCalendarActivity.class);
	startActivity(intent);
	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void goToDrug() {
	/* Go to drug activity */
	Intent intent = new Intent(this, MedicationToTake.class);
	startActivity(intent);
	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    private void goToNote() {
	/* Go to note activity */
	Intent intent = new Intent(this, NoteMainActivity.class);
	startActivity(intent);
	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void goToEmergency() {
	/* Go to emergency activity */
	Intent intent = new Intent(this, EmergencyActivity.class);
	startActivity(intent);
	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }
}
