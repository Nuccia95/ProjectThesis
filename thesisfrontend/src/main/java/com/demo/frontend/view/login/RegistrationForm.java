package com.demo.frontend.view.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import shared.thesiscommon.bean.Profile;
import shared.thesiscommon.bean.User;
import shared.thesiscommon.utils.PasswordEncoder;

public class RegistrationForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField firstnameField;
	private TextField lastnameField;
	private EmailField emailField;
	private ComboBox<String> roles;
	private PasswordField passwordField1;
	private PasswordField passwordField2;
	private Button submitButton;
	
	public RegistrationForm() {
		//nothing to do
	}
	
	public void buildRegistrationForm() {
		
		HorizontalLayout c = new HorizontalLayout();
		HorizontalLayout c2 = new HorizontalLayout();
		HorizontalLayout c3 = new HorizontalLayout();
		
		VerticalLayout container = new VerticalLayout();
		container.setSpacing(false);
		
		firstnameField = new TextField("First name");
		
		lastnameField = new TextField("Last name");
		
		emailField = new EmailField("Email");
		emailField.setPlaceholder("@");
		
		roles = new ComboBox<>();
		roles.setLabel("Roles");
		roles.setItems(User.USER_PROFILE, User.VIEWER_PROFILE);
		
		passwordField1 = new PasswordField("Wanted password");
		
		passwordField2 = new PasswordField("Password again");
		
		submitButton = new Button("Add", VaadinIcon.PLUS_CIRCLE.create());
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitButton.getElement().getStyle().set("paddin", "15px");
		
		c.add(firstnameField, lastnameField);
		c2.add(emailField, roles);
		c3.add(passwordField1, passwordField2);
		container.add(c, c2, c3, submitButton);
		container.setAlignSelf(Alignment.CENTER, submitButton);
		
		add(container);
	}

	public User getUserForm() {
		User u = new User();
		u.setEmail(emailField.getValue());
		u.setFirstName(firstnameField.getValue());
		u.setLastName(lastnameField.getValue());
		Profile profile = new Profile();
		profile.setName(roles.getValue());
		u.setProfile(profile);
		byte[] password = PasswordEncoder.encode(passwordField1.getValue());
		u.setPassword(password);
		return u;
	}
	
	public void cleanForm() {
		firstnameField.clear();
		lastnameField.clear();
		emailField.clear();
		roles.clear();
		passwordField1.clear();
		passwordField2.clear();
	}
	
	public boolean passwordsMatch() {
		return passwordField1.getValue().equals(passwordField2.getValue());
	}
	
	public Button getSubmitButton() {
		return submitButton;
	}
}
