package com.demo.frontend.prova;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo.frontend.clientservice.LoginHandler;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;

@Route(value = "prova")
public class ProvaView extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public ProvaView(@Autowired ClientService clientService) {
		String response = clientService.hello();
		add(new H1(response));
	}*/
}