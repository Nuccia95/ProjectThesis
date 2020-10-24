package com.demo.frontend.views.resources;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
	private String filterText = "";

	public ResourcesView() {
		setId("resource-view");
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
		grid.setId("grid");
		grid.setSizeFull();
		resources = clientService.getAllResources();
		resourceProvider = DataProvider.ofCollection(resources);

		grid.setDataProvider(resourceProvider);

		grid.addColumn(Resource::getName).setHeader("Name").setSortable(true).setKey("Name");
		grid.addColumn(Resource::getDescription).setHeader("Description");
		grid.addColumn(Resource::getSeatsAvailable).setHeader("Seats Available");
		grid.addComponentColumn(this::relatedReservation).setHeader("Related Reservation");

		if (CurrentUser.isAdmin())
			grid.addComponentColumn(this::trashIcon);

		add(grid);
	}

	public Span relatedReservation(Resource res) {
		int number = clientService.getReservationsByResource(res.getId());

		Span span = new Span();
		span.setId("numberRes");
		span.add(String.valueOf(number));

		if (number > 0)
			span.getElement().getStyle().set("background-color", "#00cc66");

		return span;
	}

	public Div trashIcon(Resource res) {
		Div buttonsContainer = new Div();
		buttonsContainer.setId("container-icons");

		Button trashButt = appButton.set("", VaadinIcon.TRASH.create());
		trashButt.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		trashButt.addClickListener(click -> {

			int number = clientService.getReservationsByResource(res.getId());
			String text;
			if (number > 0) {
				text = "This resource cannot be deleted, there are " + number + " of reservations " + "releted to it";
				deleteResourceDialog = new QuestionDialog(text, "REMOVE");
				deleteResourceDialog.getConfirmButton().addClickListener(ev -> deleteResourceDialog.close());
			} else {
				text = "Do you want to delete this resource?";
				deleteResourceDialog = new QuestionDialog(text, "REMOVE");
				deleteResourceDialog.getConfirmButton().addClickListener(ev -> {
					resources.remove(res);
					HttpEntity<Resource> resource = new HttpEntity<>(res);
					clientService.deleteResource(resource);
					grid.getDataProvider().refreshAll();
					deleteResourceDialog.close();
					Notification.show("Resource deleted", 2000, Position.BOTTOM_START);
				});
			}
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
		filter.setClearButtonVisible(true);
		filter.addValueChangeListener(event -> setFilter(event.getValue()));

		topLayout.setVerticalComponentAlignment(Alignment.START, filter);
		topLayout.expand(filter);

		topLayout.add(filter);

		if (CurrentUser.isAdmin()) {
			Button newResourceButton = appButton.set("Add", VaadinIcon.PLUS_CIRCLE.create());
			newResourceButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
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
					Notification.show("Resource added", 2000, Position.BOTTOM_START);
					formDialog.close();
				});
				resourcesForm.getCancelButton().addClickListener(e -> formDialog.close());
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

	public void setFilter(String filterText) {
		Objects.requireNonNull(filterText, "Filter text cannot be null.");
		if (Objects.equals(this.filterText, filterText.trim())) {
			return;
		}
		this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

		resourceProvider.setFilter(resource -> passesFilter(resource.getName(), this.filterText)
				|| passesFilter(resource.getSeatsAvailable(), this.filterText)
				|| passesFilter(resource.getDescription(), this.filterText));
	}

	private boolean passesFilter(Object object, String filterText) {
		return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
	}

}
