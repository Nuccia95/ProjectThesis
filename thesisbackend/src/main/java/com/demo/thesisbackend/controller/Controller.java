package com.demo.thesisbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.thesisbackend.services.LoginService;

import shared.thesiscommon.User;
@CrossOrigin("*")
@RestController
public class Controller {
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/hello")
	public String hello() {
		return loginService.hello();
	}

	@RequestMapping(value="/addUser", method = RequestMethod.POST)
	public User addUser(@RequestBody User user) {
		System.out.println("Sono: "+ user.getEmail());
		return loginService.addUser(user);
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public User login(@RequestBody User user) {
		return loginService.login(user);
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.POST)
	public User registration(@RequestBody User user) {
		return loginService.registration(user);
	}
	
}
