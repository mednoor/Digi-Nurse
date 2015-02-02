package com.bt.healthrecordMODEL;

public class Size {

	private Integer id;
	private String size;
	private String date;
	private String unit;

	public Size (Integer id, String size, String date, String unit) {
		super();
		this.id = id;
		this.size = size;
		this.date = date;
		this.unit = unit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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
