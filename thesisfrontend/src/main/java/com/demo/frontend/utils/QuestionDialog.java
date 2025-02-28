package com.demo.frontend.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/views/utils/questiondialog.css")
public class QuestionDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout container;
	private Span question;
	private HorizontalLayout buttonsContainer;
	private SpanDescription spanDescription;
	private Button confirmButton;
	private Button closeButton;
	private DatePicker dp;
	
	/* Question Dialog */
	public QuestionDialog(String quest, String type) {
		setId("dialog");
		
		setWidth("420px");
		
		question = new Span(quest);
		
		container = new VerticalLayout();

		spanDescription = new SpanDescription();
		spanDescription.build(type);
		add(spanDescription.build(type));

		AppButton appButton = new AppButton();
		Icon confirm = VaadinIcon.CHECK.create();
		Icon close = VaadinIcon.CLOSE.create();
		
		closeButton = appButton.set("Discard", close);
		confirmButton = appButton.set("Save", confirm);
		
		buttonsContainer = new HorizontalLayout();
		buttonsContainer.setId("buttCont");
		buttonsContainer.add(confirmButton, closeButton);	
		
		container.add(question);

		if(type.equals("DISABLE")) {
			dp = new DatePicker();
			dp.setLabel("Disable until?");
			container.setSpacing(false);
			container.add(dp);
		}
		
		add(container, buttonsContainer);
		
		closeButton.addClickListener(click -> close());
		
		open();
	}
	
	public DatePicker getDatePicker() {
		return dp;
	}
	
	public Button getConfirmButton() {
		return confirmButton;
	}
	
	public Button getCloseButton() {
		return closeButton;
	}
}
