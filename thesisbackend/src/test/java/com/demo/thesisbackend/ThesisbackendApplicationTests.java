package com.demo.thesisbackend;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.UserDAO;

import shared.thesiscommon.Reservation;
import shared.thesiscommon.User;

@SpringBootTest
class ThesisbackendApplicationTests {

	@Autowired
	ReservationDAO bookDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Test
	void contextLoads() {
	}

	@Test
	void createBook() {
		User u = new User();
		u.setEmail("vvv@gmail.com");
		u.setFirstName("Nuccia");
		u.setLastName("Oliverio");
		u.setPassword("ciao");
		u.setRole("ciao");
		userDAO.save(u);
		
		Reservation b = new Reservation();
		b.setColor("red");
		LocalDate ld = LocalDate.now();
		b.setStartDate(ld);
		b.setEndDate(ld);
		u = userDAO.findByEmail("vvv@gmail.com");
		System.out.println(u.getEmail());
		
		b.setOwner(u);
		
		bookDAO.save(b);
		
	}

}
