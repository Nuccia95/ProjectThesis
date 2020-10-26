package com.demo.frontend.views.reservationforms;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.utils.SpanDescription;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;

public class SingleEntryForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private ComboBox<String> comboBoxResources;
	private ComboBox<String> comboBoxColors;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	private SpanDescription spanDescription;
	private AppButton appButton;
	private Button saveEntryButton;
	private HorizontalLayout buttContainer;
	
	public SingleEntryForm(LocalDate date, LocalTime time) {
		setSizeFull();
		setSpacing(false);
	
		H3 title = new H3("Start date: " + date);
		
		comboBoxResources = new ComboBox<>();
		comboBoxColors = new ComboBox<>();
		
		HorizontalLayout container = new HorizontalLayout();
		container.setSpacing(false);
		
		VerticalLayout l1 = new VerticalLayout();
		l1.setSpacing(false);
		VerticalLayout l2 = new VerticalLayout();
		l2.setSpacing(false);
		
		/* Resources */
		comboBoxResources.setRequired(true);
		comboBoxResources.setLabel("Resources");

		/* Event Colors */
		comboBoxColors.setItems("dodgerblue", "green", "orange", "red", "violet");
		comboBoxColors.setLabel("Color");
		l1.add(comboBoxResources, comboBoxColors);
		container.add(l1);

		/* Start time / End time */
		l2.setAlignItems(Alignment.BASELINE);
		
		startTimePicker = new TimePicker();
		startTimePicker.setRequired(true);
		startTimePicker.setLabel("Start Time");
		startTimePicker.setMinTime(LocalTime.of(8, 0));
		startTimePicker.setMaxTime(LocalTime.of(19, 0));
		startTimePicker.setStep(Duration.ofMinutes(15));
		if(time != null)
			startTimePicker.setValue(time);
		
		endTimePicker = new TimePicker();
		endTimePicker.setRequired(true);
		endTimePicker.setMinTime(LocalTime.of(8, 0));
		endTimePicker.setMaxTime(LocalTime.of(19, 0));
		endTimePicker.setLabel("End Time");
		endTimePicker.setStep(Duration.ofMinutes(15));


		l2.add(startTimePicker, endTimePicker);
		container.add(l2);
		setSizeFull();
		
		buttContainer = new HorizontalLayout();
		appButton = new AppButton();
		saveEntryButton = appButton.set("Save", VaadinIcon.CHECK.create());
		
		buttContainer.add(saveEntryButton);
		
		add(title, container, buttContainer);
		setAlignSelf(Alignment.END, buttContainer);		
	}
	
	public SpanDescription getSpanDescription() {
		return spanDescription;
	}

	public void setSpanDescription(SpanDescription spanDescription) {
		this.spanDescription = spanDescription;
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

	public Button getSaveEntryButton() {
		return saveEntryButton;
	}
	
	public HorizontalLayout getButtContainer() {
		return buttContainer;
	}
}
