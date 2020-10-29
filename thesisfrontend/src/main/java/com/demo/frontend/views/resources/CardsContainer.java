package com.demo.frontend.views.resources;

import java.util.Set;

import com.demo.frontend.utils.AppButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import shared.thesiscommon.bean.Reservation;

@CssImport("./styles/views/resources/card.css")
public class CardsContainer extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	AppButton appButton;
	H4 title;

	public CardsContainer() {
		setId("container");
		setVisible(false);
		appButton = new AppButton();
	}

	public void setCards(Set<Reservation> reservations, String resName) {
		/* REMOVE PREVIOUS CARDS */
		if (getComponentCount() > 0)
			removeAll();

		HorizontalLayout titleCont = new HorizontalLayout();
		titleCont.setId("titleCont");
		titleCont.setSpacing(false);

		title = new H4("Resource: " + resName);

		Button close = appButton.set("", VaadinIcon.ANGLE_RIGHT.create());
		close.setId("close");
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		close.addClickListener(click -> setVisible(false));

		titleCont.add(title, close);
		titleCont.setAlignItems(Alignment.BASELINE);
		add(titleCont);

		/* ADD NEW */
		if (reservations.isEmpty()) {
			add(new Span("None reservations to display."));
		} else
			for (Reservation reservation : reservations)
				add(new ReservationCard(reservation));

		setVisible(true);
	}

}
