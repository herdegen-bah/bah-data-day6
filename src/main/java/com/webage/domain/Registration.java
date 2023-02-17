package com.webage.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="REGISTRATIONS")
public class Registration {
	//  Workshop:
	//
	//  Implement Registration.  Your implementation is very likely going to change 
	//  over time as you add functionality 
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	
	@Column(name="EVENT_ID")
	String eventId;
	
	@Column(name="CUSTOMER_ID")
	String customerId;
	
	@Column(name="REGISTRATION_DATE")
	Date date;
	
	String notes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	

}
