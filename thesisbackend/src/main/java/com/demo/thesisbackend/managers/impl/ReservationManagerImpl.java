package com.demo.thesisbackend.managers.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

		Reservation created = reservationDAO.save(reservation);

		if (created.getGroupId() == 0 && !reservation.getReceivers().isEmpty()) {
			EmailManager emailManager = new EmailManager();
			emailManager.sendEmail(created);
		}

		return created;
	}

	@Override
	public Reservation updateReservation(Reservation reservation) {
		Optional<Reservation> r = reservationDAO.findById(reservation.getId());
		if (r.isPresent()) {
			Reservation res = r.get();
			Resource resource = resourceDAO.findByName(reservation.getResource().getName());
			res.setResource(resource);
			res.setTitle(reservation.getTitle());
			res.setColor(reservation.getColor());
			res.setStartDate(reservation.getStartDate());
			res.setEndDate(reservation.getEndDate());
			res.setStartTime(reservation.getStartTime());
			res.setEndTime(reservation.getEndTime());
			Reservation updated = reservationDAO.save(res);
			
			if (!reservation.getReceivers().isEmpty()) {
				EmailManager emailManager = new EmailManager();
				emailManager.sendEmail(updated);
			}
			
			return updated;
		}
		return null;
	}

	@Override
	public long deleteReservation(long id) {
		reservationDAO.deleteById(id);
		return id;
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
	public Set<Reservation> getReservationsByResource(long id) {
		Iterable<Reservation> res = reservationDAO.findByResourceId(id);
		Set<Reservation> reservationsList = new HashSet<>();
		for (Reservation reservation : res)
			if (reservation.getStartDate().isAfter(LocalDate.now()))
				reservationsList.add(reservation);
		return reservationsList;
	}

	@Override
	public Resource getRelatedResource(long id) {

		Optional<Reservation> reservation = reservationDAO.findById(id);
		if (reservation.isPresent()) {
			Reservation r = reservation.get();
			Optional<Resource> resource = resourceDAO.findById(r.getResource().getId());
			if (resource.isPresent())
				return resource.get();
		}

		return null;
	}

	@Override
	public Boolean checkReservation(Reservation res) {

		LocalDateTime startNew = LocalDateTime.of(res.getStartDate(), res.getStartTime());
		LocalDateTime endNew = LocalDateTime.of(res.getEndDate(), res.getEndTime());

		Resource relatedResource = resourceDAO.findByName(res.getResource().getName());
		Set<Reservation> others = getReservationsByResource(relatedResource.getId());

		for (Reservation r : others) {

			if (!r.getId().equals(res.getId()) && r.getStartDate().equals(res.getStartDate()) && r.getEndDate().equals(res.getEndDate())) {

				LocalDateTime startOther = LocalDateTime.of(res.getStartDate(), r.getStartTime());
				LocalDateTime endOther = LocalDateTime.of(r.getEndDate(), r.getEndTime());

				if ((startNew.isEqual(startOther) && endNew.isEqual(endOther))
						|| (startNew.isAfter(startOther) && endNew.isBefore(endOther))
						|| (startNew.isBefore(startOther) && endNew.isAfter(endOther))
						|| (endNew.isBefore(endOther) && endNew.isAfter(startOther))
						|| (startNew.isAfter(startOther) && startNew.isBefore(endOther)))
					return false;
			}

		}

		return true;
	}

	@Override
	public Reservation getReservationById(long id) {
		Optional<Reservation> res = reservationDAO.findById(id);
		if (res.isPresent())
			return res.get();
		return null;
	}

	@Override
	public Reservation getReservationByTitle(String title) {
		Reservation res = reservationDAO.findByTitle(title);
		if (res != null)
			return res;
		return null;
	}

	@Override
	public void notifyEnabledResource(Resource res) {
		Set<Reservation> reservationsList = getReservationsByResource(res.getId());
		List<String> usersToNotify = new ArrayList<>();
		
		for (Reservation r: reservationsList) {
			if(!usersToNotify.contains(r.getOwner().getEmail()))
				usersToNotify.add(r.getOwner().getEmail());
		}
		
		EmailManager emailManager = new EmailManager();
		emailManager.sendEmailEnabledResource(usersToNotify, res);
		
	}

}
