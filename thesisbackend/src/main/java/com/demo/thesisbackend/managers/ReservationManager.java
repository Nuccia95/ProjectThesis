package com.demo.thesisbackend.managers;

import java.util.Set;

import shared.thesiscommon.bean.Reservation;

public interface ReservationManager {
	
	public Reservation createReservation(Reservation reservation);
	
	public Reservation updateReservation(Reservation reservation);
	
	public Reservation updateDate(Reservation reservation);
	
	public void deleteReservation(Reservation reservation);
	
	public void deleteRecurringReservation(Reservation reservation);
	
	public Set<Reservation> getReservationsByOwner(final long id);

	public Set<Reservation> getAllReservations();
	

}
