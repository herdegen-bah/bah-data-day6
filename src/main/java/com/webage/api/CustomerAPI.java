package com.webage.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.webage.domain.Customer;
import com.webage.logging.ApiLogger;
import com.webage.repository.CustomersRepository;

@RestController
//@CrossOrigin("http://localhost:3000")
@RequestMapping("/customers")
public class CustomerAPI {
	@Autowired
	CustomersRepository repo;

	@GetMapping
	public Iterable<Customer> getAll() {
		//  Workshop:  Write an implementation that replies with all customers.
		//  Your implementation should be no more than a few lines, at most, and make use of the 'repo' object
		
		return repo.findAll();   
	}

	@GetMapping("/{customerId}")
	public Optional<Customer> getCustomerById(@PathVariable("customerId") long id) {
		//  Workshop:  Write an implementation that looks up one customer.  What do you return if the requested 
		//  customer ID does not exists?  This implementation could be as short as a single line.
		return repo.findById(id);
	}
	
	@PostMapping
	public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer, UriComponentsBuilder uri) {
		//  Workshop:  Write an implementation that adds a new customer.  Your
		//  implementation should check to make sure that the name and email fields are
		//  not null and that no id was passed (it will be auto generated when the record
		//  is inserted.  Remember REST semantics - return a reference to the newly created 
		//  entity as a URI.
		if(newCustomer.getId() != 0 || newCustomer.getName() == null || newCustomer.getEmail() == null || newCustomer.getPassword() == null) {
			return ResponseEntity.badRequest().build();
		}
		newCustomer = repo.save(newCustomer);
		URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCustomer.getId()).toUri();
		ResponseEntity<?> response=ResponseEntity.created(location).build();
		return response;
		
	}

	//lookupCustomerByName GET
	@GetMapping("/byname/{username}")
	public ResponseEntity<?> lookupCustomerByNameGet(@PathVariable("username") String username,
			UriComponentsBuilder uri) {
		//  Workshop:  Write an implementation to look up a customer by name.  Think about what
		//  your response should be if no customer matches the name the caller is searching for.
		//  With the data model implemented in CustomersRepository, do you need to handle more than
		//  one match per request?
//		ArrayList<Customer> customer = new ArrayList<Customer>();
//		Iterable<Customer> customerList = repo.findAll();
//		for(Customer c: customerList) {
//			if (c.getName().equalsIgnoreCase(username)) {
//				customer.add(c);
//			}
//		}
//		
//		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		
//		if (!customer.isEmpty()) {
//			map.put("status", 1);
//			map.put("data", customer);
//			return new ResponseEntity<>(map, HttpStatus.OK);
//		} else {
//			map.clear();
//			map.put("status", 0);
//			map.put("message", "Data is not found");
//			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//		}
		ApiLogger.log("username: " + username);
		Iterator<Customer> customers = repo.findAll().iterator();
		while(customers.hasNext()) {
			Customer cust = customers.next();
			if(cust.getName().equals(username)) {
				ResponseEntity<?> response = ResponseEntity.ok(cust);
				return response;				
			}			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	//lookupCustomerByName POST
	@PostMapping("/byname")
	public ResponseEntity<?> lookupCustomerByNamePost(@RequestBody String username, UriComponentsBuilder uri) {
		//  Workshop:  Write an implementation to look up a customer by name, using POST semantics
		//  rather than GET.  You should be able to make use of most of your implmentation for
		//  lookupCustomerByNameGet().  
//		ArrayList<Customer> customer = new ArrayList<Customer>();
//		Iterable<Customer> customerList = repo.findAll();
//		for(Customer c: customerList) {
//			if (c.getName().equalsIgnoreCase(username)) {
//				customer.add(c);
//			}
//		}
//		
//		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		
//		if (!customer.isEmpty()) {
//			map.put("status", 1);
//			map.put("data", customer);
//			return new ResponseEntity<>(map, HttpStatus.OK);
//		} else {
//			map.clear();
//			map.put("status", 0);
//			map.put("message", "Data is not found");
//			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//		}
		return null;
	}	
	
	
	@PutMapping("/{customerId}")
	public ResponseEntity<?> putCustomer(
			@RequestBody Customer newCustomer,
			@PathVariable("customerId") long customerId) 
	{
		//  Workshop:  Write an implementation to update or create a new customer with an HTTP PUT, with the 
		//  requestor specifying the customer ID.  Are there error conditions to be handled?  How much data
		//  validation should you implement considering that customers are stored in a CustomersRepository object.
		if (newCustomer.getId() != customerId || newCustomer.getName() == null || newCustomer.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		newCustomer = repo.save(newCustomer);
		return ResponseEntity.ok().build();
		
	}	
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable("customerId") long id) {
		//  Implement a method to delete a customer.  What is an appropriate response? 
		//
		//  For discussion (do not worry about implementation):  What are some ways of handling 
		//  a "delete"?  Is it always the right thing from a business point of view to literally 
		//  delete a customer entry?  If you did actually delete a customer entry, are there issues
		//  you could potentially run into later? 
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			repo.deleteById(id);
			map.put("status", 1);
			map.put("message", "Customer is deleted successfully!");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			map.clear();
			map.put("status", 0);
			map.put("message", "Customer is not found");
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}	
	
	
}
