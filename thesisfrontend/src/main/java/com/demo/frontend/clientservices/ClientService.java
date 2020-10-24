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
	public Reservation updateDate(HttpEntity<Reservation> reservation) {
		return restTemplate.postForObject(URL + "updateDate", reservation, Reservation.class);
	}

	@Override
	public Reservation updateSingleReservation(HttpEntity<Reservation> reservation) {
		return restTemplate.postForObject(URL + "updateSingleReservation", reservation, Reservation.class);
	}

	@Override
	public void deleteReservation(HttpEntity<Reservation> reservation) {
		restTemplate.postForObject(URL + "deleteReservation", reservation, Reservation.class);
	}

	@Override
	public void deleteRecurringReservations(HttpEntity<Reservation> reservation) {
		restTemplate.postForObject(URL + "deleteRecurringReservation", reservation, Reservation.class);
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
	public void deleteResource(HttpEntity<Resource> resource) {
		restTemplate.postForObject(URL + "deleteResource", resource, Resource.class);
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
		Set<Reservation> all = new HashSet<>();
		for (Reservation r : res)
			all.add(r);
		
		return all;
	}

	@Override
	public int getReservationsByResource(long id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "getReservationsByResource")
				.queryParam("id", String.valueOf(id));

		return restTemplate.getForObject(builder.toUriString(), Integer.class);
	}

	@Override
	public String getUserName(long id) {
		User u = CurrentUser.get();

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL + "getUserName")
				.queryParam("id", u.getId().toString());
		
		return restTemplate.getForObject(builder.toUriString(), String.class);
	}
}
