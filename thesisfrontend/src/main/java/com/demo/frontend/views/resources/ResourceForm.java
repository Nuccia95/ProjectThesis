package com.demo.frontend.views.resources;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.utils.SpanDescription;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import shared.thesiscommon.bean.Resource;

public class ResourceForm extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField resourceName;
	private TextArea description;
	private IntegerField seatsAvailable;
	private SpanDescription spanDescription;
	private HorizontalLayout buttonsContainer;
	private Button saveButton;
	private Button cancelButton;
	private AppButton appButton;
	
	public ResourceForm() {
		setSizeFull();
		spanDescription = new SpanDescription();
		add(spanDescription.build("CREATE"));
		
		HorizontalLayout container = new HorizontalLayout();
		
		resourceName = new TextField("Resource Name");
		resourceName.setPlaceholder("Name");
		
		seatsAvailable = new IntegerField();
		seatsAvailable.setHasControls(true);
		seatsAvailable.setMin(100);
		seatsAvailable.setPlaceholder("Num");
		seatsAvailable.setLabel("Seats Available");
		
		container.add(resourceName, seatsAvailable);

		description = new TextArea();
		description.setLabel("Description");
		description.setPlaceholder("Description, Location ..");
		description.setWidth("100%");
		description.setMaxLength(90);
		
		appButton = new AppButton();
		saveButton = appButton.set("", VaadinIcon.CHECK.create());
		cancelButton = appButton.set("", VaadinIcon.CLOSE.create());
		buttonsContainer = new HorizontalLayout();
		buttonsContainer.add(saveButton, cancelButton);

		setAlignSelf(Alignment.END, buttonsContainer);
		add(container, description, buttonsContainer);
	}
	
	public void showExistingResource(Resource resource) {
		spanDescription.setSpanEdit();
		resourceName.setValue(resource.getName());
		description.setValue(resource.getDescription());
		seatsAvailable.setValue(resource.getSeatsAvailable());
	}
	
	public Resource getData() {
		return new Resource(resourceName.getValue(), description.getValue(), 
				seatsAvailable.getValue());
	}
	
	public Button getSaveButton() {
		return saveButton;
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}
}
