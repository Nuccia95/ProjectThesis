package com.demo.frontend.utils;

import com.vaadin.flow.component.html.Span;

public class SpanDescription{
	
	private Span description;
	private static final String COLOR = "color";
	private static final String BACKGROUND = "background";
	
	public Span build(String type) {
		description = new Span();
		description.getElement().getStyle().set("padding", "2px 10px");
		description.getElement().getStyle().set("font-size", "13px");
		description.getElement().getStyle().set("border-radius", "7px");
		switch (type) {
		case "CREATE":
			description.setText("New Resource");
			description.getElement().getStyle().set(COLOR,"var(--lumo-success-color)");
			description.getElement().getStyle().set(BACKGROUND, "var(--lumo-success-color-10pct)");
			break;
		case "MOVE":
			description.setText("Move to");
			description.getElement().getStyle().set(COLOR, "#ff751a");
			description.getElement().getStyle().set(BACKGROUND, "#ffe6cc");
			break;
		case "REMOVE":
			description.setText("Remove");
			description.getElement().getStyle().set(COLOR, "#b30000");
			description.getElement().getStyle().set(BACKGROUND, "#ffebe6");
			break;
		case "LIMITED":
			description.setText("Limited");
			description.getElement().getStyle().set(COLOR, "#24478f");
			description.getElement().getStyle().set(BACKGROUND, "#99bbff");
			break;
		case "DISABLE":
			description.setText("Disable Resource");
			description.getElement().getStyle().set(COLOR, "#b30000");
			description.getElement().getStyle().set(BACKGROUND, "#ffebe6");
			break;
		case "ENABLE":
			description.setText("Enable Resource");
			description.getElement().getStyle().set(COLOR,"var(--lumo-success-color)");
			description.getElement().getStyle().set(BACKGROUND, "var(--lumo-success-color-10pct)");
			break;
		case "CARD":
			description.setText("Reservation");
			description.getElement().getStyle().set(COLOR,"var(--lumo-success-color)");
			description.getElement().getStyle().set(BACKGROUND, "var(--lumo-success-color-10pct)");
			break;
		default:
			break;
		}
		return description;
	}
	
	public Span getDescription() {
		return description;
	}

}
