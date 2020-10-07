package com.demo.thesisbackend.services;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.UserDAO;

import shared.thesiscommon.Reservation;
import shared.thesiscommon.ReservationDTO;
import shared.thesiscommon.User;

@Service
public class ReservationService {

	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private UserDAO userDAO;

	public Reservation createReservation(ReservationDTO reservationDTO) {
		User u = userDAO.findById(reservationDTO.getOwnerId()).get();
		Reservation r = reservationDTO.getReservation();
		r.setOwner(u);
		return reservationDAO.save(r);
	}
	
	public Reservation updateDate(ReservationDTO reservationDTO) {
		Long id = reservationDTO.getReservation().getId();
		LocalDate newDate = reservationDTO.getReservation().getStartDate();
		Reservation r = reservationDAO.findById(id).get();
		r.setStartDate(newDate);
		return reservationDAO.save(r);
	}
	
	public Set<Reservation> getReservationsByOwner(String id){
		Long ownerId = Long.parseLong(id);
		System.out.println("Owner id: " + ownerId);
		User u = userDAO.findById(ownerId).get();
		return reservationDAO.findByOwner(u);
	}
	
}
