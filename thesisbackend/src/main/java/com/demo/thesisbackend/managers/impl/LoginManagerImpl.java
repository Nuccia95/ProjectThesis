package com.demo.thesisbackend.managers.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
		if (u != null && Arrays.equals(user.getPassword(), u.getPassword()))
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

			if (profile != null) {
				user.setProfile(profile);
				user.setAdmin(Boolean.FALSE);				
				return userDAO.save(user);
			}
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

	@Override
	public boolean updatePassword(User u) {
		Optional<User> user = userDAO.findById(u.getId());
		if (user.isPresent()) {
			User updatedUser = user.get();
			updatedUser.setPassword(u.getPassword());
			userDAO.save(updatedUser);
			return true;
		}
		return false;
	}

	@Override
	public boolean checkOldPassword(User u) {
		Optional<User> user = userDAO.findById(u.getId());
		if (user.isPresent()) {
			User userSaved = user.get();
			if (Arrays.equals(userSaved.getPassword(), u.getPassword()))
				return true;
		}
		return false;
	}

}
