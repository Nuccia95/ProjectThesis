package com.demo.frontend.clientservices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.frontend.view.login.CurrentUser;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.bean.User;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

@Service
public class ClientService implements WebServicesInterface {

	@Autowired
	private RestTemplate restTemplate;
	private static final String URL = "http://localhost:9999/";

	@Override
	public User login(HttpEntity<User> user) {
		return restTemplate.postForObject(URL + "login", user, User.class);
	}

	@Override
	public User registration(HttpEntity<User> user) {
		return restTemplate.postForObject(URL + "registration", user, User.class);
	}

	@Override
	public Reservation createReservation(HttpEntity<Reservation> reservation) {
		return restTemplate.postForObject(URL + "createReservation", reservation, Reservation.class);
	}

	@Override
	public Reservation updateSingleReservation(HttpEntity<Reservation> reservation) {
		return restTemplate.postForObject(URL + "updateSingleReservation", reservation, Reservation.class);
	}

	@Override
	public long deleteReservation(long id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "deleteReservation")
				.queryParam("id", String.valueOf(id));

		return restTemplate.getForObject(builder.toUriString(), Long.class);
	}

	@Override
	public void deleteRecurringReservations(HttpEntity<Reservation> reservation) {
		restTemplate.postForObject(URL + "deleteRecurringReservation", reservation, Void.class);
	}

	@Override
	public Set<Reservation> getByOwner(long id) {
		User u = CurrentUser.get();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "getReservationsByOwner")
				.queryParam("id", u.getId().toString());

		Reservation[] res = restTemplate.getForObject(builder.toUriString(), Reservation[].class);
		Set<Reservation> resByOwner = new HashSet<>();
		for (Reservation r : res)
			resByOwner.add(r);
		
		return resByOwner;
	}

	@Override
	public Resource createResource(HttpEntity<Resource> resource) {
		return restTemplate.postForObject(URL + "createResource", resource, Resource.class);
	}

	@Override
	public List<Resource> getAllResources() {
		List<Resource> resources = new ArrayList<>();
		Resource[] res = restTemplate.getForObject(URL + "getAllResources", Resource[].class);
		for (Resource r: res)
			resources.add(r);
		return resources;
	}

	@Override
	public List<String> getResourcesNames() {
		List<String> resources = new ArrayList<>();
		String[] res = restTemplate.getForObject(URL + "getResourcesNames", String[].class);
		for (String resName : res)
			resources.add(resName);
		return resources;
	}

	@Override
	public List<String> getAllEmails() {
		List<String> emails = new ArrayList<>();
		String[] allEmails = restTemplate.getForObject(URL + "getAllEmails", String[].class);
		for (String email : allEmails)
			emails.add(email);
		return emails;
	}

	@Override
	public Set<Reservation> getAllReservations() {
		Reservation[] res = restTemplate.getForObject(URL + "getAllReservations", Reservation[].class);
		Set<Reservation> list = new HashSet<>();
		for (Reservation reservation : res)
			list.add(reservation);
		return list;
	}

	@Override
	public Set<Reservation> getReservationsByResource(long id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "getReservationsByResource")
				.queryParam("id", String.valueOf(id));

		Reservation[] res = restTemplate.getForObject(builder.toUriString(), Reservation[].class);
		Set<Reservation> list = new HashSet<>();
		for (Reservation reservation : res)
			list.add(reservation);
		return list;
	}

	@Override
	public boolean checkOldPassword(HttpEntity<User> user) {
		return restTemplate.postForObject(URL + "checkOldPassword", user, Boolean.class);
	}

	@Override
	public boolean updatePassword(HttpEntity<User> user) {
		return restTemplate.postForObject(URL + "updatePassword", user, Boolean.class);
	}

	@Override
	public boolean updateResource(HttpEntity<Resource> resource) {
		return restTemplate.postForObject(URL + "updateResource", resource, Boolean.class);
	}

	@Override
	public void deleteRelatedReservations(HttpEntity<Resource> resource) {
		restTemplate.postForLocation(URL + "deleteRelatedReservations", resource);
	}

	@Override
	public Resource getRelatedResource(long id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "getRelatedResource")
				.queryParam("id", String.valueOf(id));

		return restTemplate.getForObject(builder.toUriString(), Resource.class);
	}

	@Override
	public boolean checkAvailableResource(HttpEntity<Reservation> res) {
		return restTemplate.postForObject(URL + "checkReservation", res, Boolean.class);
	}

	@Override
	public Reservation getReservationById(long id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "getReservationById")
				.queryParam("id", String.valueOf(id));
		return restTemplate.getForObject(builder.toUriString(), Reservation.class);
	}
}
