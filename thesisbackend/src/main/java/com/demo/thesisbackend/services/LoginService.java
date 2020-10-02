package com.demo.thesisbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.thesisbackend.dao.UserDAO;

@Service
public class LoginService {

	@Autowired
	private UserDAO userDAO;
	
	
	public String hello() {
		System.out.print("HELLO NUCCIA");
		return "OK - CI SIAMO CONNESSI";
	}
	
}
