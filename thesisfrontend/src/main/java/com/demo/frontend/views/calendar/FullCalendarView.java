package com.demo.frontend.views.calendar;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.Reservation;

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
@CssImport("./styles/views/fullcalendar/fullcalendar-view.css")
@PageTitle("Calendar")
public class FullCalendarView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FullCalendar calendar;
	private HorizontalLayout titleContainer;
	private DatePicker goToPicker;
	private Dialog createEntryDialog;
	private Dialog changeEntryDateDialog;
	private Dialog deleteEntryDialog;
	private Button confirmUpdateDateButton;
	private Button cancelUpdateDateButton;
	private Button deleteRecurringEntryButton;
	private EntryForm entryForm;
	private Entry currentEntry;
	private LocalDate ld;
	private final String blueColor = "#3e77c1";
	@Autowired
	private ReservationHandler reservationHandler;

	public FullCalendarView() {
		setSpacing(false);
		setId("calendar-view");
		calendar = FullCalendarBuilder.create().build();
		calendar.setNumberClickable(false);
		calendar.setFirstDay(DayOfWeek.MONDAY);
		createTopBar();
		add(calendar);
		setFlexGrow(1, titleContainer, calendar);
		manageCalendarEntries();
	}

	public void manageCalendarEntries() {
		Entry entry = new Entry();
		entry.setTitle("Some event");
		entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0), calendar.getTimezone());
		entry.setEnd(entry.getStart().plusHours(2), calendar.getTimezone());
		entry.setColor("grey");
		entry.setEditable(true);
		calendar.addEntry(entry);

		/* CREATE NEW RESERVATION */
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

		/* UPDATE EXISTING RESERVATION */
		calendar.addEntryClickedListener(e -> {
			/* delete recurring reservations */
			if (e.getEntry().isRecurring() && e.getEntry().getDescription().equals("0")) {
				buildDeleteEntryDialog();
				deleteRecurringEntryButton.addClickListener(ev -> {
					Reservation reservation = reservationHandler.fromEntryToReservation(e.getEntry());
					reservation.setId(Long.parseLong(e.getEntry().getId()));
					reservationHandler.deleteRecurringReservations(reservation);
					for (Entry recurrEntry : calendar.getEntries())
						// da fare: se l id dell entry è uguale a quello dell user;
						if (recurrEntry.getDescription() != null)
							if (recurrEntry.getDescription().equals(reservation.getId().toString()))
								calendar.removeEntry(recurrEntry);
					calendar.removeEntry(e.getEntry());
					deleteEntryDialog.close();
				});
			}
			/* edit single reservation */
			if (e.getEntry().isEditable()) {
				LocalDate ld1 = null;
				if (e.getEntry().isRecurring())
					ld1 = e.getEntry().getRecurringStartDate(Timezone.UTC);
				else
					ld1 = e.getEntry().getStart().toLocalDate();
				setForm(ld1);
				entryForm.fillExistingEntry(e.getEntry());
				createEntryDialog.open();
				entryForm.getSaveButton().addClickListener(ev -> {
					currentEntry = entryForm.createCurrentEntry();
					Reservation reservation = reservationHandler.fromEntryToReservation(currentEntry);
					reservation.setId(Long.parseLong(e.getEntry().getId()));
					Reservation r = reservationHandler.updateSingleReservation(reservation);
					Entry singleEntry = reservationHandler.fromReservationToEntry(r);
					calendar.removeEntry(e.getEntry());
					calendar.addEntry(singleEntry);
					createEntryDialog.close();
				});
				entryForm.getDeleteEntryButton().addClickListener(ev -> {
					Reservation reservation = reservationHandler.fromEntryToReservation(e.getEntry());
					reservation.setId(Long.parseLong(e.getEntry().getId()));
					reservationHandler.deleteReservation(reservation);
					calendar.removeEntry(e.getEntry());
					createEntryDialog.close();
				});
			}
		});

		/* DRAG & DROP : CHANGE RESERVATION DATE */
		calendar.addEntryDroppedListener(e -> {
			LocalDate oldDate = e.getEntry().getStart().toLocalDate();
			if (e.getEntry().isEditable()) {
				e.applyChangesOnEntry();
				LocalDate newDate = e.getEntry().getStart().toLocalDate();
				buildUpdateDateDialog(newDate);
				confirmUpdateDateButton.addClickListener(evnt -> {
					Reservation reservation = reservationHandler.fromEntryToReservation(e.getEntry());
					reservation.setId(Long.parseLong(e.getEntry().getId()));
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
		setCurrentUserReservations();
	}

	public void setCurrentUserReservations() {
		Reservation[] reservations = reservationHandler.getReservationByOwner();
		if (reservations != null)
			for (Reservation reservation : reservations) {
				Entry e = reservationHandler.fromReservationToEntry(reservation);
				calendar.addEntry(e);
			}
	}

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
				Reservation createdReservation = reservationHandler.createReservation(reservation);
				if (groupId == null)
					groupId = createdReservation.getId();
				Entry e = reservationHandler.fromReservationToEntry(createdReservation);
				calendar.addEntry(e);
			}
			start = start.plusDays(1);
		}
	}

	/* COMPONENTS */
	public void createTopBar() {
		titleContainer = new HorizontalLayout();
		HorizontalLayout buttonsContainer = new HorizontalLayout();
		HorizontalLayout topBarContainer = new HorizontalLayout();
		topBarContainer.setSizeFull();
		buttonsContainer.getElement().getStyle().set("margin-left", "auto");
		titleContainer.getElement().getStyle().set("margin-right", "auto");
		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		goToPicker.setVisible(false);

		H3 title = new H3(goToPicker.getValue().getMonth().toString());
		H3 date = new H3(goToPicker.getValue().toString());
		title.getElement().getStyle().set("fontWeight", "bold");
		title.getElement().getStyle().set("color", "black");
		date.getElement().getStyle().set("fontWeight", "bold");
		date.getElement().getStyle().set("color", blueColor);

		goToPicker.addValueChangeListener(e -> {
			calendar.gotoDate(goToPicker.getValue());
			title.setText(goToPicker.getValue().getMonth().toString());
			date.setText(goToPicker.getValue().toString());
			goToPicker.setVisible(false);
		});

		Icon i = new Icon(VaadinIcon.CALENDAR_USER);
		i.setColor(blueColor);
		Button goToButton = new Button("Go To");
		goToButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		goToButton.setIcon(i);
		titleContainer.add(goToButton);
		goToButton.addClickListener(e -> {
			goToPicker.setVisible(true);
			goToPicker.open();
		});
		buttonsContainer.setAlignItems(Alignment.BASELINE);
		buttonsContainer.add(goToButton, goToPicker);
		titleContainer.add(title, date);
		titleContainer.setAlignItems(Alignment.BASELINE);
		topBarContainer.add(titleContainer, buttonsContainer);
		topBarContainer.setAlignSelf(Alignment.END, buttonsContainer);
		add(topBarContainer);
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
		container.setSpacing(false);
		changeEntryDateDialog = setNewDialog();

		Span span = new Span("Move to");
		span.getElement().getStyle().set("padding", "2px 10px");
		span.getElement().getStyle().set("font-size", "14px");
		span.getElement().getStyle().set("color", "#ff751a");
		span.getElement().getStyle().set("background", "#ffe6cc");
		changeEntryDateDialog.add(span);

		Span description = new Span("Update the event date to " + newDate.toString() + "?");
		container.add(description);

		HorizontalLayout buttonsContainer = new HorizontalLayout();
		buttonsContainer.setSpacing(false);
		Icon confirmIcon = new Icon(VaadinIcon.CHECK);
		confirmIcon.setColor(blueColor);
		confirmUpdateDateButton = new Button(confirmIcon);
		confirmUpdateDateButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		Icon closeIcon = new Icon(VaadinIcon.CLOSE);
		closeIcon.setColor(blueColor);
		cancelUpdateDateButton = new Button(closeIcon);
		cancelUpdateDateButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		buttonsContainer.add(confirmUpdateDateButton, cancelUpdateDateButton);
		container.add(buttonsContainer);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		changeEntryDateDialog.add(container);
		changeEntryDateDialog.open();
	}

	public void buildDeleteEntryDialog() {
		VerticalLayout container = new VerticalLayout();
		container.setFlexGrow(1, container);
		deleteEntryDialog = setNewDialog();

		H4 description = new H4("Do you want to delete this reservation?");
		container.add(description);

		HorizontalLayout buttonsContainer = new HorizontalLayout();
		Icon confirmIcon = new Icon(VaadinIcon.TRASH);
		confirmIcon.setColor(blueColor);
		deleteRecurringEntryButton = new Button(confirmIcon);

		Icon closeIcon = new Icon(VaadinIcon.CLOSE);
		closeIcon.setColor(blueColor);
		Button closeButton = new Button(closeIcon);
		closeButton.addClickListener(e -> deleteEntryDialog.close());

		buttonsContainer.add(deleteRecurringEntryButton, closeButton);
		container.add(buttonsContainer);
		container.setAlignSelf(Alignment.END, buttonsContainer);

		deleteEntryDialog.add(container);
		deleteEntryDialog.open();
	}
}
