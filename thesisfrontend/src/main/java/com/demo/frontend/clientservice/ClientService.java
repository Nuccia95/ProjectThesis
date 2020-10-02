package com.demo.frontend.clientservice;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

	private RestTemplate restTemplate = new RestTemplate();
	private final String url = "http://localhost:9999/";

	
	public String hello() {
	     return restTemplate.exchange(url + "hello",
	                HttpMethod.GET,
	                null,
	                String.class)
	                .getBody();

	 }
		
}
