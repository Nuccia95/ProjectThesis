package com.demo.frontend.utils;

import java.util.List;
import org.vaadin.gatanaso.MultiselectComboBox;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;


public class FriendsDialog extends Dialog{
	
	private static final long serialVersionUID = 1L;

	private MultiselectComboBox<String> boxFriends;
	private List<String> friends;
	
	public FriendsDialog() {
		set();
	}

	public void set() {
		
		VerticalLayout container = new VerticalLayout();
		SpanDescription span = new SpanDescription();
		container.add(span.build("FRIENDS"));
		
		Label friendsLabel = new Label("Do you want to invite some friends? ");
		friendsLabel.getElement().getStyle().set("font-size", "14px");
		container.add(friendsLabel);

		boxFriends = new MultiselectComboBox<>();
		boxFriends.setPlaceholder("@choose friends..");
		boxFriends.setWidth("100%");
		boxFriends.getElement().getStyle().set("font-size", "13px");
		container.add(boxFriends);
		
		Label addFriends = new Label("Your friends is not present? Add ");
		addFriends.getElement().getStyle().set("font-size", "14px");
		container.add(addFriends);
		
		HorizontalLayout addFriendCont = new HorizontalLayout();
		TextField newFriend = new TextField();
		newFriend.setLabel("Email");
		newFriend.setPlaceholder("email@..");
		AppButton appButton = new AppButton();
		Button add = appButton.set("", VaadinIcon.PLUS_CIRCLE.create());
		addFriendCont.setSpacing(false);
		addFriendCont.add(newFriend, add);
		addFriendCont.setAlignItems(Alignment.BASELINE);
		
		add.addClickListener(ev -> {
			//String newEmail = newFriend.getValue();
		});
		
		container.add(addFriendCont);
		
		add(container);
		
	}
	
	public MultiselectComboBox<String> getBoxFriends(){
		return boxFriends;
	}
	
	
}
