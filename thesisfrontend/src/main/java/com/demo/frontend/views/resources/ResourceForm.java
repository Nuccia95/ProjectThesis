package com.demo.frontend.views.resources;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.utils.SpanDescription;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import shared.thesiscommon.bean.Resource;

public class ResourceForm extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField resourceName;
	private TextField description;
	private TextField seatsAvailable;
	private SpanDescription spanDescription;
	private HorizontalLayout buttonsContainer;
	private Button saveButton;
	private Button cancelButton;
	private VerticalLayout container;
	private AppButton appButton;
	
	public ResourceForm() {
		container = new VerticalLayout();
		spanDescription = new SpanDescription();
		container.add(spanDescription.build("CREATE"));
		appButton = new AppButton();
		
		resourceName = new TextField("Resource Name");
		description = new TextField("Description");
		seatsAvailable = new TextField("Seats Available");
		
		saveButton = appButton.set("", VaadinIcon.CHECK.create());
		cancelButton = appButton.set("", VaadinIcon.CLOSE.create());
		
		buttonsContainer = new HorizontalLayout();
		buttonsContainer.add(saveButton, cancelButton);
		container.setAlignSelf(Alignment.END, buttonsContainer);
		container.add(resourceName, description, seatsAvailable, buttonsContainer);
		add(container);
	}
	
	public void showExistingResource(Resource resource) {
		spanDescription.setSpanEdit();
		resourceName.setValue(resource.getName());
		description.setValue(resource.getDescription());
		seatsAvailable.setValue(resource.getSeatsAvailable().toString());
	}
	
	public Resource getData() {
		return new Resource(resourceName.getValue(), description.getValue(), 
				Integer.parseInt(seatsAvailable.getValue()));
	}
	
	public Button getSaveButton() {
		return saveButton;
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}
}
