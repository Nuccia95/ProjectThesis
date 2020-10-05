package com.demo.thesisbackend.dao;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shared.thesiscommon.Book;
import shared.thesiscommon.User;

@Repository
public interface BookDAO extends CrudRepository<Book, Long> {
	
	Set<Book> findByOwner(User u);

}
