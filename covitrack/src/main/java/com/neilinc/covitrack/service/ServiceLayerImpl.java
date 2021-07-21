package com.neilinc.covitrack.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.neilinc.covitrack.dao.DaoImpl;
import com.neilinc.covitrack.models.Slots;
import com.neilinc.covitrack.models.User;

@Service
@EnableScheduling
public class ServiceLayerImpl implements ServiceLayer {

	@Autowired
	DaoImpl dao;

	static List<Slots> slots = new ArrayList<Slots>();
	static Map<String, List<String>> slotSessions = new HashMap<String, List<String>>();
	static String cowinReg = "https://selfregistration.cowin.gov.in";
	
	@Value("${app.baseurl}")
	private String frontEndBaseUrl;

	@Value("${email.id}")
	private String emailId;
	
	@Value("${email.username}")
	private String emailUsername;
	
	@Value("${email.password}")
	private String emailPassword;

	@Override
	public boolean registerForAlerts(String form) throws JSONException {

		try {
			String name, email, state, city, vaccineType;
			int age, city_id, slotType;
			boolean freeDose;
			JSONObject json = new JSONObject(form);
			System.out.println(json.toString());
			email = json.getString("email");
			name = json.getString("name");
			city = json.getString("city");
			state = json.getString("state");
			age = json.getInt("age");
			city_id = json.getInt("city_id");
			slotType = json.getInt("dose");
			vaccineType = json.getString("vaccine");
			freeDose = json.getInt("free_dose") == 0 ? false : true;

//			System.out.println(name + " " + email + " " + age + " " + state + " " + city + " " + city_id);

			User user = new User(name, state, city, email, age, 1, city_id, vaccineType.toUpperCase(), slotType, freeDose);
			return dao.registerForAlerts(user);

		} catch (JSONException e) {
			System.err.println("JSON Exception");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Integer> getDistinctDistricts() {
		return dao.retrieveDistinctDistricts();
	}

	@Override
	@Scheduled(fixedDelay = 60000, initialDelay = 10)
	public void getCenters() {

		System.out.println("Inside");
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = myDateObj.format(myFormatObj);
		
		try {

		List<Integer> districts = this.getDistinctDistricts();

		for (Integer district : districts) {
//			System.out.println(district);
			final String uri = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
					+ district + "&date=" + formattedDate;

			WebClient webClient = WebClient.create();

			String response = webClient.get().uri(uri)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
					.bodyToMono(String.class).block();

			this.mapSlotToList(response, district);
		}
		}
		catch (Exception e) {
			System.out.println("Oops Error: "+e);
		}
	}

	@Override
	public void mapSlotToList(String response, int district) {

		Set<String> cities = new HashSet<String>();
		Set<Integer> ageGroups = new HashSet<Integer>();

		try {
			slots = new ArrayList<Slots>();
			String str = new JSONObject(response).getString("centers");
			JSONArray array = new JSONArray(str);
			// looping through the sessions
			for (int i = 0; i < array.length(); i++) {
				String centerName, doseType = "", slotDate = "", state, city, pincode, sessionId, fee;
				int capacity = 0, age = 0, slot1Slots = 0, slot2Slots = 0;
				centerName = array.getJSONObject(i).getString("name");
				state = array.getJSONObject(i).getString("state_name");
				city = array.getJSONObject(i).getString("district_name");
				pincode = array.getJSONObject(i).getString("pincode");
				Map<String, String> vaccines = new HashMap<String, String>();
				String feeType = array.getJSONObject(i).getString("fee_type");
			
				if (!feeType.toUpperCase().equals("FREE")){
				JSONArray vaccine_array = (array.getJSONObject(i).getJSONArray("vaccine_fees"));
				for (int vac = 0; vac < vaccine_array.length(); vac++) {
//					System.out.println(vaccine_array.getJSONObject(vac).getString("vaccine"));
//					System.out.println(vaccine_array.getJSONObject(vac).getString("fee"));
					vaccines.put(vaccine_array.getJSONObject(vac).getString("vaccine"), vaccine_array.getJSONObject(vac).getString("fee").equals("0")?"Free":vaccine_array.getJSONObject(vac).getString("fee"));
				}}
					
					

				JSONArray array1 = new JSONArray(array.getJSONObject(i).getString("sessions"));
				for (int j = 0; j < array1.length(); j++) {
					slotDate = array1.getJSONObject(j).getString("date");
					doseType = array1.getJSONObject(j).getString("vaccine");
					capacity = array1.getJSONObject(j).getInt("available_capacity");
					age = array1.getJSONObject(j).getInt("min_age_limit");
					slot1Slots = array1.getJSONObject(j).getInt("available_capacity_dose1");
					slot2Slots = array1.getJSONObject(j).getInt("available_capacity_dose2");
					
					if (vaccines.size() != 0)
							fee = vaccines.get(doseType);
					else
							fee = "Free";
					sessionId = array1.getJSONObject(j).getString("session_id");

					if (capacity > 0) {
						slots.add(new Slots(capacity, centerName, doseType, slotDate, city, state, age, pincode,
								sessionId, slot1Slots, slot2Slots, fee));
						cities.add(city);
						ageGroups.add(age);
					}
				}
			}

			this.displayAlerts(district);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * This function checks whether a user is eligible for the slots that the API fetches. 
	 */
	@Override
	public void displayAlerts(int district) {

		List<User> users = dao.retrieveUserByDistrict(district);
		Map<User, List<Slots>> map = new HashMap<User, List<Slots>>();
		boolean flag = false;
		
		for (User user : users) {
//			System.out.println(user.getName() + " Slot: " + user.getSlotType());
				System.out.println(user.getName() + " Dose Free: "+user.isFreeDose());
			for (Slots slot : slots) {
				if (!slot.getFees().toUpperCase().equals("FREE") && user.isFreeDose()) {
					System.out.println(slot.getCenterName());
					System.out.println(slot.getDoseType());
					System.out.println(slot.getSlotDate());
					System.out.println(slot.getFees());
					continue;
				}
				
				if (slotSessions.get(user.getEmail()) == null) {
//					System.out.println(slotSessions.size());
//					System.out.println("No Slots");
//					System.out.println(user.getName());
					slotSessions.put(user.getEmail(), new LinkedList<String>());
				}
				
				
				//This Logic would create a boolean variable that checks for the slot availability of a vaccine for the dose the user wants.
				boolean slotsAvailable = false;
				if ((slot.getSlot1Slots() > 0 && user.getSlotType() == 1) || (slot.getSlot2Slots() > 0 && user.getSlotType() == 2) || user.getSlotType() == 0)
					slotsAvailable = true;
				
				//Checking whether the user has been alerted for this session previously. 
				if (!slotSessions.get(user.getEmail()).contains(slot.getSessionId()) && slotsAvailable) {
					if (user.getAgeGroup() == slot.getAge() && user.getVaccineType().equals(slot.getDoseType().toUpperCase())) {
						flag = true;
						slotSessions.get(user.getEmail()).add(slot.getSessionId());
						if (map.containsKey(user))
							map.get(user).add(slot);
						else {
							List<Slots> list = new LinkedList<Slots>();
							list.add(slot);
							map.put(user, list);
						}
					}
				}
			}
		}
		if (flag)
			this.sendEmail(map);

	}

	
	@Override
	public void sendEmail(Map<User, List<Slots>> map) {
		// Recipient's email ID needs to be mentioned.

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.mail.yahoo.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUsername, emailPassword);
			}
		});

		try {

			for (Map.Entry<User, List<Slots>> entry : map.entrySet()) {

				User user = entry.getKey();
				List<Slots> slots = entry.getValue();

				// Create a default MimeMessage object.
				Message message = new MimeMessage(session);

				String to = user.getEmail();

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(emailId));

				// Set To: header field of the header.
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

				// Set Subject: header field
				message.setSubject("Vaccine Slot Availability");

				// Send the actual HTML message, as big as you like
				String content = "<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
						+ "    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css'"
						+ "        integrity='sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm' crossorigin='anonymous'>"
						+ "    <!-- jQuery Library -->"
						+ "    <script src='https://code.jquery.com/jquery-3.2.1.slim.min.js'"
						+ "        integrity='sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN' crossorigin='anonymous'>"
						+ "        </script>"
						+ "    <!-- Popper -->"
						+ "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js'"
						+ "        integrity='sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q' crossorigin='anonymous'>"
						+ "        </script>"
						+ "    <!-- Compiled and Minified Bootstrap JavaScript -->"
						+ "    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js'"
						+ "        integrity='sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl' crossorigin='anonymous'>"
						+ "        </script>"
						+ "</head>"
						+ "<body>"
						+ "    <div class='container mt-md-5 mt-3'>"
						+ "        <h4 class='text text-info'> Hi"
						+ "            The Following Slots are available </h4> <br> <br> </div>"
						+ "        <div class='container mt-3 mb-3'>"
						+ "            <table class='table table-hover'>"
						+ "                <thead>"
						+ "                    <tr>"
						+ "                        <th> Center Name </th>"
						+ "                        <th> Minimum Age </th>"
						+ "                        <th> Vaccine Name </th>"
						+ "                        <th> Cost </th>"
						+ "                        <th> Available Slots for Dose 1</th>"
						+ "                        <th> Available Slots for Dose 2</th>"
						+ "                        <th> Date </th>"
						+ "                        <th> City </th>"
						+ "                        <th> State </th>"
						+ "                        <th> Pincode </th>"
						+ "                    </tr>"
						+ "                </thead>"
						+ "                <tbody>";

				for (Slots slot : slots) {
					content += "<tr> " + "<td>" + slot.getCenterName() + "</td>" + "<td>" + slot.getAge() + "</td>" +"<td>" + slot.getDoseType() + "<td>" + slot.getFees() +  "<td>" + slot.getSlot1Slots() + "</td>"+ "<td>" + slot.getSlot2Slots() + "</td>"
							+ "</td>" + "<td>" + slot.getSlotDate() + "</td>" + "<td>"
							+ slot.getCity() + "</td>" + "<td>" + slot.getState() + "</td>" + "<td>" + slot.getPincode()
							+ "</td>" + "</tr>";
				}
				content +="                </tbody>"
						+ "            </table>"
						+ "        </div>"
						+ "        <div class='container mt-3 mt-md-5'>"
						+ "        <div class='row'>"
						+ "            <div class='col col-6'>"
						+ "                <span class='text text-left'>"
						+ "                    <button class='ml-5 btn btn-outline-warning'><a href='"+frontEndBaseUrl+"/unsubscribealerts/"+user.getId()+"' style='text-decoration: none; color: black;' target='_blank'>Unsubscribe from alerts</a></button>"
						+ "                </span>"
						+ "                <span class='text text-right'>"
						+ "                    <button class='ml-5 btn btn-outline-success'><a href='"+cowinReg+"' style='text-decoration: none; color: black;' target='_blank'>CoWin Registration Page</a></button>"
						+ "                </span>"
						+ "            </div>"
						+ "        </div>"
						+ "        <div class='row mt-5'>"
						+ "            <div class='h4 text-info'>Best, <br> Team Neilino</div>"
						+ "        </div>"
						+ "    </div>"
						+ "</body>"
						+ "</html>";
//						,
				message.setContent(content, "text/html");

				// Send message
				Transport.send(message);

				System.out.println("Sent message successfully to " + user.getEmail());

			}

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean inactivateUser(int id) {
		return dao.inactivateUser(id);
	}

}
