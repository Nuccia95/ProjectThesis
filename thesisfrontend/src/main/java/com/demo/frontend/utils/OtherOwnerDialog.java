package com.demo.frontend.utils;

import com.demo.frontend.views.resources.ReservationCard;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import shared.thesiscommon.bean.Reservation;

public class OtherOwnerDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

	public OtherOwnerDialog(Reservation res) {
		
		ReservationCard card = new ReservationCard(res);
		card.setId("card-other");
		
		AppButton appButton = new AppButton();
		Button closeButton = appButton.set("Close", VaadinIcon.CLOSE.create());
		closeButton.addClickListener(click -> close());
		
		HorizontalLayout buttcont = new HorizontalLayout();
		buttcont.add(closeButton);
		buttcont.getElement().getStyle().set("padding-left", "80%");
		add(card, buttcont);
		open();
	}

}
