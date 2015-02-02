package com.bt.healthrecordMODEL;

import java.io.Serializable;

public class Audio implements Serializable {

	private Integer id;
	private String audioLink;
	private String date;
	private String title;
	
	public Audio (Integer id, String audioLink, String date, String title) {
		super();
		this.id = id;
		this.audioLink = audioLink;
		this.date = date;	
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAudioLink() {
		return audioLink;
	}

	public void setAudioLink(String audioLink) {
		this.audioLink = audioLink;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
