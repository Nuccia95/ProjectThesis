package com.demo.frontend.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class QuestionDialog extends Dialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout container;
	private H4 question;
	private HorizontalLayout buttonsContainer;
	private SpanDescription spanDescription;

	private AppButton appButton;
	private Button confirmButton;
	private Button cancelButton;
	
	/* Question Dialog */
	public QuestionDialog(String quest, Icon confirm,  Icon cancel, String type) {

		container = new VerticalLayout();
		appButton = new AppButton();
		buttonsContainer = new HorizontalLayout();
		question = new H4(quest);

		spanDescription = new SpanDescription();
		spanDescription.build(type);
		
		confirmButton = appButton.set("", confirm);
		cancelButton = appButton.set("", cancel);
		buttonsContainer.add(confirmButton, cancelButton);
		
		container.setAlignSelf(Alignment.END, buttonsContainer);
		container.add(question, buttonsContainer);
		
		add(spanDescription.build(type), container);
		open();
	}
	
	public Button getConfirmButton() {
		return confirmButton;
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}
	
}
