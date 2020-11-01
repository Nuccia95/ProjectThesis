package com.demo.frontend.views.reservationforms;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

public class SingleEntryForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private TextField reservationTitle;
	private ComboBox<String> comboBoxResources;
	private ComboBox<String> comboBoxColors;
	private DatePicker startDatePicker;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	
	public SingleEntryForm(LocalDate date, LocalTime time) {
		
		HorizontalLayout container = new HorizontalLayout();
		container.setSpacing(false);
		setId("singleform");
		startDatePicker = new DatePicker();
		startDatePicker.setLabel("Start Date");
		startDatePicker.setReadOnly(true);
		startDatePicker.setValue(date);
		
		reservationTitle = new TextField();
		reservationTitle.setLabel("Title");
		reservationTitle.setPlaceholder("Reservation Title");
		reservationTitle.setMaxLength(20);

		VerticalLayout l1 = new VerticalLayout();
		l1.setSpacing(false);
		VerticalLayout l2 = new VerticalLayout();
		l2.setSpacing(false);
		
		/* Resources */
		comboBoxResources = new ComboBox<>();
		comboBoxResources.setRequired(true);
		comboBoxResources.setLabel("Resources");

		/* Event Colors */
		comboBoxColors = new ComboBox<>();
		comboBoxColors.setItems("Tomato", "DodgerBlue", "DarkCyan", "DeepPink", "DarkOrange");
		comboBoxColors.setLabel("Color");
		l1.add(reservationTitle, comboBoxResources, comboBoxColors);

		/* Start time / End time */
		l2.setAlignItems(Alignment.BASELINE);
		
		startTimePicker = new TimePicker();
		startTimePicker.setRequired(true);
		startTimePicker.setLabel("Start Time");
		startTimePicker.setMinTime(LocalTime.of(8, 0));
		startTimePicker.setMaxTime(LocalTime.of(19, 0));
		startTimePicker.setStep(Duration.ofMinutes(15));
		
		endTimePicker = new TimePicker();
		endTimePicker.setRequired(true);
		endTimePicker.setMinTime(LocalTime.of(8, 0));
		endTimePicker.setMaxTime(LocalTime.of(19, 0));
		endTimePicker.setLabel("End Time");
		endTimePicker.setStep(Duration.ofMinutes(15));
		endTimePicker.setEnabled(false);
		
		if(time != null) {
			startTimePicker.setValue(time);
			endTimePicker.setValue(startTimePicker.getValue().plusHours(1));
			endTimePicker.setEnabled(true);
		}
		
		startTimePicker.addValueChangeListener(event -> {
			endTimePicker.setValue(event.getValue().plusHours(1));
			endTimePicker.setEnabled(true);
		});
		
		l2.add(startDatePicker, startTimePicker, endTimePicker);
		
		container.add(l1, l2);
		
		add(container);
	}
	
	public TextField getReservationTitle() {
		return reservationTitle;
	}

	public void setResources(List<String> resources) {
		comboBoxResources.setItems(resources);
	}

	public ComboBox<String> getComboBoxResources() {
		return comboBoxResources;
	}

	public void setComboBoxResources(ComboBox<String> comboBoxResources) {
		this.comboBoxResources = comboBoxResources;
	}

	public ComboBox<String> getComboBoxColors() {
		return comboBoxColors;
	}

	public void setComboBoxColors(ComboBox<String> comboBoxColors) {
		this.comboBoxColors = comboBoxColors;
	}

	public TimePicker getStartTimePicker() {
		return startTimePicker;
	}

	public void setStartTimePicker(TimePicker startTimePicker) {
		this.startTimePicker = startTimePicker;
	}

	public TimePicker getEndTimePicker() {
		return endTimePicker;
	}

	public void setEndTimePicker(TimePicker endTimePicker) {
		this.endTimePicker = endTimePicker;
	}
}
