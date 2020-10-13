package com.demo.frontend.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class QuestionDialog {
	
	private Dialog dialog;
	private VerticalLayout container;
	private H4 question;
	private HorizontalLayout buttonsContainer;
	private Icon confirmIcon;
	private Icon cancelIcon;
	private SpanDescription spanDescription;
	private Button confirmButton;
	private Button cancelButton;
	private final String blueColor = "#3e77c1";
	
	public QuestionDialog(String q, VaadinIcon confirm, VaadinIcon cancel, String type) {
		dialog = new Dialog();
		container = new VerticalLayout();
		buttonsContainer = new HorizontalLayout();
		question = new H4(q);

		confirmIcon = new Icon(confirm);
		confirmIcon.setColor(blueColor);
		cancelIcon = new Icon(cancel);
		cancelIcon.setColor("");
		spanDescription = new SpanDescription();
		spanDescription.build(type);
		
		confirmButton = new Button(confirmIcon);
		cancelButton = new Button(cancelIcon);
		buttonsContainer.add(confirmButton, cancelButton);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		
		container.add(question, buttonsContainer);
		
		dialog.add(spanDescription.build(type), container);
		dialog.open();
	}
	
	public void close() {
		dialog.close();
	}
	
	public Button getConfirmButton() {
		return confirmButton;
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}
	
}
