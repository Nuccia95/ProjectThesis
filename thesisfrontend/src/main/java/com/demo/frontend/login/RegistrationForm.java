package com.demo.frontend.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import shared.thesiscommon.bean.User;

public class RegistrationForm {

	private H2 title;
	private HorizontalLayout c;
	private HorizontalLayout c2;
	private HorizontalLayout c3;
	private VerticalLayout container;
	private TextField firstnameField;
	private TextField lastnameField;
	private TextField roleField;
	private EmailField emailField;
	private PasswordField passwordField1;
	private PasswordField passwordField2;
	private FormLayout registrationForm;
	private Button submitButton;
	
	public RegistrationForm() {}
	
	public void buildRegistrationForm() {
		title = new H2("SignUp");
		/* containers */
		c = new HorizontalLayout();
		c2 = new HorizontalLayout();
		c3 = new HorizontalLayout();
		container = new VerticalLayout();
		
		firstnameField = new TextField("First name");
		lastnameField = new TextField("Last name");
		roleField = new TextField("Role");
		emailField = new EmailField("Email");
		emailField.setRequiredIndicatorVisible(true);
		emailField.setPlaceholder("@");
		passwordField1 = new PasswordField("Wanted password");
		passwordField1.setRequiredIndicatorVisible(true);
		passwordField2 = new PasswordField("Password again");
		passwordField2.setRequiredIndicatorVisible(true);
		submitButton = new Button("SignUp");
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		c.add(firstnameField, lastnameField);
		c2.add(emailField, roleField);
		c3.add(passwordField1, passwordField2);
		container.add(c, c2, c3);
		container.setAlignItems(Alignment.CENTER);
		registrationForm = new FormLayout(title, container, submitButton);
		registrationForm.setMaxWidth("400px");
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
		u.setRole(roleField.getValue());
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
