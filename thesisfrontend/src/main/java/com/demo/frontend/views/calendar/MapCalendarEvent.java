package com.demo.frontend.views.calendar;

import java.time.LocalDateTime;

import org.vaadin.stefan.fullcalendar.Entry;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;

public class MapCalendarEvent {
	

	public Reservation mapEntryToReservation(Entry entry) {	
		Reservation reservation = new Reservation();
		Resource resource = new Resource();
		reservation.setResource(resource);

		reservation.setColor(entry.getColor());
		reservation.getResource().setName(entry.getTitle());
		reservation.setEditable(true);
		
		if (entry.isRecurring()) {
			reservation.setRecurring(true);
			reservation.setStartTime(entry.getRecurringStartTime());
			reservation.setEndTime(entry.getRecurringEndTime());
			if(entry.getDescription() != null)
				reservation.setGroupId(Long.parseLong(entry.getDescription()));
		}else {
			reservation.setStartDate(entry.getStart().toLocalDate());
			reservation.setStartTime(entry.getStart().toLocalTime());
			reservation.setEndDate(entry.getStart().toLocalDate());
			reservation.setEndTime(entry.getEnd().toLocalTime());
		}
		return reservation;
	}

	public Entry mapReservationToEntry(Reservation reservation) {
		Entry entry = new Entry(reservation.getId().toString());
		
		entry.setTitle(reservation.getResource().getName());
		
		Long groupId = reservation.getGroupId();
		if(groupId != null)
			entry.setDescription(String.valueOf(groupId));
		
		entry.setRecurring(reservation.isRecurring());
		entry.setColor(reservation.getColor());
		
		entry.setTitle(reservation.getResource().getName());
		entry.setEditable(reservation.isEditable());
			
		LocalDateTime ldtstart = LocalDateTime.of(reservation.getStartDate(), reservation.getStartTime());
		LocalDateTime ldtend = LocalDateTime.of(reservation.getStartDate(), reservation.getEndTime());
		
		entry.setStart(ldtstart);
		entry.setEnd(ldtend);
		return entry;
	}
	
	

}
