package com.webage.api;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.webage.domain.Registration;
import com.webage.repository.RegistrationRepository;

@RestController
@RequestMapping("/registrations")
public class RegistrationAPI {

	@Autowired
	RegistrationRepository repo;

	@GetMapping
	public Iterable<Registration> getAll() {
		//  Workshop:  Implementation to return existing registrations
		return repo.findAll();
	}

	@GetMapping("/{registrationId}")
	public Optional<Registration> getRegistrationById(@PathVariable("registrationId") long id) {
		//  Workshop:  Implementation to return a single registration from an ID
		return repo.findById(id);
	}

	@PostMapping
	public ResponseEntity<?> addRegistration(@RequestBody Registration newRegistration, UriComponentsBuilder uri) {
		//  Workshop:  Implementation to add a new registration; think about data validation and error handling.
		if(newRegistration.getId() != 0 || newRegistration.getDate() == null || newRegistration.getNotes() == null) {
			return ResponseEntity.badRequest().build();
		}
		newRegistration = repo.save(newRegistration);
		URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newRegistration.getId()).toUri();
		ResponseEntity<?> response=ResponseEntity.created(location).build();
		return response;
	}

	@PutMapping("/{registrationId}")
	public ResponseEntity<?> putRegistration(
			@RequestBody Registration newRegistration,
			@PathVariable("registrationId") long registrationId) 
	{
		// Workshop: Implementation to update an event. Think about error handling.
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Registration registration = repo.findById(registrationId).get();
			registration.setCustomerId(newRegistration.getCustomerId());
			registration.setDate(newRegistration.getDate());
			registration.setEventId(newRegistration.getEventId());
			registration.setNotes(newRegistration.getNotes());
			repo.save(registration );
			map.put("status", 1);
			map.put("data", repo.findById(registrationId));
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Registration is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}	
	
	@DeleteMapping("/{eventId}")
	public ResponseEntity<?> deleteRegistrationById(@PathVariable("eventId") long id) {
		//  Workshop:  Implementation to delete an event.  For discussion (do not implement unless
		//  you are sure you have time):  Are there checks you should make to ensure validity of 
		//  data across various entities?  Where should these checks be implemented.  Are there
		//  advantages and disadvantages to separating data into separate independent entities,
		//  each with it's own "microservice"?
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			repo.deleteById(id);
			map.put("status", 1);
			map.put("message", "Registration is deleted successfully!");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Registration is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}	
	
}