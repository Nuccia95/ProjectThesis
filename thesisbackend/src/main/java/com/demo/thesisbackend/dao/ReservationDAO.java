package com.demo.thesisbackend.dao;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.Reservation;
import shared.thesiscommon.User;

@Repository
public interface ReservationDAO extends CrudRepository<Reservation, Long> {
	
	Set<Reservation> findByOwner(User u);
	

}
