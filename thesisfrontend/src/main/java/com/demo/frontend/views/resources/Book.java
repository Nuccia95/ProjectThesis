package com.demo.frontend.views.resources;

import java.time.LocalDate;

public class Book {
	
	String resourceName;
	String user;
	LocalDate date;
	
	Book(String resourceName, String user, LocalDate date){
		this.resourceName = resourceName;
		this.user = user;
		this.date = date;
	}
	
	public String getName() {
		return resourceName;
	}
	
	public void setName(String name) {
		this.resourceName = name;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

}
