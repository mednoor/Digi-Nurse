package com.bt.consultation;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;

import com.bt.caldroid.CaldroidFragment;
import com.bt.caldroid.CaldroidGridAdapter;
import com.bt.caldroid.CalendarHelper;
import com.bt.consultation_dao.AppointmentDao;
import com.bt.consultation_model.Appointment;
import com.bt.healthrecord.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

    ArrayList<Appointment> list_appointments;

    public CaldroidSampleCustomAdapter(Context context, int month, int year, HashMap<String, Object> caldroidData,
	    HashMap<String, Object> extraData) {
	super(context, month, year, caldroidData, extraData);

	retrieveListAppointmentsForMonth();
    }

    public void setAdapterDateTime(DateTime dateTime) {
	this.month = dateTime.getMonthOfYear();
	this.year = dateTime.getYear();
	this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year, startDayOfWeek);
	retrieveListAppointmentsForMonth();
    }

    public void retrieveListAppointmentsForMonth() {
	AppointmentDao appointmentDao = new AppointmentDao(context);
	appointmentDao.open();
	list_appointments = appointmentDao.getAllAppointmentByMonth(this.month, this.year);
	appointmentDao.close();
    }

    public ArrayList<Appointment> filterListAppointmentsForDay(DateTime date) {
	Appointment appointment_searched = new Appointment();
	appointment_searched.setDateAppointment(date);
	ArrayList<Appointment> list_appointments_day = new ArrayList<Appointment>();
	int indice_appointment;
	while (list_appointments.indexOf(appointment_searched) != -1) {
	    indice_appointment = list_appointments.indexOf(appointment_searched);
	    list_appointments_day.add(list_appointments.get(indice_appointment));
	    list_appointments.remove(indice_appointment);
	}
	return list_appointments_day;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View cellView = convertView;

	// For reuse
	if (convertView == null) {
	    cellView = inflater.inflate(R.layout.consultation_calendar_custom_cell, null);
	}

	int topPadding = cellView.getPaddingTop();
	int leftPadding = cellView.getPaddingLeft();
	int bottomPadding = cellView.getPaddingBottom();
	int rightPadding = cellView.getPaddingRight();

	TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);

	tv1.setTextColor(Color.BLACK);

	// Get dateTime of this cell
	DateTime dateTime = this.datetimeList.get(position);
	Resources resources = context.getResources();

	// Set color of the dates in previous / next month
	if (dateTime.getMonthOfYear() != month) {
	    tv1.setTextColor(resources.getColor(R.color.caldroid_darker_gray));
	}

	boolean shouldResetDiabledView = false;
	boolean shouldResetSelectedView = false;

	// Customize for disabled dates and date outside min/max dates
	if ((minDateTime != null && dateTime.isBefore(minDateTime))
		|| (maxDateTime != null && dateTime.isAfter(maxDateTime))
		|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

	    tv1.setTextColor(CaldroidFragment.disabledTextColor);
	    if (CaldroidFragment.disabledBackgroundDrawable == -1) {
		cellView.setBackgroundResource(R.drawable.disable_cell);
	    } else {
		cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
	    }

	    if (dateTime.equals(getToday())) {
		cellView.setBackgroundResource(R.drawable.red_border_gray_bg);
	    }

	} else {
	    shouldResetDiabledView = true;
	}

	// Customize for selected dates
	if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
	    if (CaldroidFragment.selectedBackgroundDrawable != -1) {
		cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
	    } else {
		cellView.setBackgroundColor(resources.getColor(R.color.caldroid_sky_blue));
	    }

	    tv1.setTextColor(CaldroidFragment.selectedTextColor);

	} else {
	    shouldResetSelectedView = true;
	}

	if (shouldResetDiabledView && shouldResetSelectedView) {
	    // Customize for today
	    if (dateTime.equals(getToday())) {
		cellView.setBackgroundResource(R.drawable.red_border);
	    } else {
		cellView.setBackgroundResource(R.drawable.cell_bg);
	    }
	}

	tv1.setText("" + dateTime.getDayOfMonth());
	displayIconsCell(cellView, dateTime);

	// Somehow after setBackgroundResource, the padding collapse.
	// This is to recover the padding
	cellView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

	return cellView;
    }

    public void displayIconsCell(View cellView, DateTime dateTime) {
	Appointment appointment = new Appointment();
	appointment.setDateAppointment(dateTime);
	if (list_appointments.contains(appointment)) {
	    ArrayList<Appointment> list_appointments_day = filterListAppointmentsForDay(dateTime);
	    int size = list_appointments_day.size();
	    ImageView imageView1_calendar_icon = (ImageView) cellView.findViewById(R.id.imageView1_calendar_icon);
	    ImageView imageView2_calendar_icon = (ImageView) cellView.findViewById(R.id.imageView2_calendar_icon);
	    ImageView imageView3_calendar_icon = (ImageView) cellView.findViewById(R.id.imageView3_calendar_icon);
	    ImageView imageView4_calendar_icon = (ImageView) cellView.findViewById(R.id.imageView4_calendar_icon);
	    if (size > 0)
		imageView1_calendar_icon.setBackgroundResource(list_appointments_day.get(0).getIconSpeciality(context));
	    if (size > 1)
		imageView2_calendar_icon.setBackgroundResource(list_appointments_day.get(1).getIconSpeciality(context));
	    if (size > 2)
		imageView3_calendar_icon.setBackgroundResource(list_appointments_day.get(2).getIconSpeciality(context));
	    if (size > 3)
		imageView4_calendar_icon.setBackgroundResource(list_appointments_day.get(3).getIconSpeciality(context));
	}
    }

}
