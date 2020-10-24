package com.demo.thesisbackend.managers.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.thesisbackend.dao.ProfileDAO;
import com.demo.thesisbackend.dao.UserDAO;
import com.demo.thesisbackend.managers.LoginManager;

import shared.thesiscommon.bean.Profile;
import shared.thesiscommon.bean.User;

@Component
public class LoginManagerImpl implements LoginManager {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ProfileDAO profileDAO;

	@Override
	public User login(User user) {
		User u = userDAO.findByEmail(user.getEmail());
		if (u != null)
			if (Arrays.equals(user.getPassword(), u.getPassword()))
				return u;
		return null;
	}

	@Override
	public User registration(User user) {

		User u = userDAO.findByEmail(user.getEmail());
		Profile profile = null;

		if (u == null) {
			switch (user.getProfile().getName()) {
			case User.USER_PROFILE:
				profile = profileDAO.findByName(User.USER_PROFILE);
				break;
			case User.VIEWER_PROFILE:
				profile = profileDAO.findByName(User.VIEWER_PROFILE);
				break;
			default:
				break;
			}

			if (profile != null)
				user.setProfile(profile);
			user.setAdmin(Boolean.FALSE);
		}

		return userDAO.save(user);
	}

	@Override
	public List<String> getAllEmails() {
		Iterable<User> users = userDAO.findAll();
		List<String> emails = new ArrayList<>();
		for (User user : users)
			emails.add(user.getEmail());
		return emails;
	}

	@Override
	public String getUserName(long id) {
		if (userDAO.existsById(id)) {
			User u = userDAO.findById(id).get();
			return u.getFirstName() + " " + u.getLastName();
		}
		return null;
	}
}
