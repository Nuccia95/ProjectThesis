package com.demo.thesisbackend;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.thesisbackend.emails.EmailManager;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.User;

@SpringBootTest
@SpringBootApplication
class ThesisbackendApplicationTests {

	@Test
	void prova() {
	
//		LocalDateTime startNew = LocalDateTime.of(LocalDate.of(2020, 11, 1), LocalTime.of(8, 00));
//		LocalDateTime endNew = LocalDateTime.of(LocalDate.of(2020, 11, 1), LocalTime.of(13, 00));
//		
//		LocalDateTime startOther = LocalDateTime.of(LocalDate.of(2020, 11, 1), LocalTime.of(12, 00));
//		LocalDateTime endOther = LocalDateTime.of(LocalDate.of(2020, 11, 1), LocalTime.of(16, 10));
		
//		if(startNew.isEqual(startOther) && endNew.isEqual(endOther))
//			System.out.println("no");
//		
//		if(startNew.isAfter(startOther) && endNew.isBefore(endOther))
//			System.out.println("no");
//		
//		if(startNew.isBefore(startOther) && endNew.isAfter(endOther))
//			System.out.println("no");
//		
//		if(endNew.isBefore(endOther) && endNew.isAfter(startOther))
//			System.out.println("no");
//		
//		if(startNew.isAfter(startOther) && startNew.isBefore(endOther))
//			System.out.println("no");
		
	}
	
	@Test 
	void email() {
		EmailManager emailManager = new EmailManager();
		User u = new User();
		u.setFirstName("Nuccia");
		u.setLastName("Oliverio");
		u.setEmail("nucciaoliverio95@gmail.com");
		
		List<String> emails = new ArrayList<>();
		emails.add(u.getEmail());
		
		Reservation res = new Reservation();
		res.setTitle("Reunion - Lab");
		res.setStartDate(LocalDate.of(2020, 11, 3));
		res.setStartTime(LocalTime.of(17, 00));
		res.setEndDate(LocalDate.of(2020, 10, 30));
		res.setEndTime(LocalTime.of(19, 00));
		res.setOwner(u);
		res.setReceivers(emails);
		
		emailManager.sendEmail(res);
	}
	
}