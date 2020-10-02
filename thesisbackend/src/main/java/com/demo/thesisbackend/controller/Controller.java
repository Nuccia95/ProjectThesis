package com.demo.thesisbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.thesisbackend.services.LoginService;
@CrossOrigin("*")
@RestController
public class Controller {
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/hello")
	public String hello() {
		return loginService.hello();
	}

	/*@PostMapping("/login")
		public User login(@RequestBody User user) {
		return loginService.login(user);
	}*/
	
	/*@PostMapping("/register")
	public User register() {
		user.setEmail("giampaolo@gmail.com");
		user.setFirstName("Giampaolo");
		user.setLastName("Rossi");
		return loginService.register(user);
	}*/
	
	
}
