package com.demo.frontend.clientservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import shared.thesiscommon.bean.User;

@Service
public class LoginService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private RestTemplate restTemplate;
	private static final String URL = "http://localhost:9999/";

	
	public User login(User u) {
		HttpEntity<User> request = new HttpEntity<>(u);
		return restTemplate.postForObject(URL + "login", request, User.class);
	}

	public User registration(User u) {
		HttpEntity<User> request = new HttpEntity<>(u);
		return restTemplate.postForObject(URL + "registration", request, User.class);
	}
	
	public List<String> getAllEmails() {
		List<String> emails = new ArrayList<>();
		String[] allEmails = restTemplate.getForObject(URL + "getAllEmails", String[].class);
		for (String email : allEmails)
			emails.add(email);
		return emails;
	}
	
}