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

import com.demo.thesisbackend.services.ReservationService;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.User;

import com.demo.thesisbackend.services.LoginService;


@CrossOrigin("*")
@RestController
public class Controller {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private ReservationService reservationService;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public User login(@RequestBody User user) {
		return loginService.login(user);
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.POST)
	public User registration(@RequestBody User user) {
		return loginService.registration(user);
	}
	
	@RequestMapping(value="/createReservation", method = RequestMethod.POST)
	public Reservation createReservation(@RequestBody Reservation reservation) {
		return reservationService.createReservation(reservation);
	}
	
	@RequestMapping(value="/updateDate", method = RequestMethod.POST)
	public Reservation updateDate(@RequestBody Reservation reservation) {
		return reservationService.updateDate(reservation);
	}
	
	@RequestMapping(value="/updateSingleReservation", method = RequestMethod.POST)
	public Reservation updateSingleReservation(@RequestBody Reservation reservation) {
		return reservationService.updateSingleReservation(reservation);
	}
	
	@RequestMapping(value="/deleteReservation", method = RequestMethod.POST)
	public void deleteReservation(@RequestBody Reservation reservation) {
		 reservationService.deleteReservation(reservation);
	}
	
	@RequestMapping(value="/deleteRecurringReservations", method = RequestMethod.POST)
	public void deleteRecurringReservations(@RequestBody Reservation reservation) {
		 reservationService.deleteRecurringReservations(reservation);
	}
	
	@GetMapping(value="/getReservationsByOwner")
	public Set<Reservation> updateDate(@RequestParam String id) {
		return reservationService.getReservationsByOwner(id);
	}
}
