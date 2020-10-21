package com.demo.thesisbackend;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.thesisbackend.dao.ReservationDAO;
import com.demo.thesisbackend.dao.UserDAO;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.User;
import shared.thesiscommon.utils.PasswordEncoder;
import shared.thesiscommon.utils.TextUtils;

@SpringBootTest
class ThesisbackendApplicationTests {

	@Autowired
	ReservationDAO bookDAO;
	
	@Autowired
	UserDAO userDAO;

}