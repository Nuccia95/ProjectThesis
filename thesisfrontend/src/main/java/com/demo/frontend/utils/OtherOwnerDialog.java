package com.demo.frontend.utils;

import com.demo.frontend.views.resources.ReservationCard;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

import shared.thesiscommon.bean.Reservation;

public class OtherOwnerDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

	public OtherOwnerDialog(Reservation res) {
		
		ReservationCard card = new ReservationCard(res);
		card.setId("card-other");
		
		AppButton appButton = new AppButton();
		Button closeButton = appButton.set("Close", VaadinIcon.CLOSE.create());
		closeButton.addClickListener(click -> close());
		
		HorizontalLayout container = new HorizontalLayout();
		container.setAlignItems(Alignment.BASELINE);
		container.getElement().getStyle().set("margin-left", "15px");
		Span label = new Span("Resource Related: ");
		label.getStyle().set("font-size", "14px");
		Span value = new Span(res.getResource().getName());
		value.getStyle().set("font-size", "14px");
		value.getStyle().set("font-weight", "bold");
		container.add(label, value);
		
		HorizontalLayout buttcont = new HorizontalLayout();
		buttcont.add(closeButton);
		buttcont.getElement().getStyle().set("padding-left", "80%");
		add(card, container, buttcont);
		open();
	}

}
