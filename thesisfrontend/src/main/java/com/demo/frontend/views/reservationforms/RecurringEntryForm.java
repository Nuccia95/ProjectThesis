package com.demo.frontend.views.reservationforms;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.vaadin.gatanaso.MultiselectComboBox;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RecurringEntryForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private DatePicker endDatePicker;
	private MultiselectComboBox<DayOfWeek> days;
	private Button confirmRecurring;
	private DatePicker start;
	
	public RecurringEntryForm(LocalDate startDate) {
		
		setSizeFull();
		days = new MultiselectComboBox<>();
		endDatePicker = new DatePicker();
		start = new DatePicker();
		start.setLabel("Start date");
		start.setValue(startDate);
		start.setReadOnly(true);
		setSizeFull();
		set();
	}
	
	public void set() {
		
		/*SpanDescription span = new SpanDescription();
		add(span.build("RECURRING"));*/
		
		Span description = new Span("Create a new recurring event, selecting the end and some days");
		add(description);
		
		HorizontalLayout containerDates = new HorizontalLayout();
		
		endDatePicker.setRequired(true);
		endDatePicker.setLabel("End Date");

		containerDates.setAlignItems(Alignment.BASELINE);
		containerDates.add(start, endDatePicker);
		
		days.setRequired(true);
		days.setLabel("Days");
		days.setItems(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, 
				DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		days.setCompactMode(true);
		add(containerDates, days);
	}
	
	public boolean hasData() {
		return endDatePicker.getValue()!= null && days.getValue()!=null;
	}

	public DatePicker getEndDatePicker() {
		return endDatePicker;
	}

	public void setEndDatePicker(DatePicker endDatePicker) {
		this.endDatePicker = endDatePicker;
	}

	public MultiselectComboBox<DayOfWeek> getDays() {
		return days;
	}

	public void setDays(MultiselectComboBox<DayOfWeek> days) {
		this.days = days;
	}

	public Button getConfirmRecurring() {
		return confirmRecurring;
	}

	public void setConfirmRecurring(Button confirmRecurring) {
		this.confirmRecurring = confirmRecurring;
	}

	public DatePicker getStart() {
		return start;
	}

	public void setStart(DatePicker start) {
		this.start = start;
	}
	
	
}
