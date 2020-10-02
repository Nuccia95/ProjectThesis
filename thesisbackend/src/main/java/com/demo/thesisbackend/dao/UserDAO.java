package com.demo.thesisbackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.User;


@Repository
public interface UserDAO extends CrudRepository<User, String> {

}
