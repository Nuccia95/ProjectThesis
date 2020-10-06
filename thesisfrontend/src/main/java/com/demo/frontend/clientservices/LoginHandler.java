package com.demo.frontend.clientservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import shared.thesiscommon.User;

@Service
public class LoginHandler {

	@Autowired
	private RestTemplate restTemplate;
	private final String url = "http://localhost:9999/";

	/*
	 * public String hello() { return restTemplate.exchange(url + "hello",
	 * HttpMethod.GET, null, String.class) .getBody();
	 * 
	 * }*/
	
	public User login(User u) {
		HttpEntity<User> request = new HttpEntity<>(u);
		User user = restTemplate.postForObject(url + "login", request, User.class);
		assert(user.getEmail()!=null);
		return user;
	}

	public User registration(User u) {
		HttpEntity<User> request = new HttpEntity<>(u);
		User user = restTemplate.postForObject(url + "registration", request, User.class);
		assert(user.getEmail()!=null);
		return user;
	}
	
}