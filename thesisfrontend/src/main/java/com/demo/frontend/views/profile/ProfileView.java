package com.demo.frontend.views.profile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.view.login.CurrentUser;
import com.demo.frontend.view.login.RegistrationForm;
import com.demo.frontend.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.User;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

@Route(value = "profileView", layout = MainView.class)
@PageTitle("Profile")
@CssImport("./styles/views/profile/profile-view.css")
public class ProfileView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	private WebServicesInterface clientService;

	private Tab tab1;
	private VerticalLayout page1;
	private Tab tab2;
	private VerticalLayout page2;
	private Tab tab3;
	private VerticalLayout page3;
	private Map<Tab, Component> tabsToPages;
	private AppButton appButton;

	public ProfileView() {
		setSizeFull();
		setId("profile-view");
		setSpacing(true);
		setAlignItems(Alignment.CENTER);

		tabsToPages = new HashMap<>();

		tab1 = new Tab("Profile Information");
		page1 = buildInfo();
		tab2 = new Tab("Add New User");
		page2 = buildRegistrationForm();
		page2.setVisible(false);
		// tab3 = new Tab("Setting");
		// page3 = settingsForm();
		// page3.setVisible(false);
		tab1.getElement().getStyle().set("color", " #1f3d7a");
		tab2.getElement().getStyle().set("color", " #1f3d7a");
		// tab3.getElement().getStyle().set("color"," #1f3d7a");
		Tabs tabs = new Tabs();

		if (CurrentUser.isAdmin()) {
			tabsToPages.put(tab1, page1);
			tabsToPages.put(tab2, page2);
			// tabsToPages.put(tab3, page3);
			tabs.add(tab1, tab2);
		} else
			tabs.add(tab1);

		tabs.setFlexGrowForEnclosedTabs(1);

		Div pages = new Div(page1, page2);

		tabs.addSelectedChangeListener(event -> {
			tabsToPages.values().forEach(page -> page.setVisible(false));
			Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);
		});

		add(tabs, pages);
	}

	public VerticalLayout buildInfo() {

		VerticalLayout infoContainer = new VerticalLayout();
		infoContainer.setId("infocontainer");
		infoContainer.setSpacing(false);
		infoContainer.setWidth("450px");
		infoContainer.setHeight("400px");

		H4 title = new H4("Profile");
		HorizontalLayout titleContainer = new HorizontalLayout();

		titleContainer.add(title, getAvatar());
		infoContainer.add(titleContainer, setText("First Name:", CurrentUser.get().getFirstName()),
				setText("Last Name:", CurrentUser.get().getLastName()), setText("Email:", CurrentUser.get().getEmail()),
				setText("Role:", CurrentUser.get().getProfile().getName()));

		infoContainer.setAlignSelf(Alignment.CENTER, titleContainer);
		return infoContainer;
	}

	public VerticalLayout buildRegistrationForm() {

		VerticalLayout formContainer = new VerticalLayout();
		formContainer.setId("formcontainer");
		formContainer.setSpacing(false);

		H4 title = new H4("Add New User");
		HorizontalLayout titleContainer = new HorizontalLayout();
		Icon users = VaadinIcon.USERS.create();
		users.setId("users");
		titleContainer.add(title, users);
		titleContainer.setAlignItems(Alignment.BASELINE);

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.buildRegistrationForm();

		appButton = new AppButton();
		Button submitButton = appButton.set("Add", VaadinIcon.PLUS_CIRCLE.create());
		
		formContainer.add(titleContainer, registrationForm, submitButton);
		formContainer.setAlignItems(Alignment.CENTER);
		formContainer.setAlignSelf(Alignment.END, submitButton);


		submitButton.addClickListener(ev -> {
			User u = registrationForm.getUserForm();
			if (u != null) {
				HttpEntity<User> user = new HttpEntity<>(u);
				clientService.registration(user);

				registrationForm.cleanForm();
				Notification.show(u.getFirstName() + " " + u.getLastName() + " ADDED")
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			} 
		});

		return formContainer;
	}

	public VerticalLayout settingsForm() {
		return null;
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

		Image avatar = new Image();
		avatar.setId("avatar");
		switch (CurrentUser.get().getProfile().getName()) {
		case User.ADMIN_PROFILE:
			avatar.setSrc("images/admin.png");
			break;
		case User.USER_PROFILE:
			avatar.setSrc("images/simpleUser.png");
			break;
		case User.VIEWER_PROFILE:
			avatar.setSrc("images/viewer.png");
			break;
		default:
			break;
		}
		return avatar;
	}

}