package com.demo.thesisbackend.managers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.ResourceDAO;
import com.demo.thesisbackend.managers.ResourceManager;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;

@Component
public class ResourceManagerImpl implements ResourceManager {

	@Autowired
	private ResourceDAO resourceDAO;
	@Autowired
	private ReservationDAO reservationDAO;

	@Override
	public Resource createResource(Resource res) {
		Resource r = resourceDAO.findByName(res.getName());
		if (r == null) {
			res.setEnable(true);
			return resourceDAO.save(res);
		}
		return null;
	}

	@Override
	public List<String> getResourcesNames() {
		Iterable<Resource> resources = resourceDAO.findAll();
		List<String> resourcesNames = new ArrayList<>();
		for (Resource resource : resources) {
			if(Boolean.TRUE.equals(resource.getEnable()))
					resourcesNames.add(resource.getName());
		}
		return resourcesNames;
	}

	@Override
	public List<Resource> getAllResources() {
		Iterable<Resource> resources = resourceDAO.findAll();
		List<Resource> resourcesNames = new ArrayList<>();
		for (Resource resource : resources)
			resourcesNames.add(resource);
		
		return resourcesNames;
	}

	@Override
	public Resource updateResource(Resource res) {
		Optional<Resource> r = resourceDAO.findById(res.getId());
		if(r.isPresent()) {
			Resource resource = r.get();
			if(res.getDisabledUntil()!=null)
				resource.setDisabledUntil(res.getDisabledUntil());
			resource.setEnable(res.getEnable());
			return resourceDAO.save(resource);
		}
		return null;
	}

	@Override
	public void deleteRelatedReservations(Resource res) {
		/* delete all reservations related to this resource */
		Iterable<Reservation> reservations = reservationDAO.findAll();
		for (Reservation reservation : reservations) {
			if(reservation.getResource().getId().equals(res.getId()))
				reservationDAO.deleteById(reservation.getId());
		}
	}
	
}