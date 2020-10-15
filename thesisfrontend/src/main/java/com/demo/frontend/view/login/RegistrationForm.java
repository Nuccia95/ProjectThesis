package com.demo.frontend.view.login;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import shared.thesiscommon.bean.User;

@CssImport("./styles/views/login/login-view.css")
public class RegistrationForm {

	private H2 title;
	private HorizontalLayout c;
	private HorizontalLayout c2;
	private HorizontalLayout c3;
	private VerticalLayout container;
	private TextField firstnameField;
	private TextField lastnameField;
	private EmailField emailField;
	private PasswordField passwordField1;
	private PasswordField passwordField2;
	private FormLayout registrationForm;
	private Button submitButton;
	private AppButton appButton;
	
	public RegistrationForm() {
		appButton = new AppButton();
		c = new HorizontalLayout();
		c2 = new HorizontalLayout();
		c3 = new HorizontalLayout();
	}
	
	public void buildRegistrationForm() {
		title = new H2("Sign up");
		/* containers */
		
		c2.setSizeFull();
		container = new VerticalLayout();
		container.setId("registration-form");
		container.setSpacing(false);
		
		firstnameField = new TextField("First name");
		lastnameField = new TextField("Last name");
		
		emailField = new EmailField("Email");
		emailField.setSizeFull();
		emailField.setRequiredIndicatorVisible(true);
		emailField.setPlaceholder("@");
		
		passwordField1 = new PasswordField("Wanted password");
		passwordField1.setRequiredIndicatorVisible(true);
		passwordField2 = new PasswordField("Password again");
		passwordField2.setRequiredIndicatorVisible(true);
		
		submitButton = appButton.set("SignUp", VaadinIcon.SIGN_IN.create());
		submitButton.setSizeFull();
		
		c.add(firstnameField, lastnameField);
		c2.add(emailField);
		c3.add(passwordField1, passwordField2);
		container.add(title, c, c2, c3, submitButton);
		container.setAlignSelf(Alignment.CENTER, submitButton);
		container.setAlignSelf(Alignment.START, title);
		
		registrationForm = new FormLayout(container);
		registrationForm.setColspan(submitButton, 2);
			
	}
	
	public FormLayout getForm() {
		return registrationForm;
	}

	public User getUserForm() {
		User u = new User();
		u.setEmail(emailField.getValue());
		u.setFirstName(firstnameField.getValue());
		u.setLastName(lastnameField.getValue());
		u.setPassword(passwordField1.getValue());	
		return u;
	}
	
	public boolean passwordsMatch() {
		return passwordField1.getValue().equals(passwordField2.getValue());
	}
	
	public Button getSubmitButton() {
		return submitButton;
	}
}
