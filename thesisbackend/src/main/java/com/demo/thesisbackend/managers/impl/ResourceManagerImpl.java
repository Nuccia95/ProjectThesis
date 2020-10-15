package com.demo.thesisbackend.managers.impl;

import java.util.ArrayList;
import java.util.List;

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
		if (r == null)
			return resourceDAO.save(res);
		return null;
	}

	@Override
	public void deleteResource(Resource res) {
		
		/* delete all reservations related to this resource */
		Iterable<Reservation> reservations = reservationDAO.findAll();
		for (Reservation reservation : reservations) {
			if(reservation.getResource().getId().equals(res.getId()))
				reservationDAO.deleteById(reservation.getId());
		}
		/* delete the resource */
		resourceDAO.deleteById(res.getId());
	}

	@Override
	public List<String> getResourcesNames() {
		Iterable<Resource> resources = resourceDAO.findAll();
		List<String> resourcesNames = new ArrayList<>();
		for (Resource resource : resources)
			resourcesNames.add(resource.getName());
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
	
}