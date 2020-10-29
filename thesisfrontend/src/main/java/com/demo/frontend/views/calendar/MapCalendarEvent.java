package com.demo.frontend.views.calendar;

import java.time.LocalDateTime;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;

public class MapCalendarEvent {
	
	public Reservation mapEntryToReservation(Entry entry, String relatedResource) {	
		Reservation reservation = new Reservation();

		Resource resource = new Resource();
		reservation.setResource(resource);
		reservation.getResource().setName(relatedResource);

		reservation.setTitle(entry.getTitle());
		reservation.setColor(entry.getColor());
		reservation.setEditable(true);
		
		if (entry.isRecurring()) {
			reservation.setRecurring(true);
			reservation.setStartDate(entry.getRecurringStartDate(Timezone.UTC));
			reservation.setStartTime(entry.getRecurringStartTime());
			reservation.setEndDate(entry.getRecurringEndDate(Timezone.UTC));
			reservation.setEndTime(entry.getRecurringEndTime());
			if(entry.getDescription() != null)
				reservation.setGroupId(Long.parseLong(entry.getDescription()));
		}else {
			reservation.setStartDate(entry.getStart().toLocalDate());
			reservation.setStartTime(entry.getStart().toLocalTime());
			reservation.setEndDate(entry.getEnd().toLocalDate());
			reservation.setEndTime(entry.getEnd().toLocalTime());
		}
		return reservation;
	}

	public Entry mapReservationToEntry(Reservation reservation) {
		Entry entry = new Entry(reservation.getId().toString());
		
		entry.setTitle(reservation.getTitle());
		
		Long groupId = reservation.getGroupId();
		if(groupId != null)
			entry.setDescription(String.valueOf(groupId));
		
		entry.setRecurring(reservation.isRecurring());
		entry.setColor(reservation.getColor());
		
		entry.setEditable(reservation.isEditable());
			
		LocalDateTime ldtstart = LocalDateTime.of(reservation.getStartDate(), reservation.getStartTime());
		LocalDateTime ldtend = LocalDateTime.of(reservation.getStartDate(), reservation.getEndTime());
		
		entry.setStart(ldtstart);
		entry.setEnd(ldtend);
		return entry;
	}
	
	

}
