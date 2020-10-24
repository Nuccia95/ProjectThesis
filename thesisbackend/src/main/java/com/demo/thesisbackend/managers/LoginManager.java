package com.demo.thesisbackend.managers;

import java.util.List;

import shared.thesiscommon.bean.User;

public interface LoginManager {
	
	public User login(User u);
	
	public User registration(User u);
	
	public List<String> getAllEmails();
	
	public String getUserName(long id);
}
