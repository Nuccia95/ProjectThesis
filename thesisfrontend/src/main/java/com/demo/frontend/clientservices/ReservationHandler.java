package com.demo.frontend.clientservices;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.Timezone;

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
		User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		ReservationDTO rdto = new ReservationDTO(reservation, u.getId());
		HttpEntity<ReservationDTO> request = new HttpEntity<>(rdto);
		Reservation r = restTemplate.postForObject(url + "createReservation", request, Reservation.class);
		return r;
	}
	
	public Reservation updateReservationDate(Reservation reservation) {
		User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		ReservationDTO rdto = new ReservationDTO(reservation, u.getId());
		HttpEntity<ReservationDTO> request = new HttpEntity<>(rdto);
		Reservation r = restTemplate.postForObject(url + "updateDate", request, Reservation.class);
		return r;
	}

	public void getReservationByOwner(){
		//User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
		String url1 = url + "getReservationsByOwner";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url1).queryParam("id", "1");
		
		Reservation[] reservations = restTemplate.getForObject(builder.toUriString(), Reservation[].class);
		for (Reservation reservation : reservations) {
			System.out.println(reservation.getResourceId().toString());
		}
		
	}
	
	/* Utils */
	public Reservation fromEntryToReservation(Entry entry) {	
		Reservation reservation = new Reservation();
		if(entry.getDescription() != null) {
			//the reservation is already saved
			reservation.setId(Long.parseLong(entry.getDescription()));
		}
		reservation.setColor(entry.getColor());
		reservation.setResourceId(entry.getTitle());
		if (entry.isRecurring()) {
			reservation.setRecurring(true);
			reservation.setStartTime(entry.getRecurringStartTime());
			reservation.setEndTime(entry.getRecurringEndTime());
		} else {
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
		entry.setEditable(reservation.isEditable());
		
		if (reservation.isRecurring()) {
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

	public void createRecurringReservation(FullCalendar calendar, Entry newEntry, Reservation reservation) {
		LocalDate start = newEntry.getRecurringStartDate(Timezone.UTC);
		LocalDate end = newEntry.getRecurringEndDate(Timezone.UTC);

		while (start.isBefore(end)) {
			if(start.equals(newEntry.getRecurringStartDate(Timezone.UTC))) {
				reservation.setFirstOfRecurrences(true);
				reservation.setEditable(true);
			}
			else {
				reservation.setFirstOfRecurrences(false);
				reservation.setEditable(false);
			}
				
			DayOfWeek day = start.getDayOfWeek();
			if (newEntry.getRecurringDaysOfWeeks().contains(day)) {
				reservation.setDayOfWeek(day.toString());
				reservation.setStartDate(start);
				reservation.setEndDate(newEntry.getRecurringEndDate(Timezone.UTC));
				Reservation r = createReservation(reservation);
				newEntry.setDescription(r.getId().toString());
				calendar.addEntry(newEntry);
			}
			start = start.plusDays(1);
		}
	}

}
