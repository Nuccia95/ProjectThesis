package com.demo.frontend.views.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.Timezone;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.views.reservationforms.InviteFriendsForm;
import com.demo.frontend.views.reservationforms.RecurringEntryForm;
import com.demo.frontend.views.reservationforms.SingleEntryForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

@CssImport("./styles/views/forms/forms.css")
public class EntryForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private LocalDate ld;
	private LocalTime lt;

	private Tab tab1;
	private SingleEntryForm page1;
	private Tab tab2;
	private RecurringEntryForm page2;
	private Tab tab3;
	private InviteFriendsForm page3;
	private Map<Tab, Component> tabsToPages;
	
	private Button deleteEntryButton;

	public EntryForm(LocalDateTime date) {
		
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		
		ld = date.toLocalDate();
		LocalTime lt0 = LocalTime.parse("00:00");
		if (date.toLocalTime().equals(lt0) || date.toLocalTime() == null)
			lt = null;
		else
			lt = date.toLocalTime();

		tab1 = new Tab("New Reservation");
		tab1.setId("tab");
		page1 = new SingleEntryForm(ld, lt);
		
		tab2 = new Tab("Recurring Reservation?");
		tab2.setId("tab");
		page2 = new RecurringEntryForm(ld);
		page2.setVisible(false);
		
		tab3 = new Tab("Invite Friends?");
		tab3.setId("tab");
		page3 = new InviteFriendsForm();
		page3.setVisible(false);
		
		tabsToPages = new HashMap<>();

		tabsToPages.put(tab1, page1);
		tabsToPages.put(tab2, page2);
		tabsToPages.put(tab3, page3);
		Tabs tabs = new Tabs(tab1, tab2, tab3);
		
		tabs.setFlexGrowForEnclosedTabs(1);
		Div pages = new Div(page1, page2, page3);
		
		tabs.addSelectedChangeListener(event -> {
			tabsToPages.values().forEach(page -> page.setVisible(false));
			Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);
		});
		
		add(tabs, pages);
	}

	public EntryForm(LocalDate date) {
		setSizeFull();
		ld = date;
	}

	public Entry createCurrentEntry() {

		LocalDateTime lstart = LocalDateTime.of(ld, page1.getStartTimePicker().getValue());
		LocalDateTime lend = LocalDateTime.of(ld, page1.getEndTimePicker().getValue());
		
		if(lstart.isAfter(lend) ) {
			Notification.show("Check time, START and END of reservation", 2000, Position.TOP_START).addThemeVariants(NotificationVariant.LUMO_ERROR);
			return null;
		}

		Entry newEntry = new Entry();
		newEntry.setColor(page1.getComboBoxColors().getValue());
		newEntry.setTitle(page1.getComboBoxResources().getValue());
		newEntry.setEditable(true);

		if (Boolean.TRUE.equals(page2.hasData())) {
			newEntry.setRecurring(true);
			newEntry.setRecurringStartDate(ld, Timezone.UTC);
			newEntry.setRecurringStartTime(page1.getStartTimePicker().getValue());
			newEntry.setRecurringEndDate(page2.getEndDatePicker().getValue(), Timezone.UTC);
			newEntry.setRecurringEndTime(page1.getEndTimePicker().getValue());
			newEntry.setRecurringDaysOfWeeks(page2.getDays().getValue());
			if(lstart.toLocalDate().isAfter(page2.getEndDatePicker().getValue())) {
				Notification.show("Check date, START and END of reservation", 2000, Position.TOP_START).addThemeVariants(NotificationVariant.LUMO_ERROR);
				return null;
			}
			
		} else {
			newEntry.setStart(lstart);
			newEntry.setEnd(lend);
		}

		return newEntry;
	}

	/* Fill form with value of existing entry */
	public void fillExistingEntry(Entry entry) {
		
		page1.getComboBoxResources().setValue(entry.getTitle());
		page1.getComboBoxColors().setValue(entry.getColor());

		page1.getStartTimePicker().setValue(entry.getStart().toLocalTime());
		page1.getEndTimePicker().setValue(entry.getEnd().toLocalTime());

		AppButton appButton = new AppButton();
		deleteEntryButton = appButton.set("Delete", VaadinIcon.TRASH.create());
		
		page2.setEnabled(false);
		page3.setEnabled(false);
		
		page1.getButtContainer().add(deleteEntryButton);
	}
	
	public List<String> getFriendsEmails(){
		return page3.selectedFriends();
	}
	
	public List<String> getDays(){
		List<String> list = new ArrayList<>();
		for (DayOfWeek d : page2.getDays().getValue())
			list.add(d.toString());
		return list;
	}
	
	public Button getSaveButton() {
		return page1.getSaveEntryButton();
	}

	public void setResources(List<String> resources) {
		page1.getComboBoxResources().setItems(resources);
	}

	public void setFriends(List<String> emails) {
		page3.getBoxFriends().setItems(emails);
	}

	public Button getDeleteEntryButton() {
		return deleteEntryButton;
	}

}