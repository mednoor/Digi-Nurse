package com.bt.medication_model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.DateMidnight;
import org.joda.time.LocalTime;

public class MedicationSchedule implements Serializable {

    private Integer id;
    private DateMidnight date_start = new DateMidnight();
    private Integer number_of_days = 10;
    private Boolean is_continuous = false;
    private MedicationFrequency medication_frequency;
    private ArrayList<MedicationHourDosage> medication_hour_dosage_list = new ArrayList<MedicationHourDosage>();
    private Medication medication;

    public MedicationSchedule(Integer id, DateMidnight date_start, Integer number_of_days, Boolean is_continuous) {
	super();
	this.id = id;
	this.date_start = date_start;
	this.number_of_days = number_of_days;
	this.is_continuous = is_continuous;
    }

    public MedicationSchedule() {
	medication_frequency = new MedicationFrequency();
	medication_hour_dosage_list = new ArrayList<MedicationHourDosage>();
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public DateMidnight getDate_start() {
	return date_start;
    }

    public void setDate_start(DateMidnight date_start) {
	this.date_start = date_start;
    }

    public Integer getNumber_of_days() {
	return number_of_days;
    }

    public void setNumber_of_days(Integer number_of_days) {
	this.number_of_days = number_of_days;
    }

    public Boolean getIs_continuous() {
	return is_continuous;
    }

    public void setIs_continuous(Boolean is_continuous) {
	this.is_continuous = is_continuous;
    }

    public MedicationFrequency getMedication_frequency() {
	return medication_frequency;
    }

    public void setMedication_frequency(MedicationFrequency medication_frequency) {
	this.medication_frequency = medication_frequency;
    }

    public void addMedicationHourDosage(LocalTime hour, Double dosage) {
	MedicationHourDosage new_medication_hour_dosage = new MedicationHourDosage();
	new_medication_hour_dosage.setHour(hour);

	int indice = medication_hour_dosage_list.indexOf(new_medication_hour_dosage);
	medication_hour_dosage_list.get(indice).setQuantity(dosage);
    }

    public void addMedicationHour(LocalTime hour) {
	MedicationHourDosage new_medication_hour_dosage = new MedicationHourDosage();
	new_medication_hour_dosage.setHour(hour);

	if (!isMedicationHourExist(hour)) {
	    medication_hour_dosage_list.add(new_medication_hour_dosage);
	}

    }

    public boolean isMedicationHourExist(LocalTime hour) {
	MedicationHourDosage new_medication_hour_dosage = new MedicationHourDosage();
	new_medication_hour_dosage.setHour(hour);

	return medication_hour_dosage_list.contains(new_medication_hour_dosage);
    }

    public void deleteMedicationHour(LocalTime hour) {
	MedicationHourDosage medication_hour_dosage_to_delete = new MedicationHourDosage();
	medication_hour_dosage_to_delete.setHour(hour);

	medication_hour_dosage_list.remove(medication_hour_dosage_to_delete);

    }

    public ArrayList<MedicationHourDosage> getMedication_hour_dosage_list() {
	return medication_hour_dosage_list;
    }

    public void setMedication_hour_dosage_list(ArrayList<MedicationHourDosage> medication_hour_dosage_list) {
	this.medication_hour_dosage_list = medication_hour_dosage_list;
    }

    public ArrayList<LocalTime> getListHours() {
	ArrayList<LocalTime> listHours = new ArrayList<LocalTime>();
	Iterator<MedicationHourDosage> it = medication_hour_dosage_list.iterator();
	MedicationHourDosage medicationHourDosage;
	while (it.hasNext()) {
	    medicationHourDosage = (MedicationHourDosage) it.next();
	    listHours.add(medicationHourDosage.getHour());
	}

	return listHours;
    }

    public Medication getMedication() {
	return medication;
    }

    public void setMedication(Medication medication) {
	this.medication = medication;
    }

}
