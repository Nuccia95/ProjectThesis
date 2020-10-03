package com.demo.thesisbackend.services;

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

	public User addUser(User user) {
		userDAO.save(user);
		System.out.print("sono dentro addUser");
		System.out.println("User: " + user.getEmail());
		return new User("ciccio@ciccio.it");
	}

	public User login(User user) {
		if (userDAO.existsById(user.getEmail())) {
			User u = new User();
			u = userDAO.findById(user.getEmail()).get();
			if (user.getPassword().equals(u.getPassword()))
				return u;
			else
				return null;
		}
		else
			return null;
	}

	public User registration(User user) {
		if (userDAO.existsById(user.getEmail()))
			return null;
		else
			userDAO.save(user);
		return user;
	}

}
