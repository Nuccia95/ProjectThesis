package com.demo.thesisbackend.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.ResourceDAO;
import com.demo.thesisbackend.dao.UserDAO;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.bean.User;

@Service
public class ReservationService {

	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ResourceDAO resourceDAO;

	public Reservation createReservation(Reservation reservation) {
		User owner = userDAO.findById(reservation.getOwnerId()).get();
		Resource resource = resourceDAO.findByName(reservation.getResourceName());
		reservation.setOwner(owner);
		reservation.setResource(resource);
		return reservationDAO.save(reservation);
	}

	public Reservation updateDate(Reservation reservation) {
		Reservation r = reservationDAO.findById(reservation.getId()).get();
		r.setStartDate(reservation.getStartDate());
		r.setEndDate(reservation.getStartDate());
		return reservationDAO.save(r);
	}

	public Reservation updateSingleReservation(Reservation reservation) {
		Reservation r = reservationDAO.findById(reservation.getId()).get();
		Resource resource = resourceDAO.findByName(reservation.getResourceName());
		r.setResource(resource);
		r.setColor(reservation.getColor());
		r.setStartDate(reservation.getStartDate());
		r.setEndDate(reservation.getEndDate());
		r.setStartTime(reservation.getStartTime());
		r.setEndTime(reservation.getEndTime());
		return reservationDAO.save(r);
	}

	public Set<Reservation> getReservationsByOwner(String id) {
		Long ownerId = Long.parseLong(id);
		User u = userDAO.findById(ownerId).get();
		return reservationDAO.findByOwner(u);
	}

	public void deleteReservation(Reservation reservation) {
		reservationDAO.deleteById(reservation.getId());
	}

	public void deleteRecurringReservations(Reservation reservation) {
		User u = userDAO.findById(reservation.getOwnerId()).get();
		Set<Reservation> res = reservationDAO.findByOwner(u);
		for (Reservation r : res)
			if (r.getGroupId() == reservation.getId())
				reservationDAO.deleteById(r.getId());

		reservationDAO.deleteById(reservation.getId());
	}

}
