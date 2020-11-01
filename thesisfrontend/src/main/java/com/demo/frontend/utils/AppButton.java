package com.demo.frontend.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;

public class AppButton {

	public Button set(String text, Icon icon) {
		
		Button button;
		icon.setId("icon-button");

		if (!text.equals(""))
			button = new Button(text, icon);
		else
			button = new Button(icon);
		
		button.setId("app-button");
		button.addThemeVariants(ButtonVariant.LUMO_SMALL);
		
		return button;
	}

}
