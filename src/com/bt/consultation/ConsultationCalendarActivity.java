package com.bt.consultation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.bt.caldroid.CaldroidFragment;
import com.bt.caldroid.CaldroidListener;
import com.bt.consultation_dao.AppointmentDao;
import com.bt.consultation_model.Appointment;
import com.bt.healthrecord.CommonActivity;
import com.bt.healthrecord.R;

public class ConsultationCalendarActivity extends CommonActivity {

    private CaldroidSampleCustomFragment caldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.consultation_activity);

	caldroidFragment = new CaldroidSampleCustomFragment();

	Bundle args = new Bundle();
	Calendar cal = Calendar.getInstance();
	args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
	args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
	caldroidFragment.setArguments(args);

	FragmentTransaction t = getSupportFragmentManager().beginTransaction();
	t.replace(R.id.calendar1, caldroidFragment);
	t.commit();

	CaldroidListener listener = new CaldroidListener() {

	    @Override
	    public void onSelectDate(Date date, View view) {
		DateTime datetime_selected = new DateTime(date);
		LocalTime hour = new LocalTime();
		datetime_selected = datetime_selected.withTime(hour.getHourOfDay(), hour.getMinuteOfHour(),
			hour.getSecondOfMinute(), hour.getMillisOfSecond());
		AppointmentDao appointmentDao = new AppointmentDao(getApplicationContext());
		appointmentDao.open();
		ArrayList<Appointment> list_appointment_day = appointmentDao.getAllAppointmentByDay(datetime_selected);
		appointmentDao.close();

		Intent intent;
		if (list_appointment_day.isEmpty()) {
		    intent = new Intent(ConsultationCalendarActivity.this, ConsultationAddEditActivity.class);
		} else {
		    intent = new Intent(ConsultationCalendarActivity.this, ConsultationViewByDayActivity.class);
		}
		intent.putExtra("date_selected", datetime_selected);
		startActivity(intent);

	    }
	};

	caldroidFragment.setCaldroidListener(listener);

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
