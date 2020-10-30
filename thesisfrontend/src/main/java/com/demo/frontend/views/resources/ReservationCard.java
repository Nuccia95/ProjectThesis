package com.demo.frontend.views.resources;

import com.demo.frontend.utils.SpanDescription;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import shared.thesiscommon.bean.Reservation;

@CssImport("./styles/views/resources/card.css")
public class ReservationCard extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	
	public ReservationCard(Reservation res) {
		setId("card");
		setSpacing(false);
		
		HorizontalLayout titleCont = new HorizontalLayout();
		SpanDescription spanDescription = new SpanDescription();

		Span title;
		if(res.isRecurring())
			title = new Span(res.getTitle() + " | " + "Recurrent");		
		else
			title = new Span(res.getTitle());	
		
		title.setId("titleCard");

		titleCont.setAlignItems(Alignment.BASELINE);
		titleCont.add(spanDescription.build("CARD"), title);
		
		
		HorizontalLayout infoContainer = new HorizontalLayout();
		infoContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);

		infoContainer.add(setLineInfo("Start", res.getStartDate() + "   " + res.getStartTime()));
		infoContainer.add(setLineInfo("End", res.getEndDate() + "   " + res.getEndTime()));
		infoContainer.add(setLineInfo("Owner", res.getOwner().getFirstName() + " " + res.getOwner().getLastName()));
		
		add(titleCont, infoContainer);
	}

	public VerticalLayout setLineInfo(String label, String value) {
		VerticalLayout l = new VerticalLayout();
		l.setSpacing(false);
		Span labelSpan = new Span(label);
		labelSpan.setId("label");
		Span valueSpan = new Span(value);
		valueSpan.setId("value");
		l.add(labelSpan, valueSpan);
		
		return l;
	}
}
