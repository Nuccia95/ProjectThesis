package com.demo.thesisbackend.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.bean.User;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

import com.demo.thesisbackend.managers.LoginManager;
import com.demo.thesisbackend.managers.ReservationManager;
import com.demo.thesisbackend.managers.ResourceManager;

@CrossOrigin("*")
@RestController
public class Controller implements WebServicesInterface {
	
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ReservationManager reservationManager;
	@Autowired
	private ResourceManager resourceManager;
	
	@Override
	@PostMapping("/login")
	public User login(final HttpEntity<User> user) {
		return loginManager.login(user.getBody());
	}
	
	@Override
	@PostMapping("/registration")
	public User registration(final HttpEntity<User> user) {
		return loginManager.registration(user.getBody());
	}
	
	@Override
	@PostMapping("/createReservation")
	public Reservation createReservation(final HttpEntity<Reservation> reservation) {
		return reservationManager.createReservation(reservation.getBody());
	}
	
	@Override
	@PostMapping("/updateDate")
	public Reservation updateDate(final HttpEntity<Reservation> reservation) {
		return reservationManager.updateDate(reservation.getBody());
	}
	
	@Override
	@PostMapping("/updateSingleReservation")
	public Reservation updateSingleReservation(final HttpEntity<Reservation> reservation) {
		return reservationManager.updateReservation(reservation.getBody());
	}
	
	@Override
	@PostMapping("/deleteReservation")
	public void deleteReservation(final HttpEntity<Reservation> reservation) {
		 reservationManager.deleteReservation(reservation.getBody());
	}
	
	@Override
	@PostMapping("/deleteRecurringReservation")
	public void deleteRecurringReservations(final HttpEntity<Reservation> reservation) {
		 reservationManager.deleteRecurringReservation(reservation.getBody());
	}
	
	@Override
	@GetMapping(value="/getReservationsByOwner")
	public Set<Reservation> getByOwner(final long id) {
		return reservationManager.getReservationsByOwner(id);
	}
	
	@Override
	@PostMapping("/createResource")
	public Resource createResource(final HttpEntity<Resource> resource) {
		return resourceManager.createResource(resource.getBody());
	}
	
	@Override
	@PostMapping("/deleteResource")
	public void deleteResource(final HttpEntity<Resource> resource) {
		resourceManager.deleteResource(resource.getBody());
	}
	
	@Override
	@GetMapping(value="/getAllResources")
	public List<Resource> getAllResources(){
		return resourceManager.getAllResources();
	}
	
	@Override
	@GetMapping(value="/getResourcesNames")
	public List<String> getResourcesNames() {
		return resourceManager.getResourcesNames();
	}
	
	@Override
	@GetMapping(value="/getAllEmails")
	public List<String> getAllEmails() {
		return loginManager.getAllEmails();
	}

	@Override
	@GetMapping(value="/getAllReservations")
	public Set<Reservation> getAllReservations() {
		return reservationManager.getAllReservations();
	}
	
}
