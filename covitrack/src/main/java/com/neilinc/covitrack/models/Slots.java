package com.neilinc.covitrack.models;

public class Slots {
	
	private int capacity;
	private String centerName;
	private String doseType;
	private String slotDate;
	private String city;
	private String state;
	private String pincode;
	private int age;
	
	public Slots(int capacity, String centerName, String doseType, String slotDate) {
		super();
		this.capacity = capacity;
		this.centerName = centerName;
		this.doseType = doseType;
		this.slotDate = slotDate;
	}
	
	public Slots(int capacity, String centerName, String doseType, String slotDate, String city, String state,
			int age, String pincode) {
		super();
		this.capacity = capacity;
		this.centerName = centerName;
		this.doseType = doseType;
		this.slotDate = slotDate;
		this.city = city;
		this.state = state;
		this.age = age;
		this.pincode = pincode;
	}

	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public String getDoseType() {
		return doseType;
	}
	public void setDoseType(String doseType) {
		this.doseType = doseType;
	}
	public String getSlotDate() {
		return slotDate;
	}
	public void setSlotDate(String slotDate) {
		this.slotDate = slotDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "Slots [capacity=" + capacity + ", centerName=" + centerName + ", doseType=" + doseType + ", slotDate="
				+ slotDate + ", city=" + city + ", state=" + state + ", pincode=" + pincode + ", age=" + age + "]";
	}	

}
