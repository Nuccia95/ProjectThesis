package com.demo.thesisbackend.dao;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.bean.Reservation;

@Repository
public interface ReservationDAO extends CrudRepository<Reservation, Long> {
	
	Set<Reservation> findByOwnerId(Long id);

	void deleteByGroupId(Long groupId);
	
}
