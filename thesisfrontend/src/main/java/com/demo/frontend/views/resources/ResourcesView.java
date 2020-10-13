package com.demo.frontend.views.resources;

import java.util.ArrayList;

import com.demo.frontend.views.main.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.Resource;

@Route(value = "resourcesView", layout = MainView.class)
@PageTitle("Resources")
public class ResourcesView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField filter;
	private Button newResourceButton;
	private Grid<Resource> grid;
	private ResourceForm resourcesForm;
	private Dialog formDialog;
	
	public ResourcesView() {
		setSizeFull();
		setGrid();
		add(createTopBar(), grid);
		manageResources();
	}
	
	public void setGrid() {
		grid = new Grid<>();
		grid.setSizeFull();
        grid.addColumn(Resource::getName).setHeader("Name").setSortable(true).setKey("Name");
        grid.addColumn(Resource::getDescription).setHeader("Description");
		grid.addColumn(Resource::getSeatsAvailable).setHeader("Seats Available");
		/* riempi griglia */
		ArrayList<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("Server A", "...", 0));
		resources.add(new Resource("Server B", "...", 0));
		grid.setItems(resources);
	}
	
	public void manageResources() {
		grid.addItemClickListener(event -> {
			setForm();
			resourcesForm.showExistingResource(event.getItem());
			formDialog.open();
			System.out.println(event.getItem().getName());
		});
	}

	public HorizontalLayout createTopBar() {
		filter = new TextField();
		filter.setPlaceholder("Filter name, seats available ..");
		filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

		newResourceButton = new Button("New resource");
//		if(CurrentUser.getRole().equals("USER"))
//			newResourceButton.setEnabled(false);
		// Setting theme variant of new production button to LUMO_PRIMARY that
		// changes its background color to blue and its text color to white
		newResourceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		newResourceButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
		newResourceButton.addClickListener(ev -> {
			setForm();
			formDialog.open();
		});
		
		final HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setWidth("50%");
		topLayout.add(filter, newResourceButton);
		topLayout.add(newResourceButton);
		topLayout.setVerticalComponentAlignment(Alignment.START, filter);
		topLayout.expand(filter);
		return topLayout;
	}

	public void showNotification(String msg) {
		Notification.show(msg);
	}
	
	public void setForm() {
		formDialog = new Dialog();
		resourcesForm = new ResourceForm();
		formDialog.add(resourcesForm);
	}
	

}
