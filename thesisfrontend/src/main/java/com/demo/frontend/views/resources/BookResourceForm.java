package com.demo.frontend.views.resources;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.timepicker.TimePicker;
/**
 * A form for editing a single product.
 */
public class BookResourceForm extends Div {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final VerticalLayout content;

    private final H2 productName;
    private DatePicker datePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    
    private Button closeButton;
    private Button saveButton;
    private Button cancelButton;


    private Book currentBook;

    public BookResourceForm() {
        setClassName("product-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("product-form-content");
        add(content);
        
        productName = new H2("Product name");
        productName.setWidth("100%");
        
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClickListener(event -> hideForm());
        content.setAlignSelf(Alignment.START, closeButton);
        content.add(closeButton);

        final HorizontalLayout horizontalLayout1 = new HorizontalLayout(productName, closeButton);
        horizontalLayout1.setWidth("100%");
        horizontalLayout1.setFlexGrow(1, productName, closeButton);
        content.add(horizontalLayout1);
        

        datePicker = new DatePicker();
        datePicker.setLabel("Select date");
		Div value = new Div();
		value.setText("Select a value");
		datePicker.addValueChangeListener(event -> {
			if (event.getValue() == null) {
				value.setText("No date selected");
			} else {
				value.setText("Selected date: " + event.getValue());
			}
		});
		content.add(datePicker, value);

		startTimePicker = new TimePicker();
		startTimePicker.setLabel("Start Time");
		endTimePicker = new TimePicker();
		endTimePicker.setLabel("End Time");
		
		final HorizontalLayout horizontalLayout2 = new HorizontalLayout(startTimePicker, endTimePicker);
        horizontalLayout2.setWidth("100%");
        horizontalLayout2.setFlexGrow(1, startTimePicker, endTimePicker);
        content.add(horizontalLayout2);

        saveButton = new Button("Save", VaadinIcon.CHECK.create());
        saveButton.setWidth("100%");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        /*save.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProduct(currentProduct);
            }
        });*/
        saveButton.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
        
        cancelButton = new Button("Cancel", VaadinIcon.CLOSE_CIRCLE.create());
        cancelButton.setWidth("100%");
        //cancel.addClickListener(event -> viewLogic.cancelProduct());
        cancelButton.addClickShortcut(Key.ESCAPE);
        /*getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");*/

        final HorizontalLayout horizontalLayout3 = new HorizontalLayout(saveButton, cancelButton);
        horizontalLayout3.setWidth("100%");
        horizontalLayout3.setFlexGrow(1, saveButton, cancelButton);
        content.add(horizontalLayout3);

    }

    public void showForm(boolean show, Book b) {
        setVisible(show);
        setEnabled(show);
        productName.setText(b.getName());
        System.out.println("item selezionato: " + b.getName());
    }
    
    public void hideForm() {
    	setVisible(false);
        setEnabled(false);
    }
    
}
