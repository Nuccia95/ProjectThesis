package com.demo.thesisbackend.managers.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.ResourceDAO;
import com.demo.thesisbackend.dao.UserDAO;
import com.demo.thesisbackend.managers.ReservationManager;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.bean.User;
@Component
public class ReservationManagerImpl implements ReservationManager{
	
	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ResourceDAO resourceDAO;

	@Override
	public Reservation createReservation(Reservation reservation) {
		User owner = userDAO.findById(reservation.getOwnerId()).get();
		Resource resource = resourceDAO.findByName(reservation.getResourceName());
		reservation.setOwner(owner);
		reservation.setResource(resource);
		return reservationDAO.save(reservation);
	}

	@Override
	public Reservation updateReservation(Reservation reservation) {
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

	@Override
	public Reservation updateDate(Reservation reservation) {
		Reservation r = reservationDAO.findById(reservation.getId()).get();
		r.setStartDate(reservation.getStartDate());
		r.setEndDate(reservation.getStartDate());
		return reservationDAO.save(r);
	}

	@Override
	public void deleteReservation(Reservation reservation) {
		reservationDAO.deleteById(reservation.getId());
		
	}

	@Override
	public void deleteRecurringReservation(Reservation reservation) {
		User u = userDAO.findById(reservation.getOwnerId()).get();
		Set<Reservation> res = reservationDAO.findByOwner(u);
		for (Reservation r : res)
			if (r.getGroupId() == reservation.getId())
				reservationDAO.deleteById(r.getId());
		reservationDAO.deleteById(reservation.getId());
	}

	@Override
	public Set<Reservation> getReservationsByOwner(String id) {
		User u = userDAO.findById(Long.parseLong(id)).get();
		return reservationDAO.findByOwner(u);
	}

}
