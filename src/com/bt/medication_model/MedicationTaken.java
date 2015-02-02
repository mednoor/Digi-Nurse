package com.bt.medication_model;

import java.io.Serializable;

import org.joda.time.DateMidnight;

public class MedicationTaken implements Serializable {

    private Integer id;
    private DateMidnight date_taken;
    private Boolean taken = false;
    private MedicationHourDosage medicationHourDosage;
    
    

    public MedicationTaken(DateMidnight date_taken, Boolean taken, MedicationHourDosage medicationHourDosage) {
	super();
	this.date_taken = date_taken;
	this.taken = taken;
	this.medicationHourDosage = medicationHourDosage;
    }
    
    

    public MedicationTaken() {
	super();
    }



    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public DateMidnight getDate_taken() {
	return date_taken;
    }

    public void setDate_taken(DateMidnight date_taken) {
	this.date_taken = date_taken;
    }

    public Boolean getTaken() {
	return taken;
    }

    public void setTaken(Boolean taken) {
	this.taken = taken;
    }

    public MedicationHourDosage getMedicationHourDosage() {
	return medicationHourDosage;
    }

    public void setMedicationHourDosage(MedicationHourDosage medicationHourDosage) {
	this.medicationHourDosage = medicationHourDosage;
    }

}
