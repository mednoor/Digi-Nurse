package com.bt.healthrecordMODEL;

public class Weight {

	private Integer id;
	private String weight;
	private String date;
	private String unit;

	public Weight (Integer id, String weight, String date, String unit) {
		super();
		this.id = id;
		this.weight = weight;
		this.date = date;
		this.unit = unit;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
