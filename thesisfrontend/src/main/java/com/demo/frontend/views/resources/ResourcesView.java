package com.demo.frontend.views.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import com.demo.frontend.utils.AppButton;
import com.demo.frontend.utils.QuestionDialog;
import com.demo.frontend.view.login.CurrentUser;
import com.demo.frontend.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

@Route(value = "resourcesView", layout = MainView.class)
@CssImport("./styles/views/resources/resources-view.css")
@PageTitle("Resources")
public class ResourcesView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private WebServicesInterface clientService;

	private Grid<Resource> grid;
	private ResourceForm resourcesForm;
	private Dialog formDialog;
	private AppButton appButton;
	private QuestionDialog deleteResourceDialog;
	private List<Resource> resources;
	private ListDataProvider<Resource> resourceProvider;

	public ResourcesView() {
		setSizeFull();
		appButton = new AppButton();
		add(createTopBar());
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		setGrid();
	}

	public void setGrid() {
		grid = new Grid<>();
		grid.setSizeFull();

		resources = clientService.getAllResources();
		resourceProvider = DataProvider.ofCollection(resources);

		grid.setDataProvider(resourceProvider);

		grid.addColumn(Resource::getName).setHeader("Name").setSortable(true).setKey("Name");
		grid.addColumn(Resource::getDescription).setHeader("Description");
		grid.addColumn(Resource::getSeatsAvailable).setHeader("Seats Available");

		if (CurrentUser.isAdmin())
			grid.addComponentColumn(this::trashIcon);

		add(grid);
	}

	public Div trashIcon(Resource res) {
		Div buttonsContainer = new Div();
		buttonsContainer.setId("container-icons");

		Button trashButt = appButton.set("", VaadinIcon.TRASH.create());

		trashButt.addClickListener(click -> {
			deleteResourceDialog = new QuestionDialog("Do you want to delete this resource?", VaadinIcon.CHECK.create(),
					VaadinIcon.CLOSE.create(), "REMOVE");

			/* Delete resource */
			deleteResourceDialog.getConfirmButton().addClickListener(ev -> {
				resources.remove(res);
				HttpEntity<Resource> resource = new HttpEntity<>(res);
				clientService.deleteResource(resource);
				grid.getDataProvider().refreshAll();
				deleteResourceDialog.close();
			});

			/* Close dialog */
			deleteResourceDialog.getCancelButton().addClickListener(ev -> {
				deleteResourceDialog.close();
			});
		});

		buttonsContainer.add(trashButt);
		return buttonsContainer;
	}

	public HorizontalLayout createTopBar() {

		final HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setWidth("50%");

		TextField filter = new TextField();
		filter.setPlaceholder("Filter resources by name, seats available ..");
		filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

		filter.setValueChangeMode(ValueChangeMode.EAGER);

		topLayout.setVerticalComponentAlignment(Alignment.START, filter);
		topLayout.expand(filter);

		topLayout.add(filter);

		if (CurrentUser.isAdmin()) {
			Button newResourceButton = appButton.set("Add", VaadinIcon.PLUS_CIRCLE.create());
			newResourceButton.addClickListener(ev -> {
				setForm();
				formDialog.open();
				/* create resource */
				resourcesForm.getSaveButton().addClickListener(e -> {
					Resource newRes = resourcesForm.getData();

					HttpEntity<Resource> resource = new HttpEntity<>(newRes);
					Resource r = clientService.createResource(resource);
					resources.add(r);
					grid.getDataProvider().refreshAll();
					formDialog.close();
				});

				resourcesForm.getCancelButton().addClickListener(e -> {
					formDialog.close();
				});
			});
			topLayout.add(newResourceButton);
		}
		return topLayout;
	}

	public void setForm() {
		formDialog = new Dialog();
		resourcesForm = new ResourceForm();
		formDialog.add(resourcesForm);
	}

}
