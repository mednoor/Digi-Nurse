package com.bt.healthrecordDAO;

public class Emergency {

	private Integer id;
	private String profil;
	private String name;
	private String tel;
	
	public Emergency (Integer id, String profil, String name, String tel) {
		super();
		this.id = id;
		this.profil = profil;
		this.name = name;	
		this.tel = tel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProfil() {
		return profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
}
