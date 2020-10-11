package com.demo.thesisbackend.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.bean.Resource;


@Repository
public interface ResourceDAO extends CrudRepository<Resource, Long>{
	
	Resource findByName(String name);

}
