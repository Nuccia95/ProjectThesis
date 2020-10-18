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
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

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

import com.demo.frontend.utils.QuestionDialog;
import com.demo.frontend.view.login.CurrentUser;
import com.demo.frontend.views.main.MainView;

@Route(value = "fullCalendarView", layout = MainView.class)
@CssImport("./styles/views/fullcalendar/fullcalendar-view.css")
@PageTitle("Calendar")
public class FullCalendarView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	private WebServicesInterface clientService;

	private FullCalendar calendar;
	private MapCalendarEvent mapCalEvent;
	private TopBarCalendar topBar;
	private EntryForm entryForm;

	private Dialog createEntryDialog;
	private QuestionDialog moveEntryDialog;
	private Entry currentEntry;

	public FullCalendarView() {
		setSpacing(false);
		setClassName("fullcalendar-view");

		topBar = new TopBarCalendar();
		add(topBar.buildTopBar());

		mapCalEvent = new MapCalendarEvent();

		createCalendar();

		if (Boolean.FALSE.equals(CurrentUser.isViewer()))
			manageReservations();

		updateDatePicker();
	}

	public void createCalendar() {
		calendar = FullCalendarBuilder.create().withAutoBrowserTimezone().withEntryLimit(3).build();
		calendar.setId("calendar");
		calendar.setNumberClickable(false);
		calendar.setFirstDay(DayOfWeek.MONDAY);
		calendar.setSizeFull();

		add(calendar);
		setFlexGrow(1, calendar);

		topBar.getViewBox().addValueChangeListener(e -> {
			CalendarView value = e.getValue();
			calendar.changeView(value == null ? CalendarViewImpl.DAY_GRID_MONTH : value);
		});
		calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);
	}

	public void manageReservations() {
		createReservation();
		updateExistingReservation();
		changeReservationDate();
		showMoreReservation();
	}

	public void createReservation() {
		calendar.addTimeslotsSelectedListener( event -> {
			LocalDate ld = event.getStartDateTime().toLocalDate();
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
	}

	public void updateExistingReservation() {
		calendar.addEntryClickedListener(e -> {
			Entry entry = e.getEntry();
			
			if (entry.isEditable() && entry.isRecurring() && entry.getDescription().equals("0"))
				deleteRecurringEntry(entry);
			
			if (entry.isEditable())
				editSingleReservation(entry);
		});
	}

	public void deleteRecurringEntry(Entry e) {
		QuestionDialog removeEntryDialog = new QuestionDialog("Do you want to delete this reservation?", VaadinIcon.TRASH.create(),
				VaadinIcon.CLOSE.create(), "REMOVE");

		removeEntryDialog.getConfirmButton().addClickListener(ev -> {

			Reservation reservation = mapCalEvent.mapEntryToReservation(e);
			reservation.setOwner(CurrentUser.get());
			HttpEntity<Reservation> res = new HttpEntity<>(reservation);
			clientService.deleteRecurringReservations(res);
			for (Entry recurrEntry : calendar.getEntries()) {
				if (recurrEntry.getDescription() != null &&
					recurrEntry.getDescription().equals(reservation.getId().toString()))
						calendar.removeEntry(recurrEntry);
			}
			calendar.removeEntry(e);
			removeEntryDialog.close();
		});
	}

	public void editSingleReservation(Entry entry) {
		String entryId = entry.getId();
		LocalDate ld1 = entry.getStart().toLocalDate();
		setForm(ld1);
		entryForm.fillExistingEntry(entry);
		createEntryDialog.open();
		entryForm.getSaveButton().addClickListener(ev -> {
			currentEntry = entryForm.createCurrentEntry();
			Reservation reservation = mapCalEvent.mapEntryToReservation(currentEntry);
			reservation.setId(Long.parseLong(entryId));
			reservation.setOwner(CurrentUser.get());

			HttpEntity<Reservation> res = new HttpEntity<>(reservation);
			Reservation r = clientService.updateSingleReservation(res);

			Entry singleEntry = mapCalEvent.mapReservationToEntry(r);
			calendar.removeEntry(entry);
			calendar.addEntry(singleEntry);
			createEntryDialog.close();
		});
		entryForm.getDeleteEntryButton().addClickListener(ev -> {
			Reservation reservation = mapCalEvent.mapEntryToReservation(entry);
			reservation.setOwner(CurrentUser.get());

			HttpEntity<Reservation> res = new HttpEntity<>(reservation);
			clientService.deleteReservation(res);

			calendar.removeEntry(entry);
			createEntryDialog.close();
		});
	}

	public void changeReservationDate() {
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
	}

	public void showMoreReservation() {

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

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		Set<Reservation> allRes = clientService.getAllReservations();
		if (allRes != null)
			for (Reservation reservation : allRes) {
				Entry e = mapCalEvent.mapReservationToEntry(reservation);

				/* If CurrentUser is NOT the owner, disable Entry */
				if (!CurrentUser.get().getId().equals(reservation.getOwner().getId())) {
					e.setEditable(false);
					e.setColor("dodgerblue");
				}

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

				reservation.setOwner(CurrentUser.get());
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

	public void setForm(LocalDate date) {
		createEntryDialog = new Dialog();
		entryForm = new EntryForm(date);
		entryForm.setFriends(clientService.getAllEmails());
		entryForm.setResources(clientService.getResourcesNames());
		createEntryDialog.add(entryForm);
		topBar.getGoToPicker().setValue(date);
	}

	public void setForm(LocalDateTime ldt) {
		createEntryDialog = new Dialog();
		entryForm = new EntryForm(ldt);
		entryForm.setFriends(clientService.getAllEmails());
		entryForm.setResources(clientService.getResourcesNames());
		createEntryDialog.add(entryForm);
		topBar.getGoToPicker().setValue(ldt.toLocalDate());
	}

	public void updateDatePicker() {
		topBar.getGoToPicker().addValueChangeListener(e -> {
			calendar.gotoDate(topBar.getGoToPicker().getValue());
			topBar.getTitle()
					.setText(topBar.getGoToPicker().getValue().getMonth() + " "
							+ topBar.getGoToPicker().getValue().getDayOfMonth() + ", "
							+ topBar.getGoToPicker().getValue().getYear());

			topBar.getGoToPicker().setVisible(false);
		});
	}

}
