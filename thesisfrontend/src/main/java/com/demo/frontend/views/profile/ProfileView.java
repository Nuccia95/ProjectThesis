package com.demo.frontend.views.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import com.demo.frontend.view.login.CurrentUser;
import com.demo.frontend.view.login.RegistrationForm;
import com.demo.frontend.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.User;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

@Route(value = "profileView", layout = MainView.class)
@PageTitle("Profile")
@CssImport("./styles/views/profile/profile-view.css")
public class ProfileView extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WebServicesInterface clientService;
	
	private Image avatar;
	
	public ProfileView() {
		setId("profile-view");
		setSpacing(true);
		setJustifyContentMode(JustifyContentMode.BETWEEN);

		add(buildInfo());
		
		if(CurrentUser.isAdmin())
			add(buildRegistrationForm());
	}
	
	public VerticalLayout buildInfo(){
		
		VerticalLayout infoContainer = new VerticalLayout();
		infoContainer.setId("infocontainer");
		infoContainer.setSpacing(false);

		H3 title = new H3("Profile");
		HorizontalLayout titleContainer = new HorizontalLayout();

		titleContainer.add(title, getAvatar());
		infoContainer.add(titleContainer, setText("First Name:", CurrentUser.get().getFirstName()),
				setText("Last Name:", CurrentUser.get().getLastName()),
				setText("Email:", CurrentUser.get().getEmail()),
				setText("Role:", CurrentUser.get().getProfile().getName()));
		return infoContainer;
	}
	
	public VerticalLayout buildRegistrationForm() {
		
		VerticalLayout formContainer = new VerticalLayout();
		formContainer.setId("formcontainer");
		formContainer.setSpacing(false);
		
		H3 title = new H3("Add new user");
		
		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.buildRegistrationForm();
		formContainer.add(title, registrationForm);
		formContainer.setAlignSelf(Alignment.CENTER, registrationForm);
		
		registrationForm.getSubmitButton().addClickListener(ev -> {
			
			User u = registrationForm.getUserForm();
			HttpEntity<User> user = new HttpEntity<>(u);
			clientService.registration(user);
			
			registrationForm.cleanForm();
			Notification.show(u.getFirstName() + " " + u.getLastName() + "ADDED");
		});
		
		return formContainer;
	}
	
	
	public TextField setText(String label, String value) {
		TextField tf = new TextField();
		tf.setLabel(label);
		tf.setValue(value);
		tf.setReadOnly(true);
		tf.setSizeFull();
		return tf;
	}
	
	public Image getAvatar() {
		switch (CurrentUser.get().getProfile().getName()) {
		case User.ADMIN_PROFILE:
			avatar = new Image("images/admin.png", "Avatar");
			avatar.setId("avatar");
			break;
		case User.USER_PROFILE:
			avatar = new Image("images/simpleUser.png", "Avatar");
			avatar.setId("avatar");
			break;
		case User.VIEWER_PROFILE:
			avatar = new Image("images/viewer.png", "Avatar");
			avatar.setId("avatar");
			break;
		default:
			break;
		}
		return avatar;
	}

}
