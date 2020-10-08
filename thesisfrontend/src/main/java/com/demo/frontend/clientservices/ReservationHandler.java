package com.demo.frontend.clientservices;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.vaadin.stefan.fullcalendar.Entry;

import com.vaadin.flow.server.VaadinSession;

import shared.thesiscommon.Reservation;
import shared.thesiscommon.ReservationDTO;
import shared.thesiscommon.User;

@Service
public class ReservationHandler {

	@Autowired
	private RestTemplate restTemplate;
	private final String url = "http://localhost:9999/";

	public Reservation createReservation(Reservation reservation) {
		//User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		Long id = (long) 1;
		ReservationDTO rdto = new ReservationDTO(reservation, id);
		HttpEntity<ReservationDTO> request = new HttpEntity<>(rdto);
		Reservation r = restTemplate.postForObject(url + "createReservation", request, Reservation.class);
		return r;
	}
	
	public Reservation updateReservationDate(Reservation reservation) {
		//User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		Long id = (long) 1;
		ReservationDTO rdto = new ReservationDTO(reservation, id);
		HttpEntity<ReservationDTO> request = new HttpEntity<>(rdto);
		Reservation r = restTemplate.postForObject(url + "updateDate", request, Reservation.class);
		return r;
	}

	public Reservation[] getReservationByOwner(){
		//User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString( url + "getReservationsByOwner")
				.queryParam("id", "1");
		
		return restTemplate.getForObject(builder.toUriString(), Reservation[].class);
	}
	
	/* UTILS */
	public Reservation fromEntryToReservation(Entry entry) {	
		Reservation reservation = new Reservation();
		reservation.setColor(entry.getColor());
		reservation.setResourceId(entry.getTitle());
		reservation.setEditable(true);
		
		//The description of the entry corresponds to the reservationId
		if(entry.getDescription() != null)
			reservation.setId(Long.parseLong(entry.getDescription()));
		
		if (entry.isRecurring()) {
			reservation.setRecurring(true);
			reservation.setStartTime(entry.getRecurringStartTime());
			reservation.setEndTime(entry.getRecurringEndTime());
		}else {
			reservation.setStartDate(entry.getStart().toLocalDate());
			reservation.setEndDate(entry.getStart().toLocalDate());
			reservation.setStartTime(entry.getStart().toLocalTime());
			reservation.setEndTime(entry.getEnd().toLocalTime());
		}
		return reservation;
	}

	public Entry fromReservationToEntry(Reservation reservation) {
		Entry entry = new Entry();
		entry.setDescription(reservation.getId().toString());
		entry.setColor(reservation.getColor());
		entry.setTitle(reservation.getResourceId());
		entry.setEditable(reservation.isEditable());
		entry.setRecurring(reservation.isRecurring());
			
		LocalDateTime ldtstart = LocalDateTime.of(reservation.getStartDate(), reservation.getStartTime());
		LocalDateTime ldtend = LocalDateTime.of(reservation.getStartDate(), reservation.getEndTime());
		entry.setStart(ldtstart);
		entry.setEnd(ldtend);
		return entry;
	}
}
