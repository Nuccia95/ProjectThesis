package com.demo.frontend.views.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.vaadin.gatanaso.MultiselectComboBox;
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
import com.vaadin.flow.component.icon.Icon;
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
	private H3 title;
	private H3 selectedDate;
	private HorizontalLayout titleContainer;
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
	private MultiselectComboBox<String> multiselectComboBox;
	private final String blueColor = "#3e77c1";

	public EntryForm(LocalDate ld2) {
		this.ld = ld2;
		createEntryForm();
	}

	public void createEntryForm() {
		setSpacing(false);
		setAlignItems(Alignment.AUTO);
		container = new HorizontalLayout();
		
		/* Form Title */
		titleContainer = new HorizontalLayout();
		title = new H3("EVENT:");
		title.getElement().getStyle().set("fontWeight", "bold");
		selectedDate = new H3(ld.toString());
		selectedDate.getElement().getStyle().set("color", blueColor);
		selectedDate.getElement().getStyle().set("fontWeight", "bold");
		titleContainer.add(title, selectedDate);
		add(titleContainer);

		/* Recurring event */
		checkBoxRecurring = new Checkbox();
		checkBoxRecurring.setLabel("Recurring event?");
		checkBoxRecurring.setValue(false);
		checkBoxRecurring.addValueChangeListener(e -> {
			if (e.getValue())
				addInfoRecurringEvent();
			else {
				l3.setVisible(false);
				endDatePicker.setVisible(false);
			}
		});
		add(checkBoxRecurring);

		/* Resources */
		l1 = new VerticalLayout();
		l1.setSpacing(false);
		comboBoxResources = new ComboBox<>();
		comboBoxResources.setItems("Server A", "Laboratorio 1", "Server B");
		comboBoxResources.setLabel("Resources");

		/* Event Colors */
		comboBoxColors = new ComboBox<>();
		comboBoxColors.setItems("dodgerblue", "gray", "orange", "tomato", "violet");
		comboBoxColors.setLabel("Event Color");
		l1.add(comboBoxResources, comboBoxColors);
		container.add(l1);

		/* Start time / End time */
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

		/* Friends box */
		multiselectComboBox = new MultiselectComboBox<String>();
		multiselectComboBox.setPlaceholder("@choose friends..");
		multiselectComboBox.setItems("Item 1", "Item 2", "Item 3", "Item 4");
		l5 = new HorizontalLayout();
		Icon i = new Icon(VaadinIcon.USERS);
		i.setColor(blueColor);
		inviteButton = new Button(i);
		Label friendsLabel = new Label("Do you want to invite some friend?");
		l5.setAlignItems(Alignment.BASELINE);
		l5.add(inviteButton, friendsLabel);
		add(l5);

		inviteButton.addClickListener(e -> {
			l5.add(multiselectComboBox);
		});

		l4 = new HorizontalLayout();
		Icon i2 = new Icon(VaadinIcon.CHECK);
		i2.setColor(blueColor);
		saveButton = new Button("Save", i2);
		saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		l4.add(saveButton);
		setAlignSelf(Alignment.END, l4);
		add(l4);
	}

	public void addInfoRecurringEvent() {
		l3 = new VerticalLayout();
		endDatePicker = new DatePicker();
		endDatePicker.setLabel("End Date Recurring Event");
		l3.add(endDatePicker);
		checkBoxDays = new CheckboxGroup<>();
		checkBoxDays.setLabel("Days");
		checkBoxDays.setSizeUndefined();
		checkBoxDays.setItems(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
				DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		checkBoxDays.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		l3.add(checkBoxDays);
		//l3.setSpacing(false);
		container.add(l3);
	}

	public Entry createCurrentEntry() {
		LocalDateTime lstart = LocalDateTime.of(ld, startTimePicker.getValue());
		LocalDateTime lend = LocalDateTime.of(ld, endTimePicker.getValue());

		newEntry = new Entry();
		newEntry.setColor(comboBoxColors.getValue());
		newEntry.setTitle(comboBoxResources.getValue());

		if (checkBoxRecurring.getValue()) {
			newEntry.setRecurring(true);
			newEntry.setRecurringStartDate(ld, Timezone.UTC);
			newEntry.setRecurringStartTime(startTimePicker.getValue());
			newEntry.setRecurringEndDate(endDatePicker.getValue(), Timezone.UTC);
			newEntry.setRecurringEndTime(endTimePicker.getValue());
			newEntry.setRecurringDaysOfWeeks(checkBoxDays.getValue());
		} else {
			newEntry.setStart(lstart);
			newEntry.setEnd(lend);
		}

		return newEntry;
	}

	public void fillExistingEntry(Entry entry) {
		comboBoxResources.setValue(entry.getTitle());
		comboBoxColors.setValue(entry.getColor());
		if (entry.isRecurring()) {
			addInfoRecurringEvent();
			endDatePicker.setValue(entry.getRecurringEndDate(Timezone.UTC));
			startTimePicker.setValue(entry.getRecurringStartTime());
			endTimePicker.setValue(entry.getRecurringEndTime());
			checkBoxDays.setValue(entry.getRecurringDaysOfWeeks());
			
		} else {
			startTimePicker.setValue(entry.getStart().toLocalTime());
			endTimePicker.setValue(entry.getEnd().toLocalTime());
		}
	}

	public Button getSaveButton() {
		return saveButton;
	}

}