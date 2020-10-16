package com.demo.thesisbackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.bean.Profile;

@Repository
public interface ProfileDAO extends CrudRepository<Profile, Long> {

	Profile findByName(String name);
}
