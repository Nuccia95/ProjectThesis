package com.demo.frontend.views.calendar;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.LocalDate;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import com.demo.frontend.views.main.MainView;

@Route(value = "calendarView", layout = MainView.class)
@PageTitle("Calendar")
public class CalendarView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FullCalendar calendar;
	private HorizontalLayout topBar;
	private DatePicker goToPicker;
	private Dialog entryDialog;
	private Entry newEntry;
	private LocalDate ld;

	public CalendarView() {
		setId("calendar-view");
		calendar = FullCalendarBuilder.create().build();
		setFlexGrow(1, calendar);
		createTopBar();
		add(calendar);
		manageCalendarEntry();
	}

	public void manageCalendarEntry() {

		// EXAMPLE: Create a initial sample entry CARICARE LE ENTRY DI QUESTO UTENTE E
		// MOSTRARE QUELLE DEI COLLEGHI
		Entry entry = new Entry();
		entry.setTitle("Some event");
		entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
		entry.setEnd(entry.getStart().plusHours(2));
		entry.setColor("#ff3333");
		calendar.addEntry(entry);

		// Show a dialog to create new entries or modify existing ones
		calendar.addTimeslotsSelectedListener((event) -> {
			// day clicked
			ld = event.getStartDateTime().toLocalDate();
			entryDialog = new Dialog();
			setEntryDialogOptions();
			EntryForm entryForm = new EntryForm(ld);
			entryDialog.add(entryForm);
			entryDialog.open();
			goToPicker.setValue(ld);
			newEntry = new Entry();

			entryForm.getSaveButton().addClickListener(e -> {
				if (entryForm.createCurrentEntry() != null) {
					newEntry = entryForm.createCurrentEntry();
					calendar.addEntry(newEntry);
					entryDialog.close();
				}
			});
		});

		/*
		 * The entry click event listener is called when the user clicks on an existing
		 * entry. The event provides the clicked event which might be then opened in a
		 * dialog
		 */
		/* clicked entry */
		calendar.addEntryClickedListener(e -> {
			System.out.println(e.getEntry().getTitle());
		});

		/* drag and drop entry */
		calendar.addEntryDroppedListener(e -> {
			e.applyChangesOnEntry(); // CAMBIA IL GIORNO DELL'EVENTO
			System.out.println(e.getEntry().getStart());
		});
	}

	public void setEntryDialogOptions() {
		entryDialog.setDraggable(true);
		entryDialog.setCloseOnEsc(true);
		entryDialog.setCloseOnOutsideClick(true);
	}

	public void createTopBar() {
		topBar = new HorizontalLayout();
		setSizeFull();

		goToPicker = new DatePicker();
		goToPicker.setValue(LocalDate.now());
		topBar.add(goToPicker);
		goToPicker.addValueChangeListener(e -> {
			calendar.gotoDate(goToPicker.getValue());
		});

		Button themeButton = new Button(VaadinIcon.PAINT_ROLL.create());
		ThemeList themeList = UI.getCurrent().getElement().getThemeList();
		themeButton.addClickListener(e->{
			if (themeList.contains(Lumo.DARK)) 
				themeList.remove(Lumo.DARK);
			else
				themeList.add(Lumo.DARK);			
		});
		
		topBar.add(themeButton);
	/*
	 * PreviousMonth button - NextMonth button previousMonthButton = new
	 * Button("Previous Month", VaadinIcon.ANGLE_LEFT.create());
	 * previousMonthButton.addClickListener(e -> calendar.previous());
	 * previousMonthButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	 * nextMonthButton = new Button("Next Month", VaadinIcon.ANGLE_RIGHT.create());
	 * nextMonthButton.addClickListener(e -> calendar.next());
	 * nextMonthButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	 * topBar.add(previousMonthButton, nextMonthButton);
	 */

	      add(topBar);
	}
}
