package com.demo.frontend.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RecurringEventDialog extends Dialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout container;
	private DatePicker endDatePicker;
	private CheckboxGroup<DayOfWeek> checkBoxDays;
	private Button confirmRecurring;
	private DatePicker start;
	
	public RecurringEventDialog(LocalDate startDate) {
		container = new VerticalLayout();
		checkBoxDays = new CheckboxGroup<>();
		checkBoxDays.getElement().getStyle().set("font-size", "14px");
		endDatePicker = new DatePicker();
		start = new DatePicker();
		start.setLabel("Start date");
		start.setValue(startDate);
		start.setReadOnly(true);
		set();
	}
	
	public void set() {
		
		SpanDescription span = new SpanDescription();
		container.add(span.build("RECURRING"));
		
		Span description = new Span("Create a new recurring event, selecting the end and some days");
		description.getElement().getStyle().set("font-size", "14px");
		description.getElement().getStyle().set("color", "grey");
		container.add(description);
		
		HorizontalLayout containerDates = new HorizontalLayout();
		
		endDatePicker.setRequired(true);
		endDatePicker.setLabel("End Date");

		containerDates.setAlignItems(Alignment.BASELINE);
		containerDates.add(start, endDatePicker);
		
		checkBoxDays.setRequired(true);
		checkBoxDays.setLabel("Days");
		checkBoxDays.setItems(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, 
				DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		
		AppButton appButt = new AppButton();
		confirmRecurring = appButt.set("", VaadinIcon.CHECK.create());
		confirmRecurring.addClickListener(ev -> {
			close();
		});
		
		container.setAlignSelf(Alignment.END, confirmRecurring);
		container.add(containerDates, checkBoxDays, confirmRecurring);
		add(container);
	}

	public boolean hasData() {
		return endDatePicker.getValue()!= null && checkBoxDays.getValue()!=null;
	}
	
	public DatePicker getEndDatePicker() {
		return endDatePicker;
	}

	public CheckboxGroup<DayOfWeek> getCheckBoxDays() {
		return checkBoxDays;
	}

	public Button getConfirmRecurring() {
		return confirmRecurring;
	}

}
