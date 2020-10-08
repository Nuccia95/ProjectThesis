package com.demo.frontend.views.calendar;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import shared.thesiscommon.Reservation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.Timezone;
import com.demo.frontend.clientservices.ReservationHandler;
import com.demo.frontend.views.main.MainView;

@Route(value = "fullCalendarView", layout = MainView.class)
@PageTitle("Calendar")
public class FullCalendarView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FullCalendar calendar;
	private HorizontalLayout topBar;
	private DatePicker goToPicker;
	private Dialog createEntryDialog;
	private Dialog changeEntryDateDialog;
	private Dialog deleteEntryDialog;
	private Button confirmUpdateDateButton;
	private Button cancelUpdateDateButton;
	private Button deleteEntryButton;
	private EntryForm entryForm;
	private Entry currentEntry;
	private LocalDate ld;
	private final String blueColor = "#3e77c1";
	@Autowired
	private ReservationHandler reservationHandler;

	public FullCalendarView() {
		setId("calendar-view");
		calendar = FullCalendarBuilder.create().build();
		createTopBar();
		add(calendar);
		setFlexGrow(1, calendar);
		manageCalendarEntries();
	}

	public void manageCalendarEntries() {

		/* CREATE NEW ENTRY on day clicked */
		calendar.addTimeslotsSelectedListener((event) -> {
			ld = event.getStartDateTime().toLocalDate();
			setForm(ld);
			createEntryDialog.open();
			entryForm.getSaveButton().addClickListener(e -> {
				currentEntry = entryForm.createCurrentEntry();
				Reservation reservation = reservationHandler.fromEntryToReservation(currentEntry);
				if (currentEntry.isRecurring())
					createRecurringReservations(currentEntry, reservation);
				else {
					Reservation r = reservationHandler.createReservation(reservation);
					Entry singleEntry = reservationHandler.fromReservationToEntry(r);
					calendar.addEntry(singleEntry);
				}
				createEntryDialog.close();
			});
		});

		/* EXISTING ENTRY clicked DA FARE */
		calendar.addEntryClickedListener(e -> {
			if(e.getEntry().isRecurring()) {
				builDeleteEntryDialog();
			}
			if(e.getEntry().isEditable()) {
				LocalDate ld1 = null;
				if (e.getEntry().isRecurring())
					ld1 = e.getEntry().getRecurringStartDate(Timezone.UTC);
				else
					ld1 = e.getEntry().getStart().toLocalDate();
				setForm(ld1);
				entryForm.fillExistingEntry(e.getEntry());
				createEntryDialog.open();
			}
		});

		/* DRAG & DROP, change the entry date */
		calendar.addEntryDroppedListener(e -> {	
			LocalDate oldDate = e.getEntry().getStart().toLocalDate();
			System.out.println(e.getEntry().isRecurring());
			if (e.getEntry().isEditable()) {
				e.applyChangesOnEntry();
				LocalDate newDate = e.getEntry().getStart().toLocalDate();
				buildUpdateDateDialog(newDate);				
				confirmUpdateDateButton.addClickListener(evnt -> {
					Reservation reservation = reservationHandler.fromEntryToReservation(e.getEntry());
					reservationHandler.updateReservationDate(reservation);
					changeEntryDateDialog.close();
				});
				cancelUpdateDateButton.addClickListener(evnt -> {
					calendar.removeEntry(e.getEntry());
					Entry oldEntry = e.getEntry();
					LocalDateTime ldtstart = LocalDateTime.of(oldDate, oldEntry.getStart().toLocalTime());
					LocalDateTime ldtend = LocalDateTime.of(oldDate, oldEntry.getEnd().toLocalTime());
					oldEntry.setStart(ldtstart);
					oldEntry.setEnd(ldtend);
					calendar.addEntry(oldEntry);
					changeEntryDateDialog.close();
				});
			}
		});
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		// The page is loaded in this moment
		// calendar.removeAllEntries();
		// setCurrentUserReservations();
	}

	/*
	 * public void setCurrentUserReservations() { Reservation[] reservations =
	 * reservationHandler.getReservationByOwner(); if (reservations != null) for
	 * (Reservation reservation : reservations) { Entry e =
	 * reservationHandler.fromReservationToEntry(reservation);
	 * System.out.println("Entry ID: " + e.getDescription()); } }
	 */

	public void createRecurringReservations(Entry newEntry, Reservation reservation) {
		LocalDate start = newEntry.getRecurringStartDate(Timezone.UTC);
		LocalDate end = newEntry.getRecurringEndDate(Timezone.UTC);
		Long groupId = null;

		while (start.isBefore(end)) {
			DayOfWeek day = start.getDayOfWeek();
			if (newEntry.getRecurringDaysOfWeeks().contains(day)) {
				reservation.setDayOfWeek(day.toString());
				reservation.setStartDate(start);
				reservation.setEndDate(newEntry.getRecurringEndDate(Timezone.UTC));
				reservation.setRecurring(true);
				reservation.setEditable(false);
				if (groupId != null)
					reservation.setGroupId(groupId);
				Reservation r = reservationHandler.createReservation(reservation);
				if (r.isEditable())
					groupId = r.getId();

				Entry e = reservationHandler.fromReservationToEntry(r);
				calendar.addEntry(e);
			} // end if
			start = start.plusDays(1);
		}
	}

	/* UTILS */
	public void createTopBar() {
		topBar = new HorizontalLayout();
		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		H3 title = new H3(goToPicker.getValue().getMonth().toString());
		title.getElement().getStyle().set("fontWeight", "bold");
		title.getElement().getStyle().set("color", blueColor);

		goToPicker.addValueChangeListener(e -> {
			calendar.gotoDate(goToPicker.getValue());
			title.setText(goToPicker.getValue().getMonth().toString());
		});

		topBar.setAlignItems(Alignment.BASELINE);
		topBar.add(title, goToPicker);
		add(topBar);

		// ArrayList<CalendarView> calendarViews = new
		// ArrayList<>(Arrays.asList(CalendarViewImpl.values()));

		/*
		 * ComboBox<CalendarView> comboBoxView = new ComboBox<>("", calendarViews);
		 * comboBoxView.setValue(CalendarViewImpl.DAY_GRID_MONTH);
		 * comboBoxView.setWidth("300px"); comboBoxView.addValueChangeListener(e -> {
		 * CalendarView value = e.getValue(); calendar.changeView(value == null ?
		 * CalendarViewImpl.DAY_GRID_MONTH : value); }); //topBar.add(comboBoxView);
		 */
	}	
	
	public void setForm(LocalDate ld) {
		createEntryDialog = setNewDialog();
		entryForm = new EntryForm(ld);
		createEntryDialog.add(entryForm);
		goToPicker.setValue(ld);
	}

	public Dialog setNewDialog() {
		Dialog d = new Dialog();
		d.setDraggable(true);
		d.setCloseOnEsc(true);
		d.setCloseOnOutsideClick(true);
		return d;
	}

	public void buildUpdateDateDialog(LocalDate newDate) {
		VerticalLayout container = new VerticalLayout();
		container.setFlexGrow(1, container);
		changeEntryDateDialog = setNewDialog();

		H4 description = new H4("Update the event date to " + newDate.toString() + " ?");
		container.add(description);

		HorizontalLayout buttonsContainer = new HorizontalLayout();
		Icon confirmIcon = new Icon(VaadinIcon.CHECK);
		confirmIcon.setColor(blueColor);
		confirmUpdateDateButton = new Button(confirmIcon);

		Icon closeIcon = new Icon(VaadinIcon.CLOSE);
		closeIcon.setColor(blueColor);
		cancelUpdateDateButton = new Button(closeIcon);

		buttonsContainer.add(confirmUpdateDateButton, cancelUpdateDateButton);
		container.add(buttonsContainer);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		changeEntryDateDialog.add(container);
		changeEntryDateDialog.open();
	}

	public void builDeleteEntryDialog() {
		VerticalLayout container = new VerticalLayout();
		container.setFlexGrow(1, container);
		deleteEntryDialog = setNewDialog();
		
		H4 description = new H4("Do you want to delete this reservation?");
		container.add(description);
		
		HorizontalLayout buttonsContainer = new HorizontalLayout();
		Icon confirmIcon = new Icon(VaadinIcon.TRASH);
		confirmIcon.setColor(blueColor);
		deleteEntryButton = new Button(confirmIcon);

		Icon closeIcon = new Icon(VaadinIcon.CLOSE);
		closeIcon.setColor(blueColor);
		Button closeButton = new Button(closeIcon);
		closeButton.addClickListener(e -> deleteEntryDialog.close());
		
		buttonsContainer.add(deleteEntryButton, closeButton);
		container.add(buttonsContainer);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		
		
		deleteEntryDialog.add(container);
		deleteEntryDialog.open();
	}
}
