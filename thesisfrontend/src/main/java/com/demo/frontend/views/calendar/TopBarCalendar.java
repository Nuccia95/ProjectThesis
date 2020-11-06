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

public class TopBarCalendar {

	private DatePicker goToPicker;
	private AppButton appButton;
	private H3 title;
	private ComboBox<CalendarView> viewBox;
	private Button previousButton;
	private Button nextButton;
	private Button todayButton;

	public TopBarCalendar() {
		appButton = new AppButton();
		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		viewBox = new ComboBox<>();
	}

	public HorizontalLayout buildTopBar() {
		HorizontalLayout topBarContainer = new HorizontalLayout();
		topBarContainer.setSizeFull();

		title = new H3(goToPicker.getValue().getMonth() + " " + goToPicker.getValue().getDayOfMonth() + ", "
				+ goToPicker.getValue().getYear());
		title.getElement().getStyle().set("fontWeight", "bold");

		/* calendar views */
		setViewBox();
		Button viewsButton = appButton.set("Views", VaadinIcon.GRID_BIG.create());
		viewsButton.getElement().appendChild(viewBox.getElement());
		viewsButton.addClickListener(ev -> viewBox.setOpened(true));

		/* go to date */
		setDatePicker();
		Button goToButton = appButton.set("Go to", VaadinIcon.CALENDAR_USER.create());
		goToButton.getElement().appendChild(goToPicker.getElement());
		goToButton.addClickListener(e -> goToPicker.open());

		/* previous / next month */
		previousButton = appButton.set("Prev", VaadinIcon.ANGLE_LEFT.create());
		nextButton = appButton.set("Next", VaadinIcon.ANGLE_RIGHT.create());
		
		/* today */
		todayButton = appButton.set("Today", VaadinIcon.CALENDAR.create());
		

		HorizontalLayout buttonsContainer = new HorizontalLayout();
		buttonsContainer.setId("buttons-container-calendar");
		buttonsContainer.add(viewsButton, viewBox, goToPicker, goToButton, todayButton, previousButton, nextButton);

		topBarContainer.add(title, buttonsContainer);
		return topBarContainer;
	}

	public void setDatePicker() {
		goToPicker.getElement().getStyle().set("visibility", "hidden");
		goToPicker.getElement().getStyle().set("position", "fixed");
		goToPicker.setWidth("0px");
		goToPicker.setHeight("0px");
	}

	public void setViewBox() {
		viewBox.getElement().getStyle().set("visibility", "hidden");
		viewBox.getElement().getStyle().set("position", "fixed");
		viewBox.setItems(CalendarViewImpl.TIME_GRID_WEEK, CalendarViewImpl.TIME_GRID_DAY,
				CalendarViewImpl.DAY_GRID_MONTH, CalendarViewImpl.LIST_MONTH);
		viewBox.setValue(CalendarViewImpl.DAY_GRID_MONTH);
	}

	public void previousDate(String type) {

		LocalDate currentDate;
		currentDate = goToPicker.getValue();

		switch (type) {
		case "MONTH":
			currentDate = currentDate.minusMonths(1);
			break;
		case "WEEK":
			currentDate = currentDate.minusWeeks(1);
			break;
		case "DAY":
			currentDate = currentDate.minusDays(1);
			break;
		default:
			break;
		}

		goToPicker.setValue(currentDate);
	}

	public void nextDate(String type) {

		LocalDate currentDate;
		currentDate = goToPicker.getValue();

		switch (type) {
		case "MONTH":
			currentDate = currentDate.plusMonths(1);
			break;
		case "WEEK":
			currentDate = currentDate.plusWeeks(1);
			break;
		case "DAY":
			currentDate = currentDate.plusDays(1);
			break;
		default:
			break;
		}
		
		goToPicker.setValue(currentDate);
	}

	public Button getTodayButton() {
		return todayButton;
	}
	
	public DatePicker getGoToPicker() {
		return goToPicker;
	}

	public Button getPreviousButton() {
		return previousButton;
	}

	public Button getNextButton() {
		return nextButton;
	}

	public H3 getTitle() {
		return title;
	}

	public ComboBox<CalendarView> getViewBox() {
		return viewBox;
	}
}