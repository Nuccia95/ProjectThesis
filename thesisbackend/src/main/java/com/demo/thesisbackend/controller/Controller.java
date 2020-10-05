package com.demo.thesisbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.thesisbackend.services.BookService;
import com.demo.thesisbackend.services.LoginService;

import shared.thesiscommon.Book;
import shared.thesiscommon.User;
@CrossOrigin("*")
@RestController
public class Controller {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/hello")
	public String hello() {
		return loginService.hello();
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public User login(@RequestBody User user) {
		return loginService.login(user);
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.POST)
	public User registration(@RequestBody User user) {
		return loginService.registration(user);
	}
	
	@RequestMapping(value="/createBook", method = RequestMethod.POST)
	public Book createBook(@RequestBody Book book) {
		return bookService.createBook(book);
	}
	
	@RequestMapping(value="/deleteBook", method = RequestMethod.POST)
	public void deleteBook(@RequestBody Book book) {
		bookService.deleteBook(book);
	}

}
