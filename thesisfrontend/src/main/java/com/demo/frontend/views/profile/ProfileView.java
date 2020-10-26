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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.User;
import shared.thesiscommon.utils.PasswordEncoder;
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

	private PasswordField oldPassword;
	private PasswordField newPassword;
	private PasswordField newPassword2;
	private AppButton appButton;

	public ProfileView() {
		setSizeFull();
		setId("profile-view");
		setSpacing(true);
		setAlignItems(Alignment.CENTER);
		setPasswordFields();
		appButton = new AppButton();

		tabsToPages = new HashMap<>();

		tab1 = new Tab("Profile Information");
		tab1.setId("tab");
		page1 = buildInfo();

		tab2 = new Tab("Add New User");
		tab2.setId("tab");
		page2 = buildRegistrationForm();
		page2.setVisible(false);

		tab3 = new Tab("Settings");
		tab3.setId("tab");
		page3 = buildUpdatePasswordForm();
		page3.setVisible(false);

		Tabs tabs = new Tabs();
		tabsToPages.put(tab1, page1);
		tabsToPages.put(tab2, page2);
		tabsToPages.put(tab3, page3);

		if (CurrentUser.isAdmin())
			tabs.add(tab1, tab2, tab3);
		else if (CurrentUser.isSimpleUser())
			tabs.add(tab1, tab3);
		else if (CurrentUser.isViewer())
			tabs.add(tab1);

		tabs.setFlexGrowForEnclosedTabs(1);

		Div pages = new Div(page1, page2, page3);

		tabs.addSelectedChangeListener(event -> {
			tabsToPages.values().forEach(page -> page.setVisible(false));
			Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);
		});

		add(tabs, pages);
	}

	public VerticalLayout buildInfo() {

		VerticalLayout infoContainer = new VerticalLayout();
		infoContainer.setId("profile");
		infoContainer.setSpacing(false);
		infoContainer.setWidth("440px");
		infoContainer.setHeight("400px");

		H4 title = new H4("Profile");
		HorizontalLayout titleContainer = new HorizontalLayout();
	
		titleContainer.add(title, getAvatar());
		infoContainer.add(titleContainer, setTextField("First Name", CurrentUser.get().getFirstName()),
				setTextField("Last Name", CurrentUser.get().getLastName()), setTextField("Email", CurrentUser.get().getEmail()),
				setTextField("Role", CurrentUser.get().getProfile().getName()));

		
		return infoContainer;
	}

	public VerticalLayout buildRegistrationForm() {

		VerticalLayout formContainer = new VerticalLayout();
		formContainer.setId("newUserForm");
		formContainer.setSpacing(false);

		H4 title = new H4("Add New User");
		HorizontalLayout titleContainer = new HorizontalLayout();
		titleContainer.getElement().getStyle().set("margin-left", "10px");
		Icon users = VaadinIcon.USERS.create();
		users.setId("users");
		titleContainer.add(title, users);
		titleContainer.setAlignItems(Alignment.BASELINE);

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.buildRegistrationForm();

		Button submitButton = appButton.set("Add", VaadinIcon.PLUS_CIRCLE.create());

		formContainer.add(titleContainer, registrationForm, submitButton);
		formContainer.setAlignSelf(Alignment.END, submitButton);

		submitButton.addClickListener(ev -> {
			User u = registrationForm.getUserForm();
			if (u != null) {
				HttpEntity<User> user = new HttpEntity<>(u);
				clientService.registration(user);

				registrationForm.cleanForm();
				Notification.show(u.getFirstName() + " " + u.getLastName() + " added")
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			}
		});

		return formContainer;
	}

	public VerticalLayout buildUpdatePasswordForm() {

		VerticalLayout container = new VerticalLayout();
		container.setSpacing(false);
		container.setId("passwordForm");
		container.setWidth("440px");
		container.setHeight("400px");
		H4 title = new H4("Settings");
		HorizontalLayout titleContainer = new HorizontalLayout();
		Icon settings = VaadinIcon.AUTOMATION.create();
		settings.setId("settings");
		titleContainer.add(title, settings);
		titleContainer.setAlignItems(Alignment.BASELINE);

		container.add(titleContainer, new Span("Do you want to update your password?"));

		Button updateButton = appButton.set("Update", VaadinIcon.CHECK.create());

		updateButton.addClickListener(click -> {

			if (!passwordsMatch()) {
				Notification.show("New passwords don't match", 2000, Position.BOTTOM_START)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
				cleanPasswordFields();
			} else {

				/* Check old password */
				User u = CurrentUser.get();
				u.setPassword(PasswordEncoder.encode(oldPassword.getValue()));
				HttpEntity<User> userToSend = new HttpEntity<>(u);

				if (clientService.checkOldPassword(userToSend)) {

					/* Update new password */
					User u2 = CurrentUser.get();
					u2.setPassword(PasswordEncoder.encode(newPassword.getValue()));
					HttpEntity<User> userToSend2 = new HttpEntity<>(u2);

					if (clientService.updatePassword(userToSend2)) {
						Notification.show("Password Updated", 2000, Position.BOTTOM_START)
								.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
						cleanPasswordFields();
					} else {
						Notification.show("Error during update", 2000, Position.BOTTOM_START)
								.addThemeVariants(NotificationVariant.LUMO_ERROR);
						cleanPasswordFields();
					}
				} else {
					Notification.show("Old password doesn't match", 2000, Position.BOTTOM_START)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
					cleanPasswordFields();
				}
			}
		});

		container.add(oldPassword, newPassword, newPassword2, updateButton);
		container.setAlignSelf(Alignment.END, updateButton);
		return container;
	}

	public boolean passwordsMatch() {
		return newPassword.getValue().equals(newPassword2.getValue());
	}

	public void cleanPasswordFields() {
		oldPassword.clear();
		newPassword.clear();
		newPassword2.clear();
	}

	public void setPasswordFields() {
		oldPassword = new PasswordField();
		oldPassword.setRequiredIndicatorVisible(true);
		oldPassword.setLabel("Old Password");
		oldPassword.setSizeFull();

		newPassword = new PasswordField();
		newPassword.setRequiredIndicatorVisible(true);
		newPassword.setLabel("New Password");
		newPassword.setSizeFull();

		newPassword2 = new PasswordField();
		newPassword2.setLabel("New Password");
		newPassword2.setRequiredIndicatorVisible(true);
		newPassword2.setSizeFull();
	}

	public TextField setTextField(String label, String value) {
		TextField tf = new TextField();
		
		if(label.equals("Role"))
			tf.getElement().getStyle().set("text-color", "#1f3d7a");
		
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