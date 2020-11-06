package com.demo.frontend.views.calendar;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.vaadin.stefan.fullcalendar.BusinessHours;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.utils.OtherOwnerDialog;
import com.demo.frontend.utils.QuestionDialog;
import com.demo.frontend.utils.SpanDescription;
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
	private QuestionDialog moveEntryDialog;
	private QuestionDialog resizeEntryDialog;

	public FullCalendarView() {

		getStyle().set("flex-grow", "1");
		setSpacing(false);
		setClassName("fullcalendar-view");

		mapCalEvent = new MapCalendarEvent();

		topBar = new TopBarCalendar();
		add(topBar.buildTopBar());

		createCalendar();

		if (Boolean.FALSE.equals(CurrentUser.isViewer()))
			manageReservations();

		updateDatePicker();
		setTopBarButtons();
	}

	public void createCalendar() {
		calendar = FullCalendarBuilder.create().withEntryLimit(3).build();
		calendar.setNumberClickable(false);
		calendar.setFirstDay(DayOfWeek.MONDAY);
		calendar.setMinTime(LocalTime.of(8, 0));
		calendar.setMaxTime(LocalTime.of(20, 0));
		calendar.setBusinessHours(new BusinessHours(LocalTime.of(8, 0), LocalTime.of(20, 0)));
		calendar.setNowIndicatorShown(true);
		add(calendar);
		setFlexGrow(1, calendar);
		setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

		topBar.getViewBox().addValueChangeListener(e -> {
			CalendarView value = e.getValue();
			calendar.changeView(value == null ? CalendarViewImpl.DAY_GRID_MONTH : value);
		});

		topBar.getTodayButton().addClickListener(e -> {
			calendar.today();
			topBar.getGoToPicker().setValue(LocalDate.now());
			topBar.getTitle().setText(LocalDate.now().getMonth() + " " + LocalDate.now().getDayOfMonth() + ", "
					+ LocalDate.now().getYear());
		});
	}

	/* FILL CALENDAR WITH RESERVATIONS */
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
					e.setTitle(e.getTitle() + " | " + reservation.getOwner().getFirstName() + " "
							+ reservation.getOwner().getLastName());
					e.setDescription("notmy");
					e.setColor("#191970");
				} else {
					if (reservation.getStartDate().isBefore(LocalDate.now()))
						e.setColor("Texas");
				}
				calendar.addEntry(e);
			}
	}

	public void manageReservations() {
		createReservation();
		editReservation();
		changeReservationDateTime();
		showMoreReservations();
		resizeEntry();
	}

	/* CREATE RESERVATION */
	public void createReservation() {
		calendar.addTimeslotsSelectedListener(event -> {
			LocalDateTime ldt = event.getStartDateTime();

			if (ldt.toLocalDate().isBefore(LocalDate.now())) {
				Notification.show("PAST DATE selected", 2000, Position.TOP_START)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			} else {
				initForm(ldt);
				entryForm.open();
				entryForm.getSaveButton().addClickListener(e -> {
					Entry currentEntry = entryForm.createCurrentEntry();
					if (currentEntry != null) {
						if (currentEntry.isRecurring())
							createRecurringReservations(currentEntry, entryForm.getResourceName());
						else
							createSingleReservation(currentEntry, entryForm.getResourceName());
						entryForm.close();
					}
				});
			}
		});
	}

	/* EDIT RESERVATIONS */
	public void editReservation() {
		calendar.addEntryClickedListener(e -> {
			Entry entry = e.getEntry();
			if (!entry.getDescription().equals("notmy")) {
				if (entry.isRecurring() && entry.getDescription().equals("0") && !entry.isEditable())
					deleteRecurringReservation(entry);
				if (entry.isEditable())
					editSingleReservation(entry);
			} else {
				Reservation res = clientService.getReservationById(Long.parseLong(entry.getId()));
				new OtherOwnerDialog(res);
			}
		});
	}

	/* DELETE RECURRING RESERVATIONS */
	public void deleteRecurringReservation(Entry e) {

		QuestionDialog removeEntryDialog = new QuestionDialog("DELETE this reservation?", "REMOVE");

		removeEntryDialog.getConfirmButton().addClickListener(ev -> {

			Resource relatedResource = clientService.getRelatedResource(Long.parseLong(e.getId()));
			Reservation reservation = mapCalEvent.mapEntryToReservation(e, relatedResource.getName());
			reservation.setOwner(CurrentUser.get());
			reservation.setId(Long.parseLong(e.getId()));

			HttpEntity<Reservation> res = new HttpEntity<>(reservation);
			clientService.deleteRecurringReservations(res);

			for (Entry recurrEntry : calendar.getEntries()) {
				if (recurrEntry.getDescription() != null
						&& recurrEntry.getDescription().equals(reservation.getId().toString()))
					calendar.removeEntry(recurrEntry);
			}
			calendar.removeEntry(e);
			Notification.show("Reservations DELETED", 2000, Position.BOTTOM_START);
			removeEntryDialog.close();
		});
	}

	public void editSingleReservation(Entry entry) {

		String entryId = entry.getId();
		LocalDateTime ldt = entry.getStart();
		initForm(ldt);
		Resource relatedResource = clientService.getRelatedResource(Long.parseLong(entryId));
		entryForm.fillExistingEntry(entry, relatedResource);
		entryForm.open();

		entryForm.getSaveButton().addClickListener(ev -> {
			Entry currentEntry = entryForm.createCurrentEntry();
			if (currentEntry != null) {

				Reservation reservation = mapCalEvent.mapEntryToReservation(currentEntry, entryForm.getResourceName());
				reservation.setId(Long.parseLong(entryId));
				reservation.setOwner(CurrentUser.get());

				if (entryForm.getFriendsEmails() != null)
					reservation.setReceivers(entryForm.getFriendsEmails());

				HttpEntity<Reservation> res = new HttpEntity<>(reservation);

				if (clientService.checkAvailableResource(res)) {
					Reservation rUpdated = clientService.updateSingleReservation(res);
					Entry singleEntry = mapCalEvent.mapReservationToEntry(rUpdated);
					calendar.removeEntry(entry);
					calendar.addEntry(singleEntry);
					Notification.show("Reservation UPDATED", 2000, Position.BOTTOM_START)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					entryForm.close();
				} else {
					Notification.show("Resource NOT AVAILABLE in TIME selected", 4000, Position.BOTTOM_START)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
					entryForm.close();
					return;
				}
			}
		});

		entryForm.getDeleteEntryButton().addClickListener(ev -> {
			clientService.deleteReservation(Long.parseLong(entry.getId()));
			calendar.removeEntry(entry);
			Notification.show("Reservation DELETED", 2000, Position.BOTTOM_START);
			entryForm.close();
		});
	}

	/* DRAG & DROP: CHANGE RESERVATION DATE-TIME */
	public void changeReservationDateTime() {
		calendar.addEntryDroppedListener(e -> {
			Entry entry = e.getEntry();
			LocalDateTime oldStart = e.getEntry().getStart();
			LocalDateTime oldEnd = e.getEntry().getEnd();

			if (entry.isEditable()) {
				e.applyChangesOnEntry();
				LocalDateTime newStart = e.getEntry().getStart();

				if (newStart.toLocalDate().isBefore(LocalDate.now())) {
					Notification.show("PAST DATE selected", 2000, Position.TOP_START)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
					resetOldEntry(e.getEntry(), oldStart, oldEnd);
				} else {

					String text = "";
					if (!oldStart.toLocalTime().equals(e.getEntry().getStart().toLocalTime()))
						text = "Update to new DATE " + newStart.toLocalDate() + " and new TIME "
								+ newStart.toLocalTime() + "?";
					else
						text = "Update to new DATE " + newStart.toLocalDate() + "?";

					moveEntryDialog = new QuestionDialog(text, "MOVE");
					moveEntryDialog.getConfirmButton().addClickListener(evnt -> {

						Resource relatedResource = clientService.getRelatedResource(Long.parseLong(entry.getId()));
						Reservation reservation = mapCalEvent.mapEntryToReservation(entry, relatedResource.getName());
						reservation.setId(Long.parseLong(e.getEntry().getId()));
						reservation.setOwner(CurrentUser.get());
						reservation.setReceivers(new ArrayList<>());

						HttpEntity<Reservation> res = new HttpEntity<>(reservation);

						if (clientService.checkAvailableResource(res)) {
							clientService.updateSingleReservation(res);
						} else {
							Notification.show("Resource NOT AVAILABLE in TIME selected", 4000, Position.BOTTOM_START)
									.addThemeVariants(NotificationVariant.LUMO_ERROR);
							resetOldEntry(entry, oldStart, oldEnd);
							moveEntryDialog.close();
							return;
						}

						moveEntryDialog.close();
					});
					moveEntryDialog.getCloseButton().addClickListener(evnt -> {
						resetOldEntry(entry, oldStart, oldEnd);
						moveEntryDialog.close();
					});

				}
			}
		});
	}

	/* RESIZE ENTRY: CHANGE RESERVATION DATE-TIME */
	public void resizeEntry() {
		calendar.addEntryResizedListener(e -> {
			Entry entry = e.getEntry();
			LocalDateTime oldStart = entry.getStart();
			LocalDateTime oldEnd = entry.getEnd();
			if (entry.isEditable()) {
				e.applyChangesOnEntry();
				resizeEntryDialog = new QuestionDialog("Update END time to " + entry.getEnd().toLocalTime() + " ?",
						"MOVE");

				resizeEntryDialog.getConfirmButton().addClickListener(click -> {

					Resource relatedResource = clientService.getRelatedResource(Long.parseLong(entry.getId()));
					Reservation reservation = mapCalEvent.mapEntryToReservation(entry, relatedResource.getName());
					reservation.setId(Long.parseLong(e.getEntry().getId()));
					reservation.setOwner(CurrentUser.get());
					reservation.setReceivers(new ArrayList<>());

					HttpEntity<Reservation> res = new HttpEntity<>(reservation);
					if (clientService.checkAvailableResource(res)) {
						clientService.updateSingleReservation(res);
					} else {
						Notification.show("Resource NOT AVAILABLE in TIME selected", 4000, Position.BOTTOM_START)
								.addThemeVariants(NotificationVariant.LUMO_ERROR);
						resetOldEntry(entry, oldStart, oldEnd);
						resizeEntryDialog.close();
						return;
					}

					resizeEntryDialog.close();

				});

				resizeEntryDialog.getCloseButton().addClickListener(click -> {
					resetOldEntry(entry, oldStart, oldEnd);
					resizeEntryDialog.close();
				});
			}

		});
	}

	public void showMoreReservations() {
		/* SHOW MORE RESERVATIONS */
		SpanDescription spanDescription = new SpanDescription();
		AppButton appButton = new AppButton();
		calendar.addLimitedEntriesClickedListener(event -> {
			Collection<Entry> entries = calendar.getEntries(event.getClickedDate());
			if (!entries.isEmpty()) {
				Dialog dialog = new Dialog();
				dialog.add(spanDescription.build("LIMITED"));
				VerticalLayout dialogLayout = new VerticalLayout();
				dialogLayout.setId("more-container");
				dialogLayout.setSpacing(true);
				dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
				for (Entry e : entries) {
					Button b = createClickableEntry(e);
					if (e.isEditable()) {
						b.addClickListener(click -> {
							LocalDateTime ldt = LocalDateTime.of(event.getClickedDate(), LocalTime.of(0, 0));
							initForm(ldt);
							Resource res = clientService.getRelatedResource(Long.parseLong(e.getId()));
							entryForm.fillExistingEntry(e, res);
							entryForm.open();
						});
					}
					if (e.getDescription().equals("notmy")) {
						b.addClickListener(click -> {
							Reservation res = clientService.getReservationById(Long.parseLong(e.getId()));
							new OtherOwnerDialog(res);
						});
					}
					dialogLayout.add(b);
				}
				Button close = appButton.set("Close", VaadinIcon.CLOSE.create());
				close.addClickListener(ev -> dialog.close());
				dialogLayout.add(close);
				dialogLayout.setAlignSelf(Alignment.END, close);
				dialog.add(dialogLayout);
				dialog.open();
			}
		});
	}

	public void createSingleReservation(Entry newEntry, String relatedResource) {
		Reservation reservation = mapCalEvent.mapEntryToReservation(newEntry, relatedResource);
		reservation.setOwner(CurrentUser.get());

		HttpEntity<Reservation> resToSend = new HttpEntity<>(reservation);

		/* check if the resource is available during date and time selected */
		if (clientService.checkAvailableResource(resToSend)) {

			if (entryForm.getFriendsEmails() != null)
				reservation.setReceivers(entryForm.getFriendsEmails());

			HttpEntity<Reservation> resToAdd = new HttpEntity<>(reservation);
			Reservation resAdded = clientService.createReservation(resToAdd);

			Entry singleEntry = mapCalEvent.mapReservationToEntry(resAdded);
			calendar.addEntry(singleEntry);
			Notification.show("Reservation ADDED", 2000, Position.BOTTOM_START)
					.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		} else {
			Notification.show("Resource NOT AVAILABLE in TIME selected", 4000, Position.BOTTOM_START)
					.addThemeVariants(NotificationVariant.LUMO_ERROR);
		}

	}

	public void createRecurringReservations(Entry newEntry, String relatedResource) {
		Reservation reservation = mapCalEvent.mapEntryToReservation(newEntry, relatedResource);
		LocalDate start = newEntry.getRecurringStartDate(Timezone.UTC);
		LocalDate end = newEntry.getRecurringEndDate(Timezone.UTC);
		Long groupId = null;
		reservation.setEndRecurring(end);
		if (entryForm.getFriendsEmails() != null)
			reservation.setReceivers(entryForm.getFriendsEmails());

		if (entryForm.getDays() != null)
			reservation.setDays(entryForm.getDays());

		while (start.isBefore(end)) {
			DayOfWeek day = start.getDayOfWeek();
			if (newEntry.getRecurringDaysOfWeeks().contains(day)) {
				reservation.setDayOfWeek(day.toString());
				reservation.setStartDate(start);
				/* see as single day */
				reservation.setEndDate(start);
				reservation.setRecurring(true);
				reservation.setEditable(false);
				if (groupId != null)
					reservation.setGroupId(groupId);

				reservation.setOwner(CurrentUser.get());
				HttpEntity<Reservation> res = new HttpEntity<>(reservation);

				if (clientService.checkAvailableResource(res)) {
					Reservation createdReservation = clientService.createReservation(res);

					if (groupId == null)
						groupId = createdReservation.getId();

					Entry e = mapCalEvent.mapReservationToEntry(createdReservation);
					calendar.addEntry(e);
				} else {
					Notification.show("Resource NOT AVAILABLE in TIME selected", 4000, Position.BOTTOM_START)
							.addThemeVariants(NotificationVariant.LUMO_ERROR);
					return;
				}
			}
			start = start.plusDays(1);
		}
		Notification.show("Reservation ADDED", 2000, Position.BOTTOM_START)
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}

	/* UTILS */
	public void resetOldEntry(Entry e, LocalDateTime oldStart, LocalDateTime oldEnd) {
		calendar.removeEntry(e);
		Entry oldEntry = e;
		oldEntry.setStart(oldStart);
		oldEntry.setEnd(oldEnd);
		calendar.addEntry(oldEntry);
	}

	public void initForm(LocalDateTime ldt) {
		entryForm = new EntryForm(ldt);
		entryForm.setFriends(clientService.getAllEmails());
		/* SHOW ONLY ENABLED RESOURCE */
		entryForm.setResources(clientService.getResourcesNames());
		topBar.getGoToPicker().setValue(ldt.toLocalDate());
	}

	public void updateDatePicker() {
		topBar.getGoToPicker().addValueChangeListener(e -> {
			calendar.gotoDate(topBar.getGoToPicker().getValue());
			topBar.getTitle()
					.setText(topBar.getGoToPicker().getValue().getMonth() + " "
							+ topBar.getGoToPicker().getValue().getDayOfMonth() + ", "
							+ topBar.getGoToPicker().getValue().getYear());
		});
	}

	public void setTopBarButtons() {

		topBar.getPreviousButton().addClickListener(click -> {
			CalendarView view = topBar.getViewBox().getValue();
			calendar.previous();
			if (view.equals(CalendarViewImpl.DAY_GRID_MONTH) || view.equals(CalendarViewImpl.LIST_MONTH))
				topBar.previousDate("MONTH");
			else if (view.equals(CalendarViewImpl.TIME_GRID_WEEK))
				topBar.previousDate("WEEK");
			else if (view.equals(CalendarViewImpl.TIME_GRID_DAY))
				topBar.previousDate("DAY");
		});

		topBar.getNextButton().addClickListener(click -> {
			CalendarView view = topBar.getViewBox().getValue();
			calendar.next();
			if (view.equals(CalendarViewImpl.DAY_GRID_MONTH) || view.equals(CalendarViewImpl.LIST_MONTH))
				topBar.nextDate("MONTH");
			else if (view.equals(CalendarViewImpl.TIME_GRID_WEEK))
				topBar.nextDate("WEEK");
			else if (view.equals(CalendarViewImpl.TIME_GRID_DAY))
				topBar.nextDate("DAY");
		});
	}

	public Button createClickableEntry(Entry entry) {
		Button button = new Button(entry.getTitle());
		button.setId("clickable-entry");
		Style style = button.getStyle();
		style.set("background-color", Optional.ofNullable(entry.getColor()).orElse("rgb(58, 135, 173)"));
		return button;
	}
}
