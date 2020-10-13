package com.demo.thesisbackend.managers.impl;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.thesisbackend.dao.UserDAO;
import com.demo.thesisbackend.managers.LoginManager;

import shared.thesiscommon.bean.User;

@Component
public class LoginManagerImpl implements LoginManager {

	@Autowired
	private UserDAO userDAO;
	private String ADMIN = "nuccia@gmail.com";
	private String ADMIN_PASS = "nuccia";

	@Override
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

	@Override
	public User registration(User user) {
		String email = user.getEmail();
		String password = user.getPassword();

		User u = userDAO.findByEmail(user.getEmail());
		if (u == null) {
			if (email.equals(ADMIN) && password.equals(ADMIN_PASS)) {
				user.setRole("ADMIN");
				System.out.println(user.getRole());
			} else
				user.setRole("USER");
			user.setCreatedBooks(new HashSet<>());
			return userDAO.save(user);
		} else
			return null;
	}

}