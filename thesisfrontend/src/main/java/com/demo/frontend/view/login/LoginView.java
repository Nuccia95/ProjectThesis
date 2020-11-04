package com.demo.frontend.view.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import shared.thesiscommon.bean.User;
import shared.thesiscommon.utils.PasswordEncoder;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

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
	private WebServicesInterface clientService;

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
		
		Image logo = new Image("images/calogo.png", "My Project logo");
        logo.setId("logo-login");
		
        
        VerticalLayout cont1 = new VerticalLayout();
        cont1.setId("conttitle");
        cont1.setSpacing(false);
        Span welcome = new Span("Welcome in");
        Span title = new Span("GeRiCo");
        HorizontalLayout titleCont = new HorizontalLayout();
        titleCont.setAlignItems(Alignment.BASELINE);
        titleCont.add(title, logo);
        cont1.add(welcome, titleCont);
        
        Span info1 = new Span("Organize your work");
		Span info2 = new Span("Book resources");
		Span info3 = new Span("Invite your friends");
		VerticalLayout cont2 = new VerticalLayout();
		cont2.setSpacing(false);
		cont2.add(info1, info2, info3);
		cont2.setId("info-login");
		
		
		loginInformation.setAlignSelf(Alignment.CENTER, logo);
		
		loginInformation.add(cont1, cont2);
		
		return loginInformation;
	}	
	
	private void login(LoginForm.LoginEvent event) {
		User u = new User();
		
		u.setEmail(event.getUsername());
		byte[] password = PasswordEncoder.encode(event.getPassword());
		u.setPassword(password);
		
		HttpEntity<User> user = new HttpEntity<>(u);
		User userResult = clientService.login(user);
		if (userResult == null) {
			Notification.show("Error in login, try again");	
			loginForm.setEnabled(true);
		}
		else {
			CurrentUser.set(userResult);
			getUI().get().navigate("fullCalendarView");
		}
	}
}
