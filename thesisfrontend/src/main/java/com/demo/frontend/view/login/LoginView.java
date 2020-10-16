package com.demo.frontend.view.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import com.demo.frontend.clientservices.ClientService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.User;

/**
 * UI content when the user is not logged in yet.
 */
@Route("")
@PageTitle("Login")
@CssImport("./styles/views/login/login-view.css")
public class LoginView extends FlexLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ClientService clientService;

	private LoginForm loginForm;
	private FlexLayout centeringLayout;

	public LoginView() {
		setId("login-view");
		centeringLayout = new FlexLayout();
		centeringLayout.setSizeFull();
		centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		centeringLayout.setAlignItems(Alignment.CENTER);
		buildUI();
	}
	
	
	private void buildUI() {
		setSizeFull();
		setClassName("login-screen");
		Component panel = buildPanel();
		loginForm = new LoginForm();
		builLoginForm();
		add(panel, centeringLayout);
	}

	private void builLoginForm() {
		loginForm.addLoginListener(this::login);
		loginForm.setForgotPasswordButtonVisible(false);
		centeringLayout.add(loginForm);
	}

	private Component buildPanel() {
		VerticalLayout loginInformation = new VerticalLayout();
		loginInformation.setClassName("login-information");

		H1 title = new H1("Welcome in APP-NAME");
		H2 info = new H2("description");

		loginInformation.add(title);
		loginInformation.add(info);
		return loginInformation;
	}	
	
	private void login(LoginForm.LoginEvent event) {
		User u = new User();
		u.setEmail(event.getUsername());
		u.setPassword(event.getPassword());
		HttpEntity<User> user = new HttpEntity<>(u);
		User userResult = clientService.login(user);
		if (userResult == null) {
			Notification.show("Error in login, try again");	
			getUI().get().navigate("");
		}
		else {
			CurrentUser.set(userResult, true);
			System.out.println(CurrentUser.get().getEmail());
			getUI().get().navigate("fullCalendarView");
		}
	}
	
	/*public void registration() {
		registrationForm.getSubmitButton().addClickListener(e -> {
			if (!registrationForm.passwordsMatch())
				Notification.show("Passwords don't match");
			else {
				User userResult = loginService.registration(registrationForm.getUserForm());
				if (userResult == null) {
					Notification.show("Error in registration, try again");
					getUI().get().navigate("");
				}
				else {
					CurrentUser.set(userResult, true);
					getUI().get().navigate("fullCalendarView");
				}
			}
		});
	}*/
}