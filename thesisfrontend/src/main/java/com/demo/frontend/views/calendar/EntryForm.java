package com.demo.frontend.views.calendar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.vaadin.gatanaso.MultiselectComboBox;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.utils.FriendsDialog;
import com.demo.frontend.utils.RecurringEventDialog;
import com.demo.frontend.utils.SpanDescription;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

public class EntryForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private SpanDescription spanDescription;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	private Button saveButton;
	private ComboBox<String> comboBoxColors;
	private ComboBox<String> comboBoxResources;
	private Checkbox checkBoxRecurring;
	private HorizontalLayout container;
	private VerticalLayout l1;
	private VerticalLayout l2;
	private HorizontalLayout l4;
	private HorizontalLayout l5;
	private LocalDate ld;
	private LocalTime lt;
	private Button deleteEntryButton;
	private MultiselectComboBox<String> multiselectComboBoxFriends;
	private AppButton appButton;
	private RecurringEventDialog dialogRecurring;
	private FriendsDialog friendsDialog;

	public EntryForm(LocalDateTime date) {
		ld = date.toLocalDate();
		
		LocalTime lt0 = LocalTime.parse("00:00");
		if(date.toLocalTime().equals(lt0))
			lt = null;
		else
			lt = date.toLocalTime();
		initComponents();

		dialogRecurring = new RecurringEventDialog(ld);
		friendsDialog = new FriendsDialog();
		
		buildForm();
	}

	public EntryForm(LocalDate date) {
		setSizeFull();
		ld = date;
		initComponents();
		buildForm();
	}
	
	public void initComponents() {
		setSpacing(false);
		appButton = new AppButton();
		spanDescription = new SpanDescription();
		container = new HorizontalLayout();
		container.setSpacing(false);
		container.setAlignItems(Alignment.START);
		checkBoxRecurring = new Checkbox();
		checkBoxRecurring.getElement().getStyle().set("font-size", "14px");
		l1 = new VerticalLayout();
		l2 = new VerticalLayout();
		l4 = new HorizontalLayout();
		l5 = new HorizontalLayout();
		l5.setSpacing(false);
		comboBoxResources = new ComboBox<>();
		comboBoxColors = new ComboBox<>();
		endTimePicker = new TimePicker();
		multiselectComboBoxFriends = new MultiselectComboBox<>();
		multiselectComboBoxFriends.getElement().getStyle().set("padding", "5px");
	}

	public void buildForm() {

		add(spanDescription.build("CREATE"));
		
		/* Form Title */
		H3 title = new H3("Start date: " + ld);
		title.getElement().getStyle().set("fontWeight", "bold");
		add(title);
		
		/* Recurring event */
		checkBoxRecurring.setLabel("Recurring event?");
		checkBoxRecurring.setValue(false);
		checkBoxRecurring.addValueChangeListener(e -> {
			if (Boolean.TRUE.equals(e.getValue()))
				dialogRecurring.open();
		});

		add(checkBoxRecurring);
		add(container);

		/* Resources */
		l1.setSpacing(false);
		comboBoxResources.setRequired(true);
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
		startTimePicker.setMinTime(LocalTime.of(8, 0));
		startTimePicker.setMaxTime(LocalTime.of(19, 0));
		startTimePicker.setStep(Duration.ofMinutes(15));
		if(lt != null)
			startTimePicker.setValue(lt);
		
		endTimePicker.setRequired(true);
		endTimePicker.setMinTime(LocalTime.of(8, 0));
		endTimePicker.setMaxTime(LocalTime.of(19, 0));
		endTimePicker.setLabel("End Time");
		endTimePicker.setStep(Duration.ofMinutes(15));


		l2.add(startTimePicker, endTimePicker);
		container.add(l2);

		/* Friends box */	
		multiselectComboBoxFriends.setPlaceholder("@choose friends..");
		
		Button inviteButton = appButton.set("", VaadinIcon.USERS.create());		
		Label friendsLabel = new Label("Do you want to invite some friends? ");
		friendsLabel.getElement().getStyle().set("font-size", "14px");
		l5.setAlignItems(Alignment.BASELINE);
		l5.add(inviteButton, friendsLabel);
		add(l5);
		inviteButton.addClickListener(e -> friendsDialog.open() );
		
		saveButton = appButton.set("Save", VaadinIcon.CHECK.create());
		l4.add(saveButton);
		setAlignSelf(Alignment.END, l4);
		add(l4);
	}

	public Entry createCurrentEntry() {
		
		LocalDateTime lstart = LocalDateTime.of(ld, startTimePicker.getValue());
		LocalDateTime lend = LocalDateTime.of(ld, endTimePicker.getValue());
		
		Entry newEntry = new Entry();
		newEntry.setColor(comboBoxColors.getValue());
		newEntry.setTitle(comboBoxResources.getValue());
		newEntry.setEditable(true);

		if (Boolean.TRUE.equals(checkBoxRecurring.getValue())
				&& Boolean.TRUE.equals(dialogRecurring.hasData())) {
			newEntry.setRecurring(true);
			newEntry.setRecurringStartDate(ld, Timezone.UTC);
			newEntry.setRecurringStartTime(startTimePicker.getValue());
			newEntry.setRecurringEndDate(dialogRecurring.getEndDatePicker().getValue(), Timezone.UTC);
			newEntry.setRecurringEndTime(endTimePicker.getValue());
			newEntry.setRecurringDaysOfWeeks(dialogRecurring.getCheckBoxDays().getValue());
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
		
		deleteEntryButton = appButton.set("Delete", VaadinIcon.TRASH.create());
		l4.add(deleteEntryButton);
	}
	
	public void setResources(List<String> resources) {
		comboBoxResources.setItems(resources);
	}
	
	public void setFriends(List<String> emails) {
		friendsDialog.getBoxFriends().setItems(emails);
	}
	
	public Button getSaveButton() {
		return saveButton;
	}
	
	public Button getDeleteEntryButton() {
		return deleteEntryButton;
	}

}