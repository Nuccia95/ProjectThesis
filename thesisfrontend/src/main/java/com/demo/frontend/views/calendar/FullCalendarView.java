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
import com.vaadin.flow.server.VaadinSession;

import shared.thesiscommon.Book;
import shared.thesiscommon.User;

import java.io.Console;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.demo.frontend.clientservice.BookHandler;
import com.demo.frontend.views.main.MainView;

@Route(value = "calendarView", layout = MainView.class)
@PageTitle("Calendar")
public class FullCalendarView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FullCalendar calendar;
	private HorizontalLayout topBar;
	private DatePicker goToPicker;
	private Dialog entryDialog;
	private Dialog confirmChangeDialog;
	private Button confirmButton;
	private EntryForm entryForm;
	private Entry newEntry;
	private LocalDate ld;
	private final String blueColor = "#3e77c1";
	@Autowired
	private BookHandler bookHandler;
	@Autowired 
	
	public FullCalendarView() {
		setId("calendar-view");
		calendar = FullCalendarBuilder.create().build();
		createTopBar();
		add(calendar);
		setFlexGrow(1, calendar);
		manageCalendarEntry();
	}

	public void manageCalendarEntry() {

		/*ENTRY EXAMPLE*/
		Entry entry = new Entry();
		entry.setTitle("Some event");
		entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
		entry.setEnd(entry.getStart().plusHours(2));
		entry.setColor("#ff3333");
		calendar.addEntry(entry);

		/*DAY clicked*/
		calendar.addTimeslotsSelectedListener((event) -> {
			ld = event.getStartDateTime().toLocalDate();
			setForm(ld);
			entryDialog.open();
			/* create new entry */
			newEntry = new Entry();
			entryForm.getSaveButton().addClickListener(e -> {
				if (entryForm.createCurrentEntry() != null) {
					newEntry = entryForm.createCurrentEntry();
					Book book = bookHandler.mapToBook(newEntry);
					User u = (User) VaadinSession.getCurrent().getAttribute("currentUser");
					System.out.println("CURRENT USER " + u.getId() + " " + u.getEmail());
					book.setOwner(u);
					Book b = bookHandler.creteBook(book);
					if(b != null) {
						newEntry.setDescription(Long.toString(b.getId()));
						calendar.addEntry(newEntry);
						entryDialog.close();						
					}
				}
			});
		});
		
		/*EXISTING ENTRY clicked*/
		calendar.addEntryClickedListener(e -> {
			System.out.println(e.getEntry().getTitle());
			LocalDate ld1 = null;
			if(e.getEntry().isRecurring())
				ld1 = e.getEntry().getRecurringStartDate(Timezone.UTC);
			else
				ld1 = e.getEntry().getStart().toLocalDate();
			
			setForm(ld1);
			entryForm.fillExistingEntry(e.getEntry());
			entryDialog.open();
		});

		/*DRAG & DROP entry*/
		calendar.addEntryDroppedListener(e -> {
			/* change the day of the event */
			e.applyChangesOnEntry(); 
			LocalDate ld2;
			if(e.getEntry().isRecurring())
				ld2 = e.getEntry().getRecurringStartDate(Timezone.UTC);
			else
				ld2 = e.getEntry().getStart().toLocalDate();
			
			System.out.println("New date: " + e.getEntry().getStart());
			setConfirmChangeDialog(ld2);
		});
	}

	public void setForm(LocalDate ld) {
		entryDialog = setNewDialog();
		entryForm = new EntryForm(ld);
		entryDialog.add(entryForm);
		goToPicker.setValue(ld);
	}
	
	public Dialog setNewDialog() {
		Dialog d = new Dialog();
		d.setDraggable(true);
		d.setCloseOnEsc(true);
		d.setCloseOnOutsideClick(true);
		return d;
	}
	
	public void setConfirmChangeDialog(LocalDate newDate) {
		VerticalLayout container = new VerticalLayout();
		container.setFlexGrow(1, container);
		confirmChangeDialog = setNewDialog();
		
		H4 description = new H4("Update the event date to " + newDate.toString() +" ?");
		container.add(description);
		
		
		HorizontalLayout buttonsContainer = new HorizontalLayout();
		Icon confirmIcon = new Icon(VaadinIcon.CHECK);
		confirmIcon.setColor(blueColor);
		confirmButton = new Button(confirmIcon);
		
		Icon closeIcon = new Icon(VaadinIcon.CLOSE);
		closeIcon.setColor(blueColor);
		Button closeButton = new Button(closeIcon);
		
		buttonsContainer.add(confirmButton, closeButton);
		container.add(buttonsContainer);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		confirmChangeDialog.add(container);
		confirmChangeDialog.open();
		
		closeButton.addClickListener(e -> confirmChangeDialog.close());
		
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
		topBar.add(title, goToPicker);
		add(topBar);
		//ArrayList<CalendarView> calendarViews = new ArrayList<>(Arrays.asList(CalendarViewImpl.values()));
        
       /* ComboBox<CalendarView> comboBoxView = new ComboBox<>("", calendarViews);
        comboBoxView.setValue(CalendarViewImpl.DAY_GRID_MONTH);
        comboBoxView.setWidth("300px");
        comboBoxView.addValueChangeListener(e -> {
            CalendarView value = e.getValue();
            calendar.changeView(value == null ? CalendarViewImpl.DAY_GRID_MONTH : value);
        });
        //topBar.add(comboBoxView);*/
	}

}
