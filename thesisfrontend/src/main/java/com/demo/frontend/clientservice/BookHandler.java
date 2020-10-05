package com.demo.frontend.clientservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import shared.thesiscommon.Book;

@Service
public class BookHandler {
	
	@Autowired
	private RestTemplate restTemplate;
	private final String url = "http://localhost:9999/";
	
	public Book creteBook(Book book) {
		HttpEntity<Book> request = new HttpEntity<>(book);
		Book b = restTemplate.postForObject(url + "createBook", request, Book.class);
		return b;
	}
	
	public Book mapToBook(Entry entry) {
		Book b = new Book();
		b.setColor(entry.getColor());
		b.setResourceId(entry.getTitle());;
	
		if(entry.isRecurring()) {
			b.setRecurring(1);
			b.setStartDate(entry.getRecurringStartDate(Timezone.UTC));
			b.setEndDate(entry.getRecurringEndDate(Timezone.UTC));
			b.setStartTime(entry.getRecurringStartTime());
			b.setEndTime(entry.getRecurringEndTime());
			
		}else {
			b.setStartDate(entry.getStart().toLocalDate());
			b.setStartTime(entry.getStart().toLocalTime());
			b.setEndTime(entry.getEnd().toLocalTime());
		}
		return b;
	}

}
