package com.demo.frontend.views.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.vaadin.gatanaso.MultiselectComboBox;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.demo.frontend.utils.SpanDescription;
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
	private SpanDescription spanDescription;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	private Button saveButton;
	private ComboBox<String> comboBoxColors;
	private ComboBox<String> comboBoxResources;
	private Checkbox checkBoxRecurring;
	private CheckboxGroup<DayOfWeek> checkBoxDays;
	private DatePicker endDatePicker;
	private HorizontalLayout container;
	private VerticalLayout l1;
	private VerticalLayout l2;
	private VerticalLayout l3;
	private HorizontalLayout l4;
	private HorizontalLayout l5;
	private LocalDate ld;
	private Button inviteButton;
	private Button deleteEntryButton;
	private MultiselectComboBox<String> multiselectComboBoxFriends;
	private final String blueColor = "#3e77c1";
	private Icon iconFriends;
	private Icon iconCheck;

	public EntryForm(LocalDate date) {
		this.ld = date;
		initComponents();
		createEntryForm();
	}
	
	public void initComponents() {
		setSpacing(false);
		spanDescription = new SpanDescription();
		container = new HorizontalLayout();
		container.setSpacing(false);
		container.setAlignItems(Alignment.START);
		container.setMaxWidth("900px");
		container.setMaxHeight("710px");
		checkBoxRecurring = new Checkbox();
		checkBoxRecurring.getElement().getStyle().set("font-size", "14px");
		l1 = new VerticalLayout();
		l2 = new VerticalLayout();
		l3 = new VerticalLayout();
		l4 = new HorizontalLayout();
		l5 = new HorizontalLayout();
		l5.setSpacing(false);
		comboBoxResources = new ComboBox<>();
		comboBoxColors = new ComboBox<>();
		endTimePicker = new TimePicker();
		multiselectComboBoxFriends = new MultiselectComboBox<String>();
		multiselectComboBoxFriends.getElement().getStyle().set("padding", "5px");
		endDatePicker = new DatePicker();
		checkBoxDays = new CheckboxGroup<>();
		checkBoxDays.getElement().getStyle().set("font-size", "14px");
		iconFriends = new Icon(VaadinIcon.USERS);
		iconFriends.setColor(blueColor);
		iconCheck = new Icon(VaadinIcon.CHECK);
		iconCheck.setColor(blueColor);
	}

	public void createEntryForm() {

		add(spanDescription.build("CREATE"));
		
		/* Form Title */
		title = new H3("Start date: " + ld);
		title.getElement().getStyle().set("fontWeight", "bold");
		add(title);
		
		/* Recurring event */
		checkBoxRecurring.setLabel("Recurring event?");
		checkBoxRecurring.setValue(false);
		checkBoxRecurring.addValueChangeListener(e -> {
			if (e.getValue())
				addFieldRecurringEvent();
			else {
				l3.setVisible(false);
				endDatePicker.setVisible(false);
			}
		});
		add(checkBoxRecurring);
		add(container);

		/* Resources */
		l1.setSpacing(false);
		comboBoxResources.setRequired(true);
		comboBoxResources.setItems("Server A", "Laboratorio 1", "Server B");
		comboBoxResources.setLabel("Resources");

		/* Event Colors */
		comboBoxColors.setItems("dodgerblue", "green", "orange", "red", "violet");
		comboBoxColors.setLabel("Color");
		l1.add(comboBoxResources, comboBoxColors);
		container.add(l1);

		/* Start time / End time */
		l2.setSpacing(false);
		l2.setAlignItems(Alignment.BASELINE);
		startTimePicker = new TimePicker();
		startTimePicker.setRequired(true);
		startTimePicker.setLabel("Start Time");
		startTimePicker.setMinTime(LocalTime.of(7, 0));
		startTimePicker.setMaxTime(LocalTime.of(23, 0));
		endTimePicker.setRequired(true);
		endTimePicker.setLabel("End Time");
		endTimePicker.setMinTime(LocalTime.of(7, 0));
		endTimePicker.setMaxTime(LocalTime.of(23, 0));
		l2.add(startTimePicker, endTimePicker);
		container.add(l2);

		/* Friends box */	
		multiselectComboBoxFriends.setPlaceholder("@choose friends..");
		multiselectComboBoxFriends.setItems("Item 1", "Item 2", "Item 3", "Item 4");
		inviteButton = new Button(iconFriends);
		inviteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		Label friendsLabel = new Label("Do you want to invite some friends? ");
		friendsLabel.getElement().getStyle().set("font-size", "14px");
		l5.setAlignItems(Alignment.BASELINE);
		l5.add(inviteButton, friendsLabel);
		add(l5);
		inviteButton.addClickListener(e -> {
			l5.add(multiselectComboBoxFriends);
		});
		
		saveButton = new Button("Save", iconCheck);
		saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		l4.add(saveButton);
		setAlignSelf(Alignment.END, l4);
		add(l4);
	}

	public void addFieldRecurringEvent() {
		l3.setVisible(true);
		endDatePicker.setVisible(true);
		endDatePicker.setRequired(true);
		checkBoxDays.setRequired(true);
		endDatePicker.setLabel("End Date");
		/* Days */
		checkBoxDays.setLabel("Days");
		checkBoxDays.setItems(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, 
				DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
		checkBoxDays.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
		l3.add(endDatePicker, checkBoxDays);
		container.add(l3);
	}

	public Entry createCurrentEntry() {
		LocalDateTime lstart = LocalDateTime.of(ld, startTimePicker.getValue());
		LocalDateTime lend = LocalDateTime.of(ld, endTimePicker.getValue());

		newEntry = new Entry();
		newEntry.setColor(comboBoxColors.getValue());
		newEntry.setTitle(comboBoxResources.getValue());
		newEntry.setEditable(true);

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

	/* Fill form with value of existing entry */
	public void fillExistingEntry(Entry entry) {
		
		spanDescription.setSpanEdit();
		
		checkBoxRecurring.setVisible(false);
		comboBoxResources.setValue(entry.getTitle());
		comboBoxColors.setValue(entry.getColor());
		startTimePicker.setValue(entry.getStart().toLocalTime());
		endTimePicker.setValue(entry.getEnd().toLocalTime());
		Icon i2 = new Icon(VaadinIcon.TRASH);
		deleteEntryButton = new Button("Delete", i2);
		deleteEntryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		l4.add(deleteEntryButton);
	}
	

	public Button getSaveButton() {
		return saveButton;
	}
	
	public Button getDeleteEntryButton() {
		return deleteEntryButton;
	}

}