package com.demo.frontend.views.calendar;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.Reservation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.demo.frontend.clientservices.ClientService;
import com.demo.frontend.utils.QuestionDialog;
import com.demo.frontend.view.login.CurrentUser;
import com.demo.frontend.views.main.MainView;

@Route(value = "fullCalendarView", layout = MainView.class)
@CssImport("./styles/views/fullcalendar/fullcalendar-view.css")
@PageTitle("Calendar")
public class FullCalendarView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ClientService clientService;

	private FullCalendar calendar;
	private MapCalendarEvent mapCalEvent;

	private TopBarCalendar topBar;
	private EntryForm entryForm;
	private Dialog createEntryDialog;
	private QuestionDialog moveEntryDialog;
	private QuestionDialog removeEntryDialog;
	private Entry currentEntry;
	private LocalDate ld;

	public FullCalendarView() {
		setSpacing(false);
		setClassName("fullcalendar-view");
		topBar = new TopBarCalendar();
		add(topBar.buildTopBar());
		mapCalEvent = new MapCalendarEvent();
		createCalendar();
		updateDatePicker();
		setFlexGrow(1, calendar);
		manageCalendarEntries();
	}

	public void createCalendar() {
		calendar = FullCalendarBuilder.create().withAutoBrowserTimezone().withEntryLimit(3).build();
		calendar.setId("calendar");
		calendar.setNumberClickable(false);
		calendar.setFirstDay(DayOfWeek.MONDAY);
		calendar.setSizeFull();
	    add(calendar);
		topBar.getViewBox().addValueChangeListener(e -> {
	        CalendarView value = e.getValue();
	        calendar.changeView(value == null ? CalendarViewImpl.DAY_GRID_MONTH : value);
	    });
		calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);
	}

	public void manageCalendarEntries() {
		/* CREATE NEW RESERVATION */
		calendar.addTimeslotsSelectedListener((event) -> {
			ld = event.getStartDateTime().toLocalDate();
			setForm(ld);
			createEntryDialog.open();
			entryForm.getSaveButton().addClickListener(e -> {
				currentEntry = entryForm.createCurrentEntry();
				if (currentEntry.isRecurring())
					createRecurringReservations(currentEntry);
				else
					createSingleReservation(currentEntry);
				createEntryDialog.close();
			});
		});

		/* UPDATE EXISTING RESERVATION */
		calendar.addEntryClickedListener(e -> {
			/* delete recurring reservations */
			if (e.getEntry().isRecurring() && e.getEntry().getDescription().equals("0")) {

				removeEntryDialog = new QuestionDialog("Do you want to delete this reservation?",
						VaadinIcon.TRASH.create(), VaadinIcon.CLOSE.create(), "REMOVE");

				removeEntryDialog.getConfirmButton().addClickListener(ev -> {
					
					Reservation reservation = mapCalEvent.mapEntryToReservation(e.getEntry());
					reservation.setOwner(CurrentUser.get());
					HttpEntity<Reservation> res = new HttpEntity<>(reservation);
					
					clientService.deleteRecurringReservations(res);
					
					for (Entry recurrEntry : calendar.getEntries())
						if (recurrEntry.getDescription() != null)
							if (recurrEntry.getDescription().equals(reservation.getId().toString()))
								calendar.removeEntry(recurrEntry);
					calendar.removeEntry(e.getEntry());
					removeEntryDialog.close();
				});

				removeEntryDialog.getCancelButton().addClickListener(ev -> {
					removeEntryDialog.close();
				});
			}
			/* edit single reservation */
			if (e.getEntry().isEditable()) {
				String entryId = e.getEntry().getId();
				LocalDate ld1 = null;
				ld1 = e.getEntry().getStart().toLocalDate();
				setForm(ld1);
				entryForm.fillExistingEntry(e.getEntry());
				createEntryDialog.open();
				entryForm.getSaveButton().addClickListener(ev -> {
					currentEntry = entryForm.createCurrentEntry();
					Reservation reservation = mapCalEvent.mapEntryToReservation(currentEntry);
					reservation.setId(Long.parseLong(entryId));
					reservation.setOwner(CurrentUser.get());
					
					HttpEntity<Reservation> res = new HttpEntity<>(reservation);
					Reservation r = clientService.updateSingleReservation(res);
					
					Entry singleEntry = mapCalEvent.mapReservationToEntry(r);
					calendar.removeEntry(e.getEntry());
					calendar.addEntry(singleEntry);
					createEntryDialog.close();
				});
				entryForm.getDeleteEntryButton().addClickListener(ev -> {
					Reservation reservation = mapCalEvent.mapEntryToReservation(e.getEntry());
					reservation.setOwner(CurrentUser.get());
					
					HttpEntity<Reservation> res = new HttpEntity<>(reservation);
					clientService.deleteReservation(res);
					
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

				moveEntryDialog = new QuestionDialog("Update the event date to " + newDate + "?",
						VaadinIcon.CHECK.create(), VaadinIcon.CLOSE.create(), "MOVE");

				moveEntryDialog.getConfirmButton().addClickListener(evnt -> {
					Reservation reservation = mapCalEvent.mapEntryToReservation(e.getEntry());
					reservation.setId(Long.parseLong(e.getEntry().getId()));
					reservation.setOwner(CurrentUser.get());
					
					HttpEntity<Reservation> res = new HttpEntity<>(reservation);
					clientService.updateDate(res);
					
					moveEntryDialog.close();
				});
				moveEntryDialog.getCancelButton().addClickListener(evnt -> {
					calendar.removeEntry(e.getEntry());
					Entry oldEntry = e.getEntry();
					LocalDateTime ldtstart = LocalDateTime.of(oldDate, oldEntry.getStart().toLocalTime());
					LocalDateTime ldtend = LocalDateTime.of(oldDate, oldEntry.getEnd().toLocalTime());
					oldEntry.setStart(ldtstart);
					oldEntry.setEnd(ldtend);
					calendar.addEntry(oldEntry);
					moveEntryDialog.close();
				});
			}
		});

		/* Limited number of entries displayed */
		calendar.addLimitedEntriesClickedListener(event -> {
			Collection<Entry> entries = calendar.getEntries(event.getClickedDate());
			if (!entries.isEmpty()) {
				Dialog dialog = new Dialog();
				VerticalLayout dialogLayout = new VerticalLayout();
				dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
				for (Entry e : entries) {
					Button b = createClickableEntry(e);
					b.addClickListener(click -> {
						setForm(event.getClickedDate());
						entryForm.fillExistingEntry(e);
						createEntryDialog.open();
					});
					dialogLayout.add(b);
				}
				dialog.add(dialogLayout);
				dialog.open();
			}
		});
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent); 
		Set<Reservation> reservations = clientService.getByOwner(CurrentUser.get().getId());		
		if (reservations != null)
			for (Reservation reservation : reservations) {
				Entry e = mapCalEvent.mapReservationToEntry(reservation);
				calendar.addEntry(e);
			}
	}

	public void createSingleReservation(Entry newEntry) {
		Reservation reservation = mapCalEvent.mapEntryToReservation(newEntry);
		reservation.setOwner(CurrentUser.get());
		
		HttpEntity<Reservation> res = new HttpEntity<>(reservation);
		Reservation r = clientService.createReservation(res);
		
		Entry singleEntry = mapCalEvent.mapReservationToEntry(r);
		calendar.addEntry(singleEntry);
	}

	public void createRecurringReservations(Entry newEntry) {
		Reservation reservation = mapCalEvent.mapEntryToReservation(currentEntry);
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
				
				HttpEntity<Reservation> res = new HttpEntity<>(reservation);
				Reservation createdReservation = clientService.createReservation(res);
				
				if (groupId == null)
					groupId = createdReservation.getId();
				Entry e = mapCalEvent.mapReservationToEntry(createdReservation);
				calendar.addEntry(e);
			}
			start = start.plusDays(1);
		}
	}

	public Button createClickableEntry(Entry entry) {
		Button button = new Button(entry.getTitle());
		button.setId("clickable-entry");
		Style style = button.getStyle();
		style.set("background-color", Optional.ofNullable(entry.getColor()).orElse("rgb(58, 135, 173)"));
		style.set("color", "white");
		style.set("font-size", "12px");
		style.set("height", "20px");
		style.set("border", "0 none black");
		style.set("border-radius", "3px");
		style.set("text-align", "left");
		style.set("margin", "1px");
		return button;
	}

	public void setForm(LocalDate date) {
		createEntryDialog = new Dialog();
		entryForm = new EntryForm(date);
		entryForm.setFriends(clientService.getAllEmails());
		entryForm.setResources(clientService.getResourcesNames());
		createEntryDialog.add(entryForm);
		topBar.getGoToPicker().setValue(date);
	}

	public void updateDatePicker() {
		topBar.getGoToPicker().addValueChangeListener(e -> {
			calendar.gotoDate(topBar.getGoToPicker().getValue());
			/* update date */

			topBar.getTitle()
					.setText(topBar.getGoToPicker().getValue().getMonth() + " "
							+ topBar.getGoToPicker().getValue().getDayOfMonth() + ", "
							+ topBar.getGoToPicker().getValue().getYear());

			topBar.getGoToPicker().setVisible(false);
		});
	}

}
