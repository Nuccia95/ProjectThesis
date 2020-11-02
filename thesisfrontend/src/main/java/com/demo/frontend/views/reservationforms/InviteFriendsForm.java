package com.demo.frontend.views.reservationforms;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.gatanaso.MultiselectComboBox;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;

@CssImport("./styles/views/forms/forms.css")
public class InviteFriendsForm extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	
	private MultiselectComboBox<String> boxFriends;
	private List<String> friends;
	private List<String> addedManually;
	private int cont;
	
	public InviteFriendsForm() {
		
		setId("inviteform");
		HorizontalLayout c1 = new HorizontalLayout();
		Icon users = VaadinIcon.ENVELOPES.create();
		users.setId("icon");
		c1.add(users, new Text("Do you want to invite some friends to this event?"));
		c1.setId("cont");
		c1.setSpacing(true);
		c1.setAlignItems(Alignment.BASELINE);
		
		boxFriends = new MultiselectComboBox<>();
		boxFriends.setCompactMode(true);
		boxFriends.setPlaceholder("@choose friends..");
		
		AppButton appButton = new AppButton();
		Button plus = appButton.set("New friend?", VaadinIcon.PLUS_CIRCLE_O.create());
		
		add(c1, boxFriends, plus);
		
		EmailField newFriendField = new EmailField();
		newFriendField.setInvalid(true);
		newFriendField.setPlaceholder("email");
		
		Button addButton = appButton.set("", VaadinIcon.PLUS.create());
		addButton.setId("add");
		addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		addedManually = new ArrayList<>();
		
		addButton.addClickListener(ev -> {
			if(newFriendField.getValue() != null) {
				if(newFriendField.isInvalid()) {
					Notification.show("Invalid email", 2000, Position.TOP_START).addThemeVariants(NotificationVariant.LUMO_ERROR);
					newFriendField.clear();
				}
				else{
					addedManually.add(newFriendField.getValue());
					Notification.show(newFriendField.getValue() + " Added", 2000, Position.BOTTOM_START).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					newFriendField.clear();
				}
			}
		});
		
		HorizontalLayout addFriendCont = new HorizontalLayout();
		addFriendCont.add(newFriendField, addButton);
		addFriendCont.setSpacing(false);
		addFriendCont.setAlignItems(Alignment.BASELINE);
		addFriendCont.setVisible(false);
		add(addFriendCont);

		plus.addClickListener(ev -> {
			if(addFriendCont.isVisible())
				addFriendCont.setVisible(false);
			else
				addFriendCont.setVisible(true);
		});
		
		
	}

	public List<String> selectedFriends(){
		List<String> selected = new ArrayList<>();
		for (String email : boxFriends.getValue())
			selected.add(email);
		
		for (String email : addedManually)
			selected.add(email);
		
		return selected;
	}
	
	public int getCont() {
		return cont;
	}

	public void setCont(int cont) {
		this.cont = cont;
	}

	public MultiselectComboBox<String> getBoxFriends(){
		return boxFriends;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public void setBoxFriends(MultiselectComboBox<String> boxFriends) {
		this.boxFriends = boxFriends;
	}

	public List<String> getAddedFriends() {
		return addedManually;
	}

	public void setAddedFriends(List<String> addedFriends) {
		this.addedManually = addedFriends;
	}

}
