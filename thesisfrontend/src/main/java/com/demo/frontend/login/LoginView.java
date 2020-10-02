package com.demo.frontend.login;


import org.springframework.beans.factory.annotation.Autowired;

import com.demo.frontend.clientservice.ClientService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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
	private FormLayout registrationForm;
	private FlexLayout centeringLayout;
	@Autowired
	private ClientService cs;
    
	public LoginView() {
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
        builLoginForm();
        add(panel);
        add(centeringLayout);
    }
    
    private void builLoginForm() {
    	 loginForm = new LoginForm();
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
        	loginForm.setVisible(false); 
        	buildRegistratioForm();});
        
        Button loginButton = new Button("Login", VaadinIcon.SIGN_IN.create());
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.addClickListener(e -> {
        	registrationForm.setVisible(false); 
        	builLoginForm();});
        
        loginInformation.add(title);
        loginInformation.add(info, registrationButton, loginButton);

        return loginInformation;
    }
   
    private void login(LoginForm.LoginEvent event) { 	
    	getUI().get().navigate("main");
        /*if (/*accessControl.signIn(event.getUsername(), event.getPassword())) {
            getUI().get().navigate("");
        } else {
            event.getSource().setError(true);
        }*/
    }
    
    private void buildRegistratioForm() {
    	H2 title = new H2("SignUp");

        TextField firstnameField = new TextField("First name");
        TextField lastnameField = new TextField("Last name");
        TextField roleField = new TextField("Role");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField1 = new PasswordField("Wanted password");
        PasswordField passwordField2 = new PasswordField("Password again");

        Span errorMessage = new Span();

        Button submitButton = new Button("SignUp");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

     
        registrationForm = new FormLayout(title, firstnameField, lastnameField, emailField, 
        		roleField, passwordField1, passwordField2, errorMessage, submitButton);
        registrationForm.setMaxWidth("500px");
        registrationForm.getStyle().set("margin", "0 auto");
        registrationForm.setColspan(title, 2);
        registrationForm.setColspan(errorMessage, 2);
        registrationForm.setColspan(submitButton, 2);

        centeringLayout.add(registrationForm);
        
        // Add some styles to the error message to make it pop out
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        add(centeringLayout);
    }
    
    
}
