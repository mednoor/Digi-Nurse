package com.bt.healthrecordMODEL;

import java.io.Serializable;

public class Note implements Serializable{

	private Integer id;
	private String title;
	private String note;
	private String dateCreated;
	private String dateUpdated;
	private Integer state;

	public Note (Integer id, String title, String note, String dateCreated, String dateUpdated, Integer state) {
		super();
		this.id = id;
		this.title = title;
		this.note = note;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}