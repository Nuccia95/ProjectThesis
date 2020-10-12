package com.demo.frontend.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo.frontend.clientservices.LoginHandler;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import shared.thesiscommon.bean.User;

/**
 * UI content when the user is not logged in yet.
 */
@Route("")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
public class LoginView extends FlexLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginForm loginForm;
	private RegistrationForm registrationForm;
	private FlexLayout centeringLayout;
	private boolean logFormVisible;
	@Autowired
	private LoginHandler loginHandler;

	public LoginView() {
		centeringLayout = new FlexLayout();
		centeringLayout.setSizeFull();
		centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		centeringLayout.setAlignItems(Alignment.CENTER);
		buildUI();
	}
	
	/*Layout*/
	private void buildUI() {
		setSizeFull();
		setClassName("login-screen");
		Component panel = buildPanel();
		loginForm = new LoginForm();
		registrationForm = new RegistrationForm();
		registrationForm.buildRegistrationForm();
		registration();
		centeringLayout.add(registrationForm.getForm());
		add(centeringLayout);
		registrationForm.getForm().setVisible(false);
		logFormVisible = true;
		builLoginForm();
		add(panel);
		add(centeringLayout);
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
		title.setWidth("100%");

		H2 info = new H2("You don't have an account?");
		Button registrationButton = new Button("SignUp");
		registrationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		registrationButton.addClickListener(e -> {
			if (logFormVisible) {
				loginForm.setVisible(false);
				logFormVisible = false;
			}
			registrationForm.getForm().setVisible(true);
		});

		Button loginButton = new Button("Login", VaadinIcon.SIGN_IN.create());
		loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		loginButton.addClickListener(e -> {
			if (!logFormVisible) {
				registrationForm.getForm().setVisible(false);
				loginForm.setVisible(true);
				logFormVisible = true;
			}
		});
		loginInformation.add(title);
		loginInformation.add(info, registrationButton, loginButton);
		return loginInformation;
	}	
	
	/*Methods*/
	private void login(LoginForm.LoginEvent event) {
		User u = new User();
		u.setEmail(event.getUsername());
		u.setPassword(event.getPassword());
		getUI().get().navigate("fullCalendarView");
		User userResult = loginHandler.login(u);
		if (userResult == null)
			Notification.show("Error in login, try again");
		else {
			CurrentUser.set(userResult, true);
			getUI().get().navigate("fullCalendarView");
		}
	}
	
	public void registration() {
		registrationForm.getSubmitButton().addClickListener(e -> {
			if (!registrationForm.passwordsMatch())
				Notification.show("Passwords don't match");
			else {
				User userResult = loginHandler.registration(registrationForm.getUserForm());
				if (userResult == null)
					Notification.show("Error in registration, try again");
				else {
					CurrentUser.set(userResult, true);
					getUI().get().navigate("fullCalendarView");
				}
			}
		});
	}
}
