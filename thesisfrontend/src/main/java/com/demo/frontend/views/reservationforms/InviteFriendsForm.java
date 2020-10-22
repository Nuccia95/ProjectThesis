package com.demo.frontend.views.reservationforms;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.gatanaso.MultiselectComboBox;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;

public class InviteFriendsForm extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	private MultiselectComboBox<String> boxFriends;
	
	private List<String> friends;
	private List<String> addedManually;
	private int cont;
	
	public InviteFriendsForm() {
		setWidth("500");
		setHeight("600");
		
		Label friendsLabel = new Label("Do you want to invite some friends?");
		add(friendsLabel);
		addedManually = new ArrayList<>();

		boxFriends = new MultiselectComboBox<>();
		boxFriends.setSizeFull();
		boxFriends.setCompactMode(true);
		boxFriends.setPlaceholder("@choose friends..");
		boxFriends.setWidth("100%");
		boxFriends.getElement().getStyle().set("font-size", "13px");
		add(boxFriends);
		
		Label addFriends = new Label("Your friends is not present?");
		add(addFriends);
		
		HorizontalLayout addFriendCont = new HorizontalLayout();
		
		EmailField newFriendField = new EmailField();
		newFriendField.setSizeFull();
		newFriendField.setLabel("@Email");
		
		AppButton appButton = new AppButton();
		Button addButton = appButton.set("", VaadinIcon.PLUS_CIRCLE.create());
		
		/* increment when add new friends */
		cont = 0;
		H4 number = new H4();
		
		addButton.addClickListener(ev -> {
			if(newFriendField.getValue() != null) {
				addedManually.add(newFriendField.getValue());
				cont++;
				number.setText(String.valueOf(cont));
			}
		});
			
		addFriendCont.setAlignItems(Alignment.BASELINE);
		addFriendCont.add(newFriendField, addButton, number);
		add(addFriendCont);
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
