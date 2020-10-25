package com.demo.frontend.utils;

import org.vaadin.stefan.fullcalendar.Entry;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class OtherEntryDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

	
	public void set(Entry entry) {
		VerticalLayout container = new VerticalLayout();
		container.setSpacing(false);
		
		SpanDescription description = new SpanDescription();
		add(description.build("OTHER"));
		
		container.add(setText("Title", entry.getTitle()));
		
		if(entry.isRecurring())
			container.add(setText("Recurrig", "Yes"));
		
		container.add(setText("Start", entry.getStart().toLocalDate() + " | " + entry.getStart().toLocalTime()));
		container.add(setText("End", entry.getEnd().toLocalDate() + " | " + entry.getEnd().toLocalTime()));
	
		add(container);
	}
	
	
	public TextField setText(String label, String value) {
		TextField tf = new TextField();
		tf.setLabel(label);
		tf.setValue(value);
		tf.setReadOnly(true);
		return tf;
	}
}
