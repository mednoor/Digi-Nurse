package com.bt.consultation_model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import android.content.Context;

import com.bt.healthrecord.R;
import com.bt.medication_model.MedicationHourDosage;
import com.bt.utils.Utils;

public class Appointment implements Serializable {

    private Integer id;
    private String uri_contact_doctor = "";
    private Integer speciality;
    private String other_specialtity;
    private DateTime date_appointment;
    private String comment = "";

    public Appointment(Integer id, String uri_contact_doctor, Integer speciality, String other_specialtity,
	    DateTime date_appointment, String comment) {
	super();
	this.id = id;
	this.uri_contact_doctor = uri_contact_doctor;
	this.speciality = speciality;
	this.other_specialtity = other_specialtity;
	this.date_appointment = date_appointment;
	this.comment = comment;
    }

    public Appointment() {
	super();
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getUriContactDdoctor() {
	return uri_contact_doctor;
    }

    public void setUriContactDoctor(String uri_contact_doctor) {
	this.uri_contact_doctor = uri_contact_doctor;
    }

    public Integer getSpeciality() {
	return speciality;
    }

    public String getNameSpeciality(Context context) {
	String[] specialities = context.getResources().getStringArray(R.array.Speciality);
	String nameSpeciality;
	if (speciality == 0)// other speciality
	    nameSpeciality = other_specialtity;
	else
	    nameSpeciality = specialities[speciality - 1];
	return nameSpeciality;
    }

    public Integer getIconSpeciality(Context context) {
	String[] specialities = context.getResources().getStringArray(R.array.Speciality);
	Integer id_speciality_icon;
	if (speciality == 0)
	    id_speciality_icon = Utils.drawables[specialities.length - 1];
	else
	    id_speciality_icon = Utils.drawables[speciality - 1];
	return id_speciality_icon;
    }

    public void setSpeciality(Integer speciality) {
	this.speciality = speciality;
    }

    public void setHourAppointment(LocalTime hour) {
	date_appointment = date_appointment.withTime(hour.getHourOfDay(), hour.getMinuteOfHour(),
		hour.getSecondOfMinute(), hour.getMillisOfSecond());
    }

    public String getOtherSpecialtity() {
	return other_specialtity;
    }

    public void setOtherSpecialtity(String other_specialtity) {
	this.other_specialtity = other_specialtity;
    }

    public DateTime getDateAppointment() {
	return date_appointment;
    }

    public LocalTime getHourAppointment() {
	return date_appointment.toLocalTime();
    }

    public void setDateAppointment(DateTime date_appointment) {
	this.date_appointment = date_appointment;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    @Override
    public boolean equals(Object obj) {
	// Vérification de l'égalité des références
	if (obj == this) {
	    return true;
	}

	// Vérification du type du paramètre
	if (obj instanceof Appointment) {
	    // Vérification des valeurs des attributs
	    Appointment other = (Appointment) obj;

	    // Pour les attributs de type primitif
	    // on compare directement les valeurs :
	    if (this.date_appointment.getMonthOfYear() != other.date_appointment.getMonthOfYear() || this.date_appointment.getDayOfMonth() != other.date_appointment.getDayOfMonth()
		    || this.date_appointment.getYear()!= other.date_appointment.getYear()) {
		return false; // les attributs sont différents
	    }
	    // Si on arrive ici c'est que tous les attributs sont égaux :
	    return true;
	}

	return false;
    }

}
