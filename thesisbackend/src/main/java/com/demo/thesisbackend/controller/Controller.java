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
	public User login(HttpEntity<User> user) {
		return loginManager.login(user.getBody());
	}
	
	@Override
	@PostMapping("/registration")
	public User registration(HttpEntity<User> user) {
		return loginManager.registration(user.getBody());
	}
	
	@Override
	@PostMapping("/createReservation")
	public Reservation createReservation(HttpEntity<Reservation> reservation) {
		return reservationManager.createReservation(reservation.getBody());
	}
	
	@Override
	@PostMapping("/updateSingleReservation")
	public Reservation updateSingleReservation(HttpEntity<Reservation> reservation) {
		return reservationManager.updateReservation(reservation.getBody());
	}
	
	@Override
	@PostMapping("/deleteReservation")
	public void deleteReservation(long id) {
		 reservationManager.deleteReservation(id);
	}
	
	@Override
	@PostMapping("/deleteRecurringReservation")
	public void deleteRecurringReservations(HttpEntity<Reservation> reservation) {
		 reservationManager.deleteRecurringReservation(reservation.getBody());
	}
	
	@Override
	@GetMapping(value="/getReservationsByOwner")
	public Set<Reservation> getByOwner(long id) {
		return reservationManager.getReservationsByOwner(id);
	}
	
	@Override
	@PostMapping("/createResource")
	public Resource createResource(HttpEntity<Resource> resource) {
		return resourceManager.createResource(resource.getBody());
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

	@Override
	@GetMapping(value="/getReservationsByResource")
	public Set<Reservation> getReservationsByResource(long id) {
		return reservationManager.getReservationsByResource(id);
	}

	@Override
	@PostMapping("/checkOldPassword")
	public boolean checkOldPassword(HttpEntity<User> user) {
		return loginManager.checkOldPassword(user.getBody());
	}

	@Override
	@PostMapping("/updatePassword")
	public boolean updatePassword(HttpEntity<User> user) {
		return loginManager.updatePassword(user.getBody());
	}

	@Override
	@PostMapping("/updateResource")
	public boolean updateResource(HttpEntity<Resource> resource) {
		 return resourceManager.updateResource(resource.getBody());
	}

	@Override
	@PostMapping("/deleteRelatedReservations")
	public void deleteRelatedReservations(HttpEntity<Resource> resource) {
		resourceManager.deleteRelatedReservations(resource.getBody());
	}

	@Override
	@GetMapping(value="/getRelatedResource")
	public Resource getRelatedResource(long id) {
		return reservationManager.getRelatedResource(id);
	}

	@Override
	@PostMapping(value="/checkReservation")
	public boolean checkAvailableResource(HttpEntity<Reservation> res) {
		return reservationManager.checkReservation(res.getBody());
	}
}
