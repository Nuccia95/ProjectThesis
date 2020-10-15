package com.demo.frontend.clientservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.frontend.view.login.CurrentUser;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.User;

@Service
public class ReservationService {

	@Autowired
	private RestTemplate restTemplate;
	private static final String URL = "http://localhost:9999/";

	public Reservation createReservation(Reservation reservation) {
		reservation.setOwner(CurrentUser.get());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		return restTemplate.postForObject(URL + "createReservation", request, Reservation.class);
	}
	
	public Reservation updateReservationDate(Reservation reservation) {
		reservation.setOwner(CurrentUser.get());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		return restTemplate.postForObject(URL + "updateDate", request, Reservation.class);
	}
	
	public Reservation updateSingleReservation(Reservation reservation) {
		reservation.setOwner(CurrentUser.get());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		return restTemplate.postForObject(URL + "updateSingleReservation", request, Reservation.class);
	}

	public Reservation[] getReservationByOwner(){
		User u = CurrentUser.get();
		
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString( URL + "getReservationsByOwner")
				.queryParam("id", u.getId().toString());
		
		Reservation[] res = restTemplate.getForObject(builder.toUriString(), Reservation[].class);
		
		return res;
	}
	
	public void deleteReservation(Reservation reservation) {
		reservation.setOwner(CurrentUser.get());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		restTemplate.postForObject(URL + "deleteReservation", request, Reservation.class);
	}
	
	public void deleteRecurringReservations(Reservation reservation) {
		reservation.setOwner(CurrentUser.get());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		restTemplate.postForObject(URL + "deleteRecurringReservation", request, Reservation.class);
	}
}
