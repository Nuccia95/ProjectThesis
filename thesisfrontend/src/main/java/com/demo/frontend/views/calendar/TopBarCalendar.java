package com.demo.frontend.views.calendar;

import java.time.LocalDate;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class TopBarCalendar {
	
	private DatePicker goToPicker;
	private AppButton appButton;
	private H3 title;
	
	public TopBarCalendar() {
		appButton = new AppButton(); 
		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		goToPicker.setVisible(false);
	}

	public HorizontalLayout buildTopBar() {
		HorizontalLayout topBarContainer = new HorizontalLayout();		
		topBarContainer.setSizeFull();
		
		HorizontalLayout buttonsContainer = new HorizontalLayout();
		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		goToPicker.setVisible(false);

		title = new H3(goToPicker.getValue().getMonth() + " " + goToPicker.getValue().getDayOfMonth() + ", " +
				goToPicker.getValue().getYear());
		title.getElement().getStyle().set("fontWeight", "bold");

		Button goToButton = appButton.set("Go to", VaadinIcon.CALENDAR_USER.create());
		goToButton.addClickListener(e -> {
			goToPicker.setVisible(true);
			goToPicker.open();
		});

		buttonsContainer.add(goToButton, goToPicker);
		buttonsContainer.getElement().getStyle().set("margin-left", "auto");
		topBarContainer.setAlignSelf(Alignment.END, buttonsContainer);
		topBarContainer.add(title, buttonsContainer);
		return topBarContainer;
	}
	
	public DatePicker getGoToPicker() {
		return goToPicker;
	}
	
	public H3 getTitle() {
		return title;
	}
	
	
}
