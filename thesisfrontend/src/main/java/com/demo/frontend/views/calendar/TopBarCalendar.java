package com.demo.frontend.views.calendar;

import java.time.LocalDate;

import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class TopBarCalendar {
	
	private HorizontalLayout topBarContainer;
	private DatePicker goToPicker;
	private AppButton appButton;
	private H3 title;
	private ComboBox<CalendarView> viewBox;
	
	public TopBarCalendar() {
		appButton = new AppButton(); 
		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		goToPicker.setVisible(false);
		viewBox = new ComboBox<>();
	}

	public HorizontalLayout buildTopBar() {
		topBarContainer = new HorizontalLayout();		
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
		
	    viewBox.setItems(CalendarViewImpl.DAY_GRID_MONTH, CalendarViewImpl.DAY_GRID_WEEK, CalendarViewImpl.LIST_MONTH, CalendarViewImpl.TIME_GRID_WEEK);
	    viewBox.setValue(CalendarViewImpl.DAY_GRID_MONTH);
	    
	    topBarContainer.setAlignItems(Alignment.BASELINE);
		topBarContainer.add(title, viewBox, buttonsContainer);
		topBarContainer.setAlignSelf(Alignment.END, buttonsContainer);
		return topBarContainer;
	}
	
	public DatePicker getGoToPicker() {
		return goToPicker;
	}
	
	public H3 getTitle() {
		return title;
	}
	
	public ComboBox<CalendarView> getViewBox() {
		return viewBox;
	}
}
