package com.neilinc.covitrack.service;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.neilinc.covitrack.models.Slots;
import com.neilinc.covitrack.models.User;

public interface ServiceLayer {
	public boolean registerForAlerts(String form) throws JSONException;
	
	public void mapSlotToList(String response, int district);
	
	public List<Integer> getDistinctDistricts();
	
	public void displayAlerts(int district);
	
	public void getCenters();

	void sendEmail(Map<User, List<Slots>> map);
	
	boolean inactivateUser(int id);
}
