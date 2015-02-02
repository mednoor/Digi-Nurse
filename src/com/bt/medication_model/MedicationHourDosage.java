package com.bt.medication_model;

import java.io.Serializable;

import org.joda.time.LocalTime;

public class MedicationHourDosage implements Serializable, Comparable<MedicationHourDosage> {

    private Integer id;
    private LocalTime hour = new LocalTime();
    private Double quantity = 1.0;
    private MedicationSchedule medication_schedule;

    public MedicationHourDosage(Integer id, LocalTime hour, Double quantity ) {
	super();
	this.id = id;
	this.hour = hour;
	this.quantity = quantity;
    }

    public MedicationHourDosage() {
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public LocalTime getHour() {
	return hour;
    }

    public void setHour(LocalTime hour) {
	this.hour = hour;
    }

    public Double getQuantity() {
	return quantity;
    }

    public void setQuantity(Double quantity) {
	this.quantity = quantity;
    }

    public Integer getMinutesFromHour() {
	return (hour.getHourOfDay() * 60) + hour.getMinuteOfHour();
    }

    @Override
    public boolean equals(Object obj) {
	// V�rification de l'�galit� des r�f�rences
	if (obj == this) {
	    return true;
	}

	// V�rification du type du param�tre
	if (obj instanceof MedicationHourDosage) {
	    // V�rification des valeurs des attributs
	    MedicationHourDosage other = (MedicationHourDosage) obj;

	    // Pour les attributs de type objets
	    // on compare dans un premier temps les r�f�rences
	    if (this.hour != other.hour) {
		// Si les r�f�rences ne sont pas identiques
		// on doit en plus utiliser equals()
		if (this.hour == null || !this.hour.equals(other.hour)) {
		    return false; // les attributs sont diff�rents
		}
	    }
	    // Si on arrive ici c'est que tous les attributs sont �gaux :
	    return true;
	}

	return false;
    }

    public MedicationSchedule getMedication_schedule() {
	return medication_schedule;
    }

    public void setMedication_schedule(MedicationSchedule medication_schedule) {
	this.medication_schedule = medication_schedule;
    }

    @Override
    public int compareTo(MedicationHourDosage another) {

	return hour.compareTo(another.getHour());

    }

}
