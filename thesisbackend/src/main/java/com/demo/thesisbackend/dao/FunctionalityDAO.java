package com.demo.thesisbackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.bean.Functionality;

@Repository
public interface FunctionalityDAO extends CrudRepository<Functionality, Long>{
	
	Functionality findByName(String name);
	
}
