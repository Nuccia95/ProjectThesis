package com.demo.thesisbackend.services;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.thesisbackend.dao.UserDAO;
import shared.thesiscommon.User;

@Service
public class LoginService {

	@Autowired
	private UserDAO userDAO;

	public String hello() {
		System.out.print("HELLO NUCCIA");
		return "OK - CI SIAMO CONNESSI";
	}

	public User login(User user) {
		User u = userDAO.findByEmail(user.getEmail());
		if (u != null) {
			if (user.getPassword().equals(u.getPassword()))
				return u;
			else
				return null;
		}
		return null;
	}

	public User registration(User user) {
		User u = userDAO.findByEmail(user.getEmail());
		if (u == null) {
			user.setCreatedBooks(new HashSet<>());
			return userDAO.save(user);
		}
		else
			return null;
	}
}
