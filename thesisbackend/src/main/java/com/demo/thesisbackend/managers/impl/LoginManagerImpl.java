package com.demo.thesisbackend.managers.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.thesisbackend.dao.UserDAO;
import com.demo.thesisbackend.managers.LoginManager;

import shared.thesiscommon.bean.User;

@Component
public class LoginManagerImpl implements LoginManager {

	@Autowired
	private UserDAO userDAO;
	private String admin = "nuccia@gmail.com";
	private String adminPass = "nuccia";

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
			if (email.equals(admin) && password.equals(adminPass))
				user.setAdmin(true);
			else
				user.setAdmin(false);
			return userDAO.save(user);
		}
		return null;
	}

	@Override
	public List<String> getAllEmails() {
		Iterable<User> users = userDAO.findAll();
		List<String> emails = new ArrayList<>();
		for (User user : users)
			emails.add(user.getEmail());
		return emails;
	}

}
