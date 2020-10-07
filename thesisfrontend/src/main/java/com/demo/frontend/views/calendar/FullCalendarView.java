package com.demo.frontend.views.calendar;

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
import java.time.LocalDate;
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
	private EntryForm entryForm;
	private Entry newEntry;
	private LocalDate ld;
	private final String blueColor = "#3e77c1";
	@Autowired
	private ReservationHandler reservationHandler;
	Button button = new Button("CLICK");

	public FullCalendarView() {
		setId("calendar-view");
		calendar = FullCalendarBuilder.create().build();
		createTopBar();
		add(calendar);
		setFlexGrow(1, calendar);
		manageCalendarEntry();
	}

	public void manageCalendarEntry() {

		/* ENTRY EXAMPLE */
		Entry entry = new Entry();
		entry.setTitle("Some event");
		entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
		entry.setEnd(entry.getStart().plusHours(2));
		entry.setColor("#ff3333");
		entry.setEditable(false);
		calendar.addEntry(entry);

		/* CREATE NEW ENTRY on day clicked */
		calendar.addTimeslotsSelectedListener((event) -> {
			ld = event.getStartDateTime().toLocalDate();
			setForm(ld);
			createEntryDialog.open();
			entryForm.getSaveButton().addClickListener(e -> {
				newEntry = entryForm.createCurrentEntry();
				Reservation reservation = reservationHandler.fromEntryToReservation(newEntry);
				Reservation output;
				if (newEntry.isRecurring())
					reservationHandler.createRecurringReservation(calendar, newEntry, reservation);
				else {
					output = reservationHandler.createReservation(reservation);
					newEntry.setDescription(output.getId().toString());
				}

				calendar.addEntry(newEntry);
				createEntryDialog.close();
			});
		});

		/* EXISTING ENTRY clicked */
		calendar.addEntryClickedListener(e -> {
			if (e.getEntry().isEditable()) {
				System.out.println("Entry clicked: " + e.getEntry().getTitle());
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

		/* DRAG & DROP entry */
		calendar.addEntryDroppedListener(e -> {
			/* change the day of the event */
			e.applyChangesOnEntry();
			LocalDate newDate;
			if (e.getEntry().isRecurring())
				newDate = e.getEntry().getRecurringStartDate(Timezone.UTC);
			else
				newDate = e.getEntry().getStart().toLocalDate();
			buildUpdateDateDialog(newDate);
			confirmUpdateDateButton.addClickListener(evnt -> {
				Reservation reservation = reservationHandler.fromEntryToReservation(e.getEntry());
				reservationHandler.updateReservationDate(reservation);
				changeEntryDateDialog.close();
			});
		});
	}

	/* UTILS */
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
		Button closeButton = new Button(closeIcon);

		buttonsContainer.add(confirmUpdateDateButton, closeButton);
		container.add(buttonsContainer);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		changeEntryDateDialog.add(container);
		changeEntryDateDialog.open();

		closeButton.addClickListener(e -> changeEntryDateDialog.close());
	}

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
		topBar.add(title, goToPicker, button);
		add(topBar);
		button.addClickListener(e -> reservationHandler.getReservationByOwner());
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

}
