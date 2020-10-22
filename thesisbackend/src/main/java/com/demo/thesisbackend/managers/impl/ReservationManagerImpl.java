package com.demo.thesisbackend.managers.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.ResourceDAO;
import com.demo.thesisbackend.emails.EmailManager;
import com.demo.thesisbackend.managers.ReservationManager;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;

@Component
public class ReservationManagerImpl implements ReservationManager {

	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private ResourceDAO resourceDAO;

	@Override
	public Reservation createReservation(Reservation reservation) {
		Resource resource = resourceDAO.findByName(reservation.getResource().getName());
		reservation.setResource(resource);
		
		if(reservation.getGroupId() == 0){
			EmailManager emailManager = new EmailManager();
			emailManager.sendEmail(reservation);
		}

		return reservationDAO.save(reservation);
	}

	@Override
	public Reservation updateReservation(Reservation reservation) {
		Optional<Reservation> r = reservationDAO.findById(reservation.getId());
		if (r.isPresent()) {
			Reservation res = r.get();
			Resource resource = resourceDAO.findByName(reservation.getResource().getName());
			res.setResource(resource);
			res.setColor(reservation.getColor());
			res.setStartDate(reservation.getStartDate());
			res.setEndDate(reservation.getEndDate());
			res.setStartTime(reservation.getStartTime());
			res.setEndTime(reservation.getEndTime());
			return reservationDAO.save(res);
		}
		return null;
	}

	@Override
	public Reservation updateDate(Reservation reservation) {
		Optional<Reservation> r = reservationDAO.findById(reservation.getId());
		if(r.isPresent()) {
			Reservation res = r.get();
			res.setStartDate(reservation.getStartDate());
			res.setEndDate(reservation.getStartDate());			
			return reservationDAO.save(res);
		}
		return null;
	}

	@Override
	public void deleteReservation(Reservation reservation) {
		reservationDAO.deleteById(reservation.getId());

	}

	@Override
	public void deleteRecurringReservation(Reservation reservation) {
		Set<Reservation> res = reservationDAO.findByOwnerId(reservation.getOwner().getId());
		for (Reservation r : res)
			if (r.getGroupId() == reservation.getId())
				reservationDAO.deleteById(r.getId());
		reservationDAO.deleteById(reservation.getId());
	}

	@Override
	public Set<Reservation> getReservationsByOwner(long id) {
		return reservationDAO.findByOwnerId(id);
	}

	@Override
	public Set<Reservation> getAllReservations() {
		Set<Reservation> allres = new HashSet<>();
		Iterable<Reservation> all = reservationDAO.findAll();
		for (Reservation reservation : all)
			allres.add(reservation);
		
		return allres;
 	}

	@Override
	public int getReservationsByResource(long id) {
		Iterable<Reservation> res  = reservationDAO.findByResourceId(id);
		int count = 0;
		for (Reservation reservation : res)
			if(reservation.getStartDate().isAfter(LocalDate.now()))
				count ++;
		return count;
	}

}
