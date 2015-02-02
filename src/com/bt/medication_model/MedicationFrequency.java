package com.bt.medication_model;

import java.io.Serializable;

public class MedicationFrequency implements Serializable {

    private Integer id;
    private Boolean is_every_day = false;
    private String days_week = "";
    private Integer interval_day = 0;

    public MedicationFrequency(Integer id, Boolean is_every_day, String days_week, Integer interval_day) {
	super();
	this.id = id;
	this.is_every_day = is_every_day;
	this.days_week = days_week;
	this.interval_day = interval_day;
    }

    public MedicationFrequency() {
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Boolean getIs_every_day() {
	return is_every_day;
    }

    public void setIs_every_day(Boolean is_every_day) {
	this.is_every_day = is_every_day;
	// if we set one of three , default value for the rest
	this.days_week = "";
	this.interval_day = 0;
    }

    public String getDays_week() {
	return days_week;
    }

    public void setDays_week(String days_week) {
	this.days_week = days_week;
	// if we set one of three , default value for the rest
	this.interval_day = 0;
	this.is_every_day = false;
    }

    public Integer getInterval_day() {
	return interval_day;
    }

    public void setInterval_day(Integer interval_day) {
	this.interval_day = interval_day;
	// if we set one of three , default value for the rest
	this.days_week = "";
	this.is_every_day = false;
    }
}
