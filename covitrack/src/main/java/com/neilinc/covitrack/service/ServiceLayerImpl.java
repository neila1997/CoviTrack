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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.neilinc.covitrack.dao.DaoImpl;
import com.neilinc.covitrack.models.Slots;
import com.neilinc.covitrack.models.User;

@Service
public class ServiceLayerImpl implements ServiceLayer {

	@Autowired
	DaoImpl dao;

	static boolean flip = false;

	static List<Slots> slots = new ArrayList();

	@Override
	public boolean registerForAlerts(String form) throws JSONException {

		try {
			String name, email, state, city;
			int age, city_id;
			JSONObject json = new JSONObject(form);
			System.out.println(json.toString());
			email = json.getString("email");
			name = json.getString("name");
			city = json.getString("city");
			state = json.getString("state");
			age = json.getInt("age");
			city_id = json.getInt("city_id");

			System.out.println(name + " " + email + " " + age + " " + state + " " + city + " " + city_id);

			User user = new User(name, state, city, email, age, 1, city_id);
			return dao.registerForAlerts(user);

		} catch (JSONException e) {
			System.err.println("JSON Exception");
		}
		return false;
	}

	@Override
	public void mapSlotToList(String response, int district) {

		Set<String> cities = new HashSet();
		Set<Integer> ageGroups = new HashSet();

		try {
			slots = new ArrayList<Slots>();
			String str = new JSONObject(response).getString("centers");
			JSONArray array = new JSONArray(str);
			// looping through the sessions
			for (int i = 0; i < array.length(); i++) {
				String centerName, doseType = "", slotDate = "", state, city, pincode;
				int capacity = 0, age = 0;
				centerName = array.getJSONObject(i).getString("name");
				state = array.getJSONObject(i).getString("state_name");
				city = array.getJSONObject(i).getString("district_name");
				pincode = array.getJSONObject(i).getString("pincode");
				
				JSONArray array1 = new JSONArray(array.getJSONObject(i).getString("sessions"));
				for (int j = 0; j < array1.length(); j++) {
					slotDate = array1.getJSONObject(j).getString("date");
					doseType = array1.getJSONObject(j).getString("vaccine");
					capacity = array1.getJSONObject(j).getInt("available_capacity");
					age = array1.getJSONObject(j).getInt("min_age_limit");
					
					if (capacity > 0) {
						slots.add(new Slots(capacity, centerName, doseType, slotDate, city, state, age, pincode));
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

	public List<Integer> getDistinctDistricts() {
		return dao.retrieveDistinctDistricts();
	}

	public void displayAlerts(int district) {

		List<User> users = dao.retrieveUserByDistrict(district);
		Map<User, List<Slots>> map = new HashMap();

//		System.out.println("District ID: " + district);
//		System.out.println("Applicable Slots \n");
		for (User user : users) {
//			System.out.println(user.getName());
			for (Slots slot : slots) {
				if (user.getAgeGroup() == slot.getAge()) {
					if (map.containsKey(user))
						map.get(user).add(slot);
					else {
						List<Slots> list = new LinkedList();
						list.add(slot);
						map.put(user, list);
					}
//					System.out.println(slot);
				}
			}
		}
		this.sendEmail(map);

	}

	@Override
	public void flipSwitchTrue() {
		flip = true;
		this.getCenters();
	}

	@Override
	public void flipSwitchFalse() {
		flip = false;
	}

	@Override
	public void getCenters() {

		while (flip) {

			System.out.println("Inside");
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedDate = myDateObj.format(myFormatObj);

			List<Integer> districts = this.getDistinctDistricts();

			for (Integer district : districts) {
				System.out.println(district);
				final String uri = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
						+ district + "&date=" + formattedDate;

				WebClient webClient = WebClient.create();

				String response = webClient.get().uri(uri)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).exchange().block()
						.bodyToMono(String.class).block();

				this.mapSlotToList(response, district);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendEmail(Map<User, List<Slots>> map) {
		// Recipient's email ID needs to be mentioned.

		// Sender's email ID needs to be mentioned
		String from = "covitrack@yahoo.com";
		final String username = "covitrack";// change accordingly
		final String password = "mruwwvzfhtjqoswy";// change accordingly

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
				return new PasswordAuthentication(username, password);
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
				message.setFrom(new InternetAddress(from));

//				Address addresses[] = new InternetAddress[1];
//				Address address = new InternetAddress("Neil.Abraham@Cerner.com");
//				addresses[0] = new InternetAddress();
//				addresses[0] = address;
//				message.setRecipients(RecipientType.BCC, addresses);

				// Set To: header field of the header.
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

				// Set Subject: header field
				message.setSubject("Vaccine Slot Availability");

				// Send the actual HTML message, as big as you like
				String content = "<html>"
						+ "<head> <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n"
						+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\n"
						+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>\n"
						+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script> </head> "
						+ "<body> <div class='container mt-md-5 mt-3'><h4 class='text text-info'> Hi " + user.getName()
						+ ", The Following Slots are available </h4> <br> <br>"
						+ "<div class = 'table-responsive'><table class='table table-hover'> <thead> <tr> " + "<th> Center Name </th>"
						+ "<th> Available Slots </th>" + "<th> Vaccine Name </th>" + "<th> Date </th>"
						+ "<th> City </th>" + "<th> State </th>" + "<th> Pincode  </th>" + "</tr> </thead><tbody>";
				for (Slots slot : slots) {
					content += "<tr> " + "<td>" + slot.getCenterName() + "</td>" + "<td>" + slot.getCapacity() + "</td>"
							+ "<td>" + slot.getDoseType() + "</td>" + "<td>" + slot.getSlotDate() + "</td>" + "<td>"
							+ slot.getCity() + "</td>" + "<td>" + slot.getState() + "</td>" + "<td>" + slot.getPincode()
							+ "</td>" + "</tr>";
				}

				content += "</tbody></table></div> <div class='mt-md-5 mt-3'> <div class='h4 text-info'>Hope this Helps you! <br> Team Neilino</div></div></div></body></html>";

//						,
				message.setContent(content, "text/html");

				// Send message
				Transport.send(message);

				System.out.println("Sent message successfully....");

			}

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
