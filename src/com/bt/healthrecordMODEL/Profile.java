package com.bt.healthrecordMODEL;

public class Profile {

	private Integer id;
	private String firstName;
	private String lastName;
	private String birthDay;
	private String bloodType;
	private String doctor;
	private String imgLink;
	private String civility;

	public Profile (Integer id, String firstName, String lastName, String birthDay, String bloodType, String doctor, String imgLink, String civility) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDay = birthDay;
		this.bloodType = bloodType;
		this.doctor = doctor;
		this.imgLink = imgLink;
		this.civility = civility;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getImgLink() {
		return imgLink;
	}
	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
	public String getCivility() {
		return civility;
	}
	public void setCivility(String civility) {
		this.civility = civility;
	}
}
