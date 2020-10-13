package com.demo.thesisbackend.managers;

import shared.thesiscommon.bean.User;

public interface LoginManager {
	
	public User login(User u);
	
	public User registration(User u);

}
