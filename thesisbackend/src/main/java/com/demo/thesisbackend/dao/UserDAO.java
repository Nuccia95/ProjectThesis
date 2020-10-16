package com.demo.thesisbackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.bean.User;



@Repository
public interface UserDAO extends CrudRepository<User, Long> {
	
	User findByEmail(String email);
	
	User findByUsername(String username);

}
