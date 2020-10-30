package com.demo.frontend.views.resources;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import shared.thesiscommon.bean.Reservation;
import shared.thesiscommon.bean.Resource;
import shared.thesiscommon.webservicesinterface.WebServicesInterface;

@Route(value = "resourcesView", layout = MainView.class)
@CssImport("./styles/views/resources/resources-view.css")
@PageTitle("Resources")
public class ResourcesView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	private WebServicesInterface clientService;

	private Grid<Resource> grid;
	private ResourceForm resourcesForm;
	private Dialog formDialog;
	private AppButton appButton;
	private List<Resource> resources;
	private ListDataProvider<Resource> resourceProvider;
	private String filterText = "";
	private CardsContainer cardsContainer;
	private HorizontalLayout container;

	private static final String ENABLE = "Enabled";
	private static final String DISABLE = "Disabled";
	private static final String ENABLE_COLOR = "#66ff66";
	private static final String DISABLE_COLOR = "#b30000";
	
	public ResourcesView() {
		setId("resource-view");
		setSizeFull();
		appButton = new AppButton();

		add(createTopBar());

		container = new HorizontalLayout();
		add(container);
		container.setSizeFull();

		cardsContainer = new CardsContainer();
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		setGrid();
		container.add(cardsContainer);
	}

	public void setGrid() {
		
		grid = new Grid<>();
		grid.setId("grid");
		grid.setSizeFull();
		resources = clientService.getAllResources();
		resourceProvider = DataProvider.ofCollection(resources);

		grid.setDataProvider(resourceProvider);

		grid.addColumn(Resource::getName).setHeader("Name").setSortable(true).setKey("Name").setFlexGrow(2);
		grid.addColumn(Resource::getDescription).setHeader("Description").setFlexGrow(5);
		grid.addColumn(Resource::getSeatsAvailable).setHeader("Seats Available").setSortable(true)
				.setKey("Seats Available").setFlexGrow(1);
		grid.addComponentColumn(this::relatedReservations).setHeader("Related Reservatios").setSortable(true)
				.setKey("Related Reservatios").setFlexGrow(1);

		if (CurrentUser.isAdmin()) {
			grid.addComponentColumn(this::enableResource).setHeader(ENABLE).setFlexGrow(1).setSortable(true).setKey("Status")
					.setComparator(Comparator.comparing(Resource::getEnable));
		}

		container.add(grid);
	}

	public Button enableResource(Resource res) {

		Icon banIcon = VaadinIcon.BAN.create();
		Button statusButton = appButton.set(ENABLE, banIcon);

		if (Boolean.TRUE.equals(res.getEnable()))
			setStatusButton(statusButton, ENABLE);
		else
			setStatusButton(statusButton, DISABLE);
		
		statusButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		statusButton.addClickListener(click -> {

			Set<Reservation> reservations = clientService.getReservationsByResource(res.getId());

			if (Boolean.TRUE.equals(res.getEnable())) {
				String text = "";
				if (!reservations.isEmpty()) {
					text = "This resource has " + reservations.size()
							+ " future reservations related DISABLE it?";
				}else {
					text = "DISABLE this resource? ";
				}
				setDisableDialog(res, statusButton, text);
			} else
				setEnableDialog(res, statusButton);
		});

		return statusButton;

	}

	public boolean setEnableDialog(Resource res, Button statusButton) {
		
		QuestionDialog enableDialog = new QuestionDialog("ENABLE this resource?", "ENABLE");
		
		enableDialog.getConfirmButton().addClickListener(ev -> {
			res.setEnable(true);
			HttpEntity<Resource> resource = new HttpEntity<>(res);
			clientService.updateResource(resource);
			setStatusButton(statusButton, ENABLE);
			enableDialog.close();
		});

		enableDialog.getCloseButton().addClickListener(ev -> enableDialog.close());
		return false;
	}

	public void setDisableDialog(Resource res, Button statusButton, String text) {

		QuestionDialog disableDialog = new QuestionDialog(text, "DISABLE");

		disableDialog.getConfirmButton().addClickListener(ev -> {
			res.setEnable(false);
			HttpEntity<Resource> resource = new HttpEntity<>(res);
			clientService.updateResource(resource);
			setStatusButton(statusButton, DISABLE);
			disableDialog.close();
		});

		disableDialog.getCloseButton().addClickListener(ev -> disableDialog.close());
	}
	
	public void setStatusButton(Button statusButton, String type) {
		switch (type) {
		case ENABLE:
			statusButton.setText(ENABLE);
			statusButton.getIcon().getElement().getStyle().set("color", ENABLE_COLOR);
			break;
		case DISABLE:
			statusButton.setText(DISABLE);
			statusButton.getIcon().getElement().getStyle().set("color", DISABLE_COLOR);
			break;
		default:
			break;
		}
	}

	public Button relatedReservations(Resource res) {

		Set<Reservation> reservations = clientService.getReservationsByResource(res.getId());
		int number = reservations.size();

		Button relatedButton = new Button(String.valueOf(number));
		relatedButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		relatedButton.addClickListener(click -> {
			cardsContainer.setCards(reservations, res.getName());
			
			cardsContainer.getRemoveAllButton().addClickListener(ev -> {			
				
				QuestionDialog removeAllDialog = new QuestionDialog("REMOVE ALL related reservations to " 
				+ "resource " + res.getName() + "?", "REMOVE");
				
				removeAllDialog.getConfirmButton().addClickListener(e -> {

					HttpEntity<Resource> resource = new HttpEntity<>(res);
					clientService.deleteRelatedReservations(resource);
					cardsContainer.cleanPanel(res.getName());
					grid.getDataProvider().refreshAll();
					removeAllDialog.close();
				});
				removeAllDialog.getCloseButton().addClickListener(e -> removeAllDialog.close());
			});
		});
		return relatedButton;
	}
	
	/* UTILS */

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
			Button newResourceButton = appButton.set("New Resource", VaadinIcon.PLUS_CIRCLE.create());
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
					Notification.show("Resource ADDED", 2000, Position.BOTTOM_START)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
