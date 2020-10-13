package com.demo.frontend.clientservices;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.vaadin.stefan.fullcalendar.Entry;

import com.demo.frontend.view.login.CurrentUser;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.User;

@Service
public class ReservationService {

	@Autowired
	private RestTemplate restTemplate;
	private final String url = "http://localhost:9999/";

	public Reservation createReservation(Reservation reservation) {
		reservation.setOwnerId(CurrentUser.get().getId());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		Reservation r = restTemplate.postForObject(url + "createReservation", request, Reservation.class);
		return r;
	}
	
	public Reservation updateReservationDate(Reservation reservation) {
		reservation.setOwnerId(CurrentUser.get().getId());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		Reservation r = restTemplate.postForObject(url + "updateDate", request, Reservation.class);
		return r;
	}
	
	public Reservation updateSingleReservation(Reservation reservation) {
		reservation.setOwnerId(CurrentUser.get().getId());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		Reservation r = restTemplate.postForObject(url + "updateSingleReservation", request, Reservation.class);
		return r;
	}

	public Reservation[] getReservationByOwner(){
		User u = CurrentUser.get();
		System.out.println(u);
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString( url + "getReservationsByOwner")
				.queryParam("id", u.getId().toString());
		return restTemplate.getForObject(builder.toUriString(), Reservation[].class);
	}
	
	public void deleteReservation(Reservation reservation) {
		reservation.setOwnerId(CurrentUser.get().getId());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		restTemplate.postForObject(url + "deleteReservation", request, Reservation.class);
	}
	
	public void deleteRecurringReservations(Reservation reservation) {
		reservation.setOwnerId(CurrentUser.get().getId());
		HttpEntity<Reservation> request = new HttpEntity<>(reservation);
		restTemplate.postForObject(url + "deleteRecurringReservation", request, Reservation.class);
	}
	
	/* UTILS */
	public Reservation mapEntryToReservation(Entry entry) {	
		Reservation reservation = new Reservation();
		reservation.setColor(entry.getColor());
		reservation.setResourceName(entry.getTitle());
		reservation.setEditable(true);
		
		if (entry.isRecurring()) {
			reservation.setRecurring(true);
			reservation.setStartTime(entry.getRecurringStartTime());
			reservation.setEndTime(entry.getRecurringEndTime());
			if(entry.getDescription() != null)
				reservation.setGroupId(Long.parseLong(entry.getDescription()));
		}else {
			reservation.setStartDate(entry.getStart().toLocalDate());
			reservation.setEndDate(entry.getStart().toLocalDate());
			reservation.setStartTime(entry.getStart().toLocalTime());
			reservation.setEndTime(entry.getEnd().toLocalTime());
		}
		return reservation;
	}

	public Entry mapReservationToEntry(Reservation reservation) {
		Entry entry = new Entry(reservation.getId().toString());
		
		Long groupId = reservation.getGroupId();
		if(groupId != null)
			entry.setDescription(String.valueOf(groupId));
		
		entry.setRecurring(reservation.isRecurring());
		entry.setColor(reservation.getColor());
		entry.setTitle(reservation.getResourceName());
		entry.setEditable(reservation.isEditable());
			
		LocalDateTime ldtstart = LocalDateTime.of(reservation.getStartDate(), reservation.getStartTime());
		LocalDateTime ldtend = LocalDateTime.of(reservation.getStartDate(), reservation.getEndTime());
		entry.setStart(ldtstart);
		entry.setEnd(ldtend);
		return entry;
	}
}
