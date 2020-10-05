package com.demo.thesisbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.thesisbackend.dao.BookDAO;
import com.demo.thesisbackend.dao.UserDAO;

import shared.thesiscommon.Book;

@Service
public class BookService {

	@Autowired
	private BookDAO bookDAO;
	@Autowired
	private UserDAO userDAO;

	public Book createBook(Book book) {
		Book newBook = bookDAO.save(book);
		return newBook;
	}
	
	public void deleteBook(Book book) {
		bookDAO.delete(book);
	}

}
