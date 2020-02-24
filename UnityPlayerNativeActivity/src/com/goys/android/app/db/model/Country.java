package com.goys.android.app.db.model;

public class Country {
	private int id;
	private String country;

	public Country() {

	}

	public Country(int ID, String CountryName) {
		this.id = ID;
		this.country = CountryName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
