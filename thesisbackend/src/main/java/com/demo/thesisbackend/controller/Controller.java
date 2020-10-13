package com.demo.thesisbackend.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.User;
import com.demo.thesisbackend.managers.LoginManager;
import com.demo.thesisbackend.managers.ReservationManager;


@CrossOrigin("*")
@RestController
public class Controller {
	
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ReservationManager reservationManager;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public User login(@RequestBody User user) {
		return loginManager.login(user);
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.POST)
	public User registration(@RequestBody User user) {
		return loginManager.registration(user);
	}
	
	@RequestMapping(value="/createReservation", method = RequestMethod.POST)
	public Reservation createReservation(@RequestBody Reservation reservation) {
		return reservationManager.createReservation(reservation);
	}
	
	@RequestMapping(value="/updateDate", method = RequestMethod.POST)
	public Reservation updateDate(@RequestBody Reservation reservation) {
		return reservationManager.updateDate(reservation);
	}
	
	@RequestMapping(value="/updateSingleReservation", method = RequestMethod.POST)
	public Reservation updateSingleReservation(@RequestBody Reservation reservation) {
		return reservationManager.updateReservation(reservation);
	}
	
	@RequestMapping(value="/deleteReservation", method = RequestMethod.POST)
	public void deleteReservation(@RequestBody Reservation reservation) {
		 reservationManager.deleteReservation(reservation);
	}
	
	@RequestMapping(value="/deleteRecurringReservation", method = RequestMethod.POST)
	public void deleteRecurringReservations(@RequestBody Reservation reservation) {
		 reservationManager.deleteRecurringReservation(reservation);
	}
	
	@GetMapping(value="/getReservationsByOwner")
	public Set<Reservation> updateDate(@RequestParam String id) {
		return reservationManager.getReservationsByOwner(id);
	}
}
