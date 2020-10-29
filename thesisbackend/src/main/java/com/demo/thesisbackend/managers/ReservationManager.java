package com.demo.thesisbackend.managers;

import java.util.Set;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;

public interface ReservationManager {
	
	public Reservation createReservation(Reservation reservation);
	
	public Reservation updateReservation(Reservation reservation);
	
	public void deleteReservation(long id);
	
	public void deleteRecurringReservation(Reservation reservation);
	
	public Set<Reservation> getReservationsByOwner(long id);

	public Set<Reservation> getAllReservations();
	
	public Set<Reservation> getReservationsByResource(long id);
	
	public Resource getRelatedResource(long id);
	
	public Boolean checkReservation(Reservation res);
}
