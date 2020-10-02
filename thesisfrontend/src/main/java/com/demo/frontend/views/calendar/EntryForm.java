package com.demo.frontend.views.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

public class EntryForm extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Entry newEntry;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	private Button saveButton;
	private ComboBox<String> comboBoxColors;
	private ComboBox<String> comboBoxResources;
	private Checkbox checkBoxRecurring;
	private CheckboxGroup<DayOfWeek> checkBoxDays;
	private DatePicker endDatePicker;
	private VerticalLayout l1;
	private VerticalLayout l2;
	private VerticalLayout l3;
	private HorizontalLayout container;
	private HorizontalLayout l4;
	private HorizontalLayout l5;
	private LocalDate ld;
	private Button inviteButton;
	
	public EntryForm(LocalDate ld2) {
		this.ld = ld2;
		createEntryForm();
	}

	public void createEntryForm() {	
		container = new HorizontalLayout();
		container.setSpacing(false);
		container.setAlignItems(Alignment.STRETCH);
		H3 title = new H3("EVENT " + ld.toString());
		title.getElement().getStyle().set("fontWeight","bold");
		add(title);

		/* recurring event */
		checkBoxRecurring = new Checkbox();
		checkBoxRecurring.setLabel("Yes, recurring event");
		checkBoxRecurring.setValue(false);
		checkBoxRecurring.addValueChangeListener(e -> {
			if(e.getValue()) {
				addInfoRecurringEvent();			
			}else {
				l3.setVisible(false);
				endDatePicker.setVisible(false);
			}
		});
		add(checkBoxRecurring);
		
		/* resources */
		l1 = new VerticalLayout();
		l1.setSpacing(false);
		comboBoxResources = new ComboBox<>();
		comboBoxResources.setItems("Server A", "Laboratorio 1", "Server B");
		comboBoxResources.setLabel("Resources");
		
		/* colors */
		comboBoxColors = new ComboBox<>();
		comboBoxColors.setItems("dodgerblue", "gray", "orange", "tomato", "violet");
		comboBoxColors.setLabel("Event Color");
		l1.add(comboBoxResources, comboBoxColors);
		container.add(l1);		
		
		/* start time / end time */
		l2 = new VerticalLayout();
		l2.setSpacing(false);
		l2.setAlignItems(Alignment.BASELINE);
		startTimePicker = new TimePicker();
		startTimePicker.setLabel("Start Time");
		startTimePicker.setMinTime(LocalTime.of(7, 0));
		startTimePicker.setMaxTime(LocalTime.of(23, 0));	
		
		endTimePicker = new TimePicker();
		endTimePicker.setLabel("End Time");
		endTimePicker.setMinTime(LocalTime.of(7, 0));
		endTimePicker.setMaxTime(LocalTime.of(23, 0));

		l2.add(startTimePicker, endTimePicker);
		container.add(l2);
		add(container);

		/* buttons */
		l5 = new HorizontalLayout();
		inviteButton = new Button(VaadinIcon.USERS.create());
		Label friendsLabel = new Label("Do you want to invite some friend?");
		l5.setAlignItems(Alignment.BASELINE);
		l5.add(inviteButton, friendsLabel);
		add(l5);
		
		l4 = new HorizontalLayout();		
		saveButton = new Button("Save", VaadinIcon.CHECK.create());
		saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		l4.add(saveButton);
		add(l4);		
	}
	
	public void addInfoRecurringEvent() {
		l3 = new VerticalLayout();
		endDatePicker = new DatePicker();
		endDatePicker.setLabel("End Date Recurring Event");
		l3.add(endDatePicker);
		checkBoxDays = new CheckboxGroup<>();
		checkBoxDays.setLabel("Days");
		checkBoxDays.setItems(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		checkBoxDays.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		l3.add(checkBoxDays);
		l3.setSpacing(false);
		container.add(l3);
	}
	
	public Entry createCurrentEntry() {
		LocalDateTime lstart = LocalDateTime.of(ld, startTimePicker.getValue());
		LocalDateTime lend = LocalDateTime.of(ld, endTimePicker.getValue());
		
		newEntry = new Entry();
		newEntry.setColor(comboBoxColors.getValue());
		newEntry.setTitle(comboBoxResources.getValue());
		
		if(checkBoxRecurring.getValue()){
			newEntry.setRecurring(true);
			newEntry.setRecurringStartDate(ld, Timezone.UTC);
			newEntry.setRecurringStartTime(startTimePicker.getValue());
			newEntry.setRecurringEndDate(endDatePicker.getValue(), Timezone.UTC);
			newEntry.setRecurringEndTime(endTimePicker.getValue());
			newEntry.setRecurringDaysOfWeeks(checkBoxDays.getValue());
		}else {
			newEntry.setStart(lstart);
			newEntry.setEnd(lend);
		}
		
		return newEntry;
	}
	
	public Button getSaveButton() {
		return saveButton;
	}

}
