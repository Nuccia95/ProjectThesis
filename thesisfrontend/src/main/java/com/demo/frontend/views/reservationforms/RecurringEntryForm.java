package com.demo.frontend.views.reservationforms;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.vaadin.gatanaso.MultiselectComboBox;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/views/forms/forms.css")
public class RecurringEntryForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private DatePicker endDatePicker;
	private MultiselectComboBox<DayOfWeek> days;
	private Button confirmRecurring;
	private DatePicker start;
	
	public RecurringEntryForm(LocalDate startDate) {
		setHeight("290px");
		days = new MultiselectComboBox<>();
		endDatePicker = new DatePicker();
		start = new DatePicker();
		start.setLabel("Start date");
		start.setValue(startDate);
		start.setReadOnly(true);
		set();
	}
	
	public void set() {
		
		HorizontalLayout c1 = new HorizontalLayout();
		Icon recurring = VaadinIcon.RECYCLE.create();
		recurring.setId("icon");
		c1.add(recurring, new Text("Create new recurring event, set required fields"));
		c1.setSpacing(true);
		c1.setAlignItems(Alignment.BASELINE);
		
		HorizontalLayout containerDates = new HorizontalLayout();
		endDatePicker.setRequired(true);
		endDatePicker.setLabel("End Date");
		containerDates.add(start, endDatePicker);
		containerDates.setAlignItems(Alignment.BASELINE);
		
		days.setLabel("Days");
		days.setRequired(true);
		days.setSizeFull();
		days.setItems(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, 
				DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		days.setCompactMode(true);
		
		add(c1, containerDates, days);
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
