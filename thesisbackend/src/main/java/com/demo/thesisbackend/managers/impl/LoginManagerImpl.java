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
			if(Arrays.equals(user.getPassword(), u.getPassword()))
				return u;
		return null;
	}

	@Override
	public User registration(User user) {
		
		User u = userDAO.findByEmail(user.getEmail());
		if(u == null) {
			switch (user.getRole()) {
			case "VIEWER":
				Profile viewerProfile = profileDAO.findByName(User.VIEWER_USERNAME);
				user.setUsername(User.ADMIN_USERNAME);
				user.setProfile(viewerProfile);
				break;
			case "MANAGER":
				Profile managerProfile = profileDAO.findByName(User.VIEWER_USERNAME);
				user.setUsername(User.VIEWER_USERNAME);
				user.setProfile(managerProfile);
				break;
			default:
				break;
			}
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

}
