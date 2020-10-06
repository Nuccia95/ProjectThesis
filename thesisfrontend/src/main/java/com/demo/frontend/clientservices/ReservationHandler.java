package com.demo.frontend.clientservices;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import shared.thesiscommon.Reservation;
import shared.thesiscommon.ReservationDTO;

@Service
public class ReservationHandler {
	
	@Autowired
	private RestTemplate restTemplate;
	private final String url = "http://localhost:9999/";
	
	public Reservation createReservation(ReservationDTO reservationDTO){
		HttpEntity<ReservationDTO> request = new HttpEntity<>(reservationDTO);
		Reservation r = restTemplate.postForObject(url + "createReservation", request, Reservation.class);
		return r;
		
	}
	
	public Reservation fromEntryToReservation(Entry entry) {
		Reservation reservation = new Reservation();
		reservation.setColor(entry.getColor());
		reservation.setResourceId(entry.getTitle());;
		
		if(entry.isRecurring()) {
			reservation.setRecurring(1);
			reservation.setStartDate(entry.getRecurringStartDate(Timezone.UTC));
			reservation.setEndDate(entry.getRecurringEndDate(Timezone.UTC));
			reservation.setStartTime(entry.getRecurringStartTime());
			reservation.setEndTime(entry.getRecurringEndTime());
			
		}else {
			reservation.setStartDate(entry.getStart().toLocalDate());
			reservation.setStartTime(entry.getStart().toLocalTime());
			reservation.setEndTime(entry.getEnd().toLocalTime());
		}
		return reservation;
	}
	
	public Entry fromReservationToEntry(Reservation reservation) {
		Entry entry = new Entry();
		entry.setColor(reservation.getColor());
		entry.setTitle(reservation.getResourceId());

		if (reservation.getRecurring() == 1) {
			entry.setRecurring(true);
			entry.setRecurringStartDate(reservation.getStartDate(), Timezone.UTC);
			entry.setRecurringStartTime(reservation.getStartTime());
			entry.setRecurringEndDate(reservation.getEndDate(), Timezone.UTC);
			entry.setRecurringEndTime(reservation.getEndTime());
		} else {
			LocalDateTime ldtstart = LocalDateTime.of(reservation.getStartDate(), reservation.getStartTime());
			LocalDateTime ldtend = LocalDateTime.of(reservation.getEndDate(), reservation.getEndTime());
			entry.setStart(ldtstart);
			entry.setEnd(ldtend);
		}
		return entry;
	}

}
