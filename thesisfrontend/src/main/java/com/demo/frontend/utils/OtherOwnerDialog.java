package com.demo.frontend.utils;

import org.vaadin.stefan.fullcalendar.Entry;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class OtherOwnerDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

	public OtherOwnerDialog(Entry entry) {
		VerticalLayout container = new VerticalLayout();
		container.setSpacing(false);
		
		SpanDescription description = new SpanDescription();
		add(description.build("OTHER"));
		
		container.add(setText("Title", entry.getTitle()));
		
		if(entry.isRecurring())
			container.add(setText("Recurrig", "Yes"));
		
		container.add(setText("Start", entry.getStart().toLocalDate() + " | " + entry.getStart().toLocalTime()));
		container.add(setText("End", entry.getEnd().toLocalDate() + " | " + entry.getEnd().toLocalTime()));
	
		
		AppButton appButton = new AppButton();
		Button closeButton = appButton.set("", VaadinIcon.CLOSE.create());
		closeButton.addClickListener(click -> close());
		
		
		container.add(closeButton);
		container.setAlignSelf(Alignment.END, closeButton);
		add(container);
		open();
	}
	
	public TextField setText(String label, String value) {
		TextField tf = new TextField();
		tf.setLabel(label);
		tf.setValue(value);
		tf.setReadOnly(true);
		return tf;
	}
}
