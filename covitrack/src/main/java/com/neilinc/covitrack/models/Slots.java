package com.neilinc.covitrack.models;

public class Slots {
	
	private int capacity;
	private int slot1Slots;
	private int slot2Slots;
	private String fees;
	public int getSlot1Slots() {
		return slot1Slots;
	}

	public void setSlot1Slots(int slot1Slots) {
		this.slot1Slots = slot1Slots;
	}

	public int getSlot2Slots() {
		return slot2Slots;
	}

	public void setSlot2Slots(int slot2Slots) {
		this.slot2Slots = slot2Slots;
	}

	public String getFees() {
		return fees;
	}

	public void setFees(String fees) {
		this.fees = fees;
	}

	private String centerName;
	private String doseType;
	private String slotDate;
	private String city;
	private String state;
	private String pincode;
	private String sessionId;
	private int age;
	
	public Slots(int capacity, String centerName, String doseType, String slotDate) {
		super();
		this.capacity = capacity;
		this.centerName = centerName;
		this.doseType = doseType;
		this.slotDate = slotDate;
	}
	
	public Slots(int capacity, String centerName, String doseType, String slotDate, String city, String state,
			int age, String pincode, String sessionId) {
		super();
		this.capacity = capacity;
		this.centerName = centerName;
		this.doseType = doseType;
		this.slotDate = slotDate;
		this.city = city;
		this.state = state;
		this.age = age;
		this.pincode = pincode;
		this.sessionId = sessionId;
	}	
	
	public Slots(int capacity, String centerName, String doseType, String slotDate, String city, String state,
			int age, String pincode, String sessionId, int slot1, int slot2, String fees) {
		super();
		this.capacity = capacity;
		this.centerName = centerName;
		this.doseType = doseType;
		this.slotDate = slotDate;
		this.city = city;
		this.state = state;
		this.age = age;
		this.pincode = pincode;
		this.sessionId = sessionId;
		this.slot1Slots = slot1;
		this.slot2Slots = slot2;
		this.fees = fees;
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "Slots [capacity=" + capacity + ", slot1Slots=" + slot1Slots + ", slot2Slots=" + slot2Slots + ", fees="
				+ fees + ", centerName=" + centerName + ", doseType=" + doseType + ", slotDate=" + slotDate + ", city="
				+ city + ", state=" + state + ", pincode=" + pincode + ", sessionId=" + sessionId + ", age=" + age
				+ "]";
	}	
	

}
