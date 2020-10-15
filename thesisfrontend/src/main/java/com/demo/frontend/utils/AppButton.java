package com.demo.frontend.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;

public class AppButton {

	public Button set(String text, Icon icon) {
		
		Button button;
		
		if (!text.equals(""))
			button = new Button(text, icon);
		else
			button = new Button(icon);
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		return button;
	}

}
