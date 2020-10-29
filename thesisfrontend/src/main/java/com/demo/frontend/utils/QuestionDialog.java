package com.demo.frontend.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class QuestionDialog extends Dialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout container;
	private Span question;
	private HorizontalLayout buttonsContainer;
	private SpanDescription spanDescription;
	private Button confirmButton;
	private Button closeButton;
	
	/* Question Dialog */
	public QuestionDialog(String quest, String type) {

		setWidth("450px");
		setHeight("200px");
		container = new VerticalLayout();
		question = new Span(quest);

		spanDescription = new SpanDescription();
		spanDescription.build(type);
		add(spanDescription.build(type));

		AppButton appButton = new AppButton();
		Icon confirm = VaadinIcon.CHECK.create();
		Icon close = VaadinIcon.CLOSE.create();
		
		closeButton = appButton.set("", close);
		confirmButton = appButton.set("", confirm);
		
		buttonsContainer = new HorizontalLayout();
		buttonsContainer.add(confirmButton, closeButton);		
		
		container.setAlignSelf(Alignment.END, buttonsContainer);
		container.add(question, buttonsContainer);
	
		closeButton.addClickListener(click -> close());
		
		add(container);
		open();
	}
	
	public Button getConfirmButton() {
		return confirmButton;
	}
	
	public Button getCloseButton() {
		return closeButton;
	}
}
