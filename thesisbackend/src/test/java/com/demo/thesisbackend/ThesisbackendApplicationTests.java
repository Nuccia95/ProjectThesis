package com.demo.thesisbackend;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.thesisbackend.emails.EmailManager;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
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
}