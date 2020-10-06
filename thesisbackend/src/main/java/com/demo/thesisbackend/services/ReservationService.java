package com.demo.thesisbackend.services;

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
	
	public void deleteBook(Reservation book) {
		reservationDAO.delete(book);
	}

}
