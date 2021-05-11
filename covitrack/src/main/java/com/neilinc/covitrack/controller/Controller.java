package com.neilinc.covitrack.controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.neilinc.covitrack.service.ServiceLayer;

@RestController
@CrossOrigin(allowedHeaders = "*")
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
	
	@GetMapping("/startthesearch")
	private void searchStart(@RequestParam(name = "private_key") String id) {
		if(id.equals("1997")) {
			service.flipSwitchTrue();
		}
	}	
//	@GetMapping("/sendemail")
//	private void sendEmail() {
//		service.sendEmail();
//	}
	
	@GetMapping("/stopthesearch")
	private void searchStop(@PathVariable(name = "private_key") String id) {
		if(id.equals("7991")) {
			service.flipSwitchFalse();
		}
	}
}
