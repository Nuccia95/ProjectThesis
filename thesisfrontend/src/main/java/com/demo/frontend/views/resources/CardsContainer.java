package com.demo.frontend.views.resources;

import java.util.HashSet;
import java.util.Set;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import shared.thesiscommon.bean.Reservation;

@CssImport("./styles/views/resources/card.css")
public class CardsContainer extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	AppButton appButton;
	Span title;
	Button removeAllButton;
	Button close;

	public CardsContainer() {
		setId("container");
		setVisible(false);
		appButton = new AppButton();
	}

	public void setCards(Set<Reservation> reservations, String resName) {
		/* REMOVE PREVIOUS CARDS */
		removeAll();

		HorizontalLayout titleCont = new HorizontalLayout();
		titleCont.setId("titleCont");
		titleCont.setSpacing(false);

		title = new Span(resName);
		title.setId("title");

		close = appButton.set("", VaadinIcon.ANGLE_RIGHT.create());
		close.setId("close");
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		close.addClickListener(click -> setVisible(false));

		removeAllButton = appButton.set("Clean all", VaadinIcon.TRASH.create());
		removeAllButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		removeAllButton.setId("btnremove");

		Icon info = VaadinIcon.INFO_CIRCLE.create();
		info.setId("info");
		titleCont.add(info, title, close, removeAllButton);
		titleCont.setAlignItems(Alignment.BASELINE);
		add(titleCont);

		/* ADD NEW */
		if (reservations.isEmpty()) {
			removeAllButton.setVisible(false);
			add(new Span("None reservations to display"));
		} else {
			removeAllButton.setVisible(true);
			for (Reservation reservation : reservations)
				add(new ReservationCard(reservation));
		}
		setVisible(true);
	}
	
	public Button getRemoveAllButton() {
		return removeAllButton;
	}

	public void setRemoveAllButton(Button removeAllButton) {
		this.removeAllButton = removeAllButton;
	}

	public void cleanPanel(String resName) {
		setCards(new HashSet<Reservation>(), resName);
	}

}
