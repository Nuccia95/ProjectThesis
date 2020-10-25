package com.demo.frontend.utils;

import com.vaadin.flow.component.html.Span;

public class SpanDescription{
	
	private Span description;
	
	public Span build(String type) {
		description = new Span();
		description.getElement().getStyle().set("padding", "2px 10px");
		description.getElement().getStyle().set("font-size", "14px");
		description.getElement().getStyle().set("border-radius", "7px");
		switch (type) {
		case "CREATE":
			description.setText("New Resource");
			description.getElement().getStyle().set("color","var(--lumo-success-color)");
			description.getElement().getStyle().set("background", "var(--lumo-success-color-10pct)");
			break;
		case "MOVE":
			description.setText("Move to");
			description.getElement().getStyle().set("color", "#ff751a");
			description.getElement().getStyle().set("background", "#ffe6cc");
			break;
		case "REMOVE":
			description.setText("Remove");
			description.getElement().getStyle().set("color", "#b30000");
			description.getElement().getStyle().set("background", "#ffebe6");
			break;
		case "LIMITED":
			description.setText("Limited");
			description.getElement().getStyle().set("color", "#24478f");
			description.getElement().getStyle().set("background", "#99bbff");
			break;
		case "OTHER":
			description.setText("Owned by another user");
			description.getElement().getStyle().set("color", "#ffaa00");
			description.getElement().getStyle().set("background", "#ffeecc");
			break;
		default:
			break;
		}
		return description;
	}
	
	public void setSpanEdit() {
		description.setText("Edit");
		description.getElement().getStyle().set("color","#cccc00");
		description.getElement().getStyle().set("background", "#ffffe6");
	}
	
	public Span getDescription() {
		return description;
	}

}
