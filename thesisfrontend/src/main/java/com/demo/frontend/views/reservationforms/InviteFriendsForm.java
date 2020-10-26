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
		
		HorizontalLayout c1 = new HorizontalLayout();
		Icon users = VaadinIcon.ENVELOPES.create();
		users.setId("icon");
		c1.add(users, new Text("Do you want to invite some friends?"));
		c1.setSpacing(true);
		c1.setAlignItems(Alignment.BASELINE);
		
		boxFriends = new MultiselectComboBox<>();
		boxFriends.setSizeFull();
		boxFriends.setCompactMode(true);
		boxFriends.setSizeFull();
		boxFriends.setPlaceholder("@choose friends..");
		
		HorizontalLayout c2 = new HorizontalLayout();
		Icon pencil = VaadinIcon.PENCIL.create();
		pencil.setId("icon");
		c2.add(pencil, new Text("Your friend is not present?"));
		c2.setSpacing(true);
		c2.setAlignItems(Alignment.BASELINE);
		
		
		
		HorizontalLayout addFriendCont = new HorizontalLayout();
		addFriendCont.setSizeFull();
		
		EmailField newFriendField = new EmailField();
		newFriendField.setInvalid(true);
		newFriendField.setLabel("Email");
		newFriendField.setSizeFull();
		newFriendField.setPlaceholder("name@..");
		
		AppButton appButton = new AppButton();
		Button addButton = appButton.set("", VaadinIcon.PLUS_CIRCLE.create());
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
		
		addFriendCont.setAlignItems(Alignment.BASELINE);
		addFriendCont.add(newFriendField, addButton);
		add(c1, boxFriends, c2, addFriendCont);
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
