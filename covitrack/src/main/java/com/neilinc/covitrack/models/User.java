package com.neilinc.covitrack.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name="state")
	private String state;

	@Column(name="city")
	private String city;
	
	@Column(name="email")
	private String email;
	
	@Column(name="age")
	private int ageGroup;

	@Column(name="active")
	private int active;

	@Column(name="city_id")
	private int city_id;
	
	@Column(name="slot_type")
	private int slotType;
	
	@Column(name="vaccine_type")
	private String vaccineType;
	
	@Column(name="free_dose")
	private boolean freeDose;
	
	public boolean isFreeDose() {
		return freeDose;
	}

	public void setFreeDose(boolean freeDose) {
		this.freeDose = freeDose;
	}

	public User() {
	}
	
	public User(String name, String state, String city, String email, int ageGroup, int active, int city_id, String vaccineType, int slotType, boolean freeDose) {
		super();
		this.name = name;
		this.state = state;
		this.city = city;
		this.email = email;
		this.ageGroup = ageGroup;
		this.active = active;
		this.city_id = city_id;
		this.slotType = slotType;
		this.vaccineType = vaccineType;
		this.freeDose = freeDose;
	}

	public int getSlotType() {
		return slotType;
	}

	public void setSlotType(int slotType) {
		this.slotType = slotType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(int ageGroup) {
		this.ageGroup = ageGroup;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public String getVaccineType() {
		return vaccineType;
	}

	public void setVaccineType(String vaccineType) {
		this.vaccineType = vaccineType;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", state=" + state + ", city=" + city + ", email=" + email
				+ ", ageGroup=" + ageGroup + ", active=" + active + ", city_id=" + city_id + ", slotType=" + slotType
				+ ", vaccineType=" + vaccineType + "]";
	}
	
}
