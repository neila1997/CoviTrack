package com.neilinc.covitrack.controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neilinc.covitrack.service.ServiceLayer;

@RestController
@CrossOrigin(allowedHeaders = {"*"})
public class Controller {

	@Autowired
	ServiceLayer service;

	@GetMapping(path = "/testapi")
	public ResponseEntity<String> testApi() {
		return new ResponseEntity<String>("Latched", HttpStatus.OK);
	}

	@PostMapping(path = "/registerforalerts", consumes = "application/json")
	public ResponseEntity<String> registerApi(@RequestBody String alertForm) {
		try {
			if (service.registerForAlerts(alertForm))
				return new ResponseEntity<String>("You have been successfully registered for alerts", HttpStatus.OK);

		} catch (JSONException e) {
			System.err.println(e.getMessage());
		}

		return new ResponseEntity<String>("Failure", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(path = "/unsubscribe")
	public ResponseEntity<String> unsubscribeAlerts(@RequestParam(name="id") int id){
		System.out.println("id "+ id);
		if(service.inactivateUser(id))
			return new ResponseEntity<String>("You have been successfully unsubscribed for alerts", HttpStatus.OK);
		
		return new ResponseEntity<String>("Unsuccessful", HttpStatus.BAD_GATEWAY);
	}
}
