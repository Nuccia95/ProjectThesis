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
import com.demo.thesisbackend.services.LoginService;

import shared.thesiscommon.Reservation;
import shared.thesiscommon.ReservationDTO;
import shared.thesiscommon.User;
@CrossOrigin("*")
@RestController
public class Controller {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ReservationService reservationService;
	
	@GetMapping("/hello")
	public String hello() {
		return loginService.hello();
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public User login(@RequestBody User user) {
		return loginService.login(user);
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.POST)
	public User registration(@RequestBody User user) {
		return loginService.registration(user);
	}
	
	@RequestMapping(value="/createReservation", method = RequestMethod.POST)
	public Reservation createBook(@RequestBody ReservationDTO reservationDTO) {
		return reservationService.createReservation(reservationDTO);
	}
	
	@RequestMapping(value="/updateDate", method = RequestMethod.POST)
	public Reservation updateDate(@RequestBody ReservationDTO reservationDTO) {
		return reservationService.updateDate(reservationDTO);
	}
	
	@GetMapping(value="/getReservationsByOwner")
	public Set<Reservation> updateDate(@RequestParam String id) {
		System.out.println("controller");
		return reservationService.getReservationsByOwner(id);
	}


	
}
