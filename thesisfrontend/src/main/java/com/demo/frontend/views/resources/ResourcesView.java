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
	private QuestionDialog disableDialog;
	private List<Resource> resources;
	private ListDataProvider<Resource> resourceProvider;
	private String filterText = "";
	private CardsContainer cardsContainer;
	private HorizontalLayout container;

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

		grid.addColumn(Resource::getName).setHeader("Name").setSortable(true).setKey("Name");
		grid.addColumn(Resource::getDescription).setHeader("Description").setFlexGrow(5);
		grid.addColumn(Resource::getSeatsAvailable).setHeader("Seats Available").setSortable(true)
				.setKey("Seats Available");
		grid.addComponentColumn(this::relatedReservations).setHeader("Related Reservatios").setSortable(true)
				.setKey("Related Reservatios");
		grid.addComponentColumn(this::currentStatus).setHeader("Enabled").setSortable(true).setKey("Status")
		.setComparator(Comparator.comparing(Resource::getEnable));
		
		if (CurrentUser.isAdmin()) {
			grid.addComponentColumn(this::enableButton);
		}
	
		container.add(grid);
	}

	public Button enableButton(Resource res) {

		Icon banIcon = VaadinIcon.BAN.create();
		Button statusButton = appButton.set("", banIcon);
		statusButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		if (CurrentUser.isAdmin()) {
			statusButton.addClickListener(click -> {

				Set<Reservation> reservations = clientService.getReservationsByResource(res.getId());

				if (Boolean.TRUE.equals(res.getEnable())) {
					
					if (!reservations.isEmpty()) { 
						String text = "This resource will be disabled. " + "It has " + reservations.size()
								+ " future reservations. " + "Do you want to delete also them?";
						disableDialog = new QuestionDialog(text, "DISABLE");

						/* confirm */
						disableDialog.getConfirmButton().addClickListener(ev -> {
							HttpEntity<Resource> resource = new HttpEntity<>(res);
							clientService.deleteRelatedReservations(resource);

							res.setEnable(false);
							HttpEntity<Resource> r = new HttpEntity<>(res);
							clientService.updateResource(r);
							disableDialog.close();
						});

						/* close */
						disableDialog.getCloseButton().addClickListener(ev -> {
							res.setEnable(false);
							HttpEntity<Resource> r = new HttpEntity<>(res);
							clientService.updateResource(r);
							disableDialog.close();
						});

					} else
						setDisableDialog(res); /* with no related reservation */
				} else
					setEnableDialog(res);
			});
		} else {
			statusButton.setEnabled(false);
		}
		return statusButton;
	}

	public void setEnableDialog(Resource res) {
		String text = "Do you want to enable this resource? ";
		QuestionDialog enableDialog = new QuestionDialog(text, "ENABLE");

		/* confirm */
		enableDialog.getConfirmButton().addClickListener(ev -> {
			res.setEnable(true);
			HttpEntity<Resource> resource = new HttpEntity<>(res);
			clientService.updateResource(resource);
			enableDialog.close();
		});

		/* close */
		enableDialog.getCloseButton().addClickListener(ev -> enableDialog.close());
	}

	public void setDisableDialog(Resource res) {
		/* with no related reservation */

		String text = "Do you want to disable this resource? ";
		disableDialog = new QuestionDialog(text, "DISABLE");

		/* confirm */
		disableDialog.getConfirmButton().addClickListener(ev -> {
			res.setEnable(false);
			HttpEntity<Resource> resource = new HttpEntity<>(res);
			clientService.updateResource(resource);
			disableDialog.close();
		});

		/* close */
		disableDialog.getCloseButton().addClickListener(ev -> disableDialog.close());
	}


	public Button relatedReservations(Resource res) {

		Set<Reservation> reservations = clientService.getReservationsByResource(res.getId());
		int number = reservations.size();
		
		Button relatedButton = new Button(String.valueOf(number));
		relatedButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		relatedButton.addClickListener(click -> cardsContainer.setCards(reservations, res.getName()) );
		
		return relatedButton;
	}

	public Icon currentStatus(Resource res) {
		Icon circle = VaadinIcon.CIRCLE.create();
		circle.getElement().getStyle().set("width", "17px");
		circle.getElement().getStyle().set("height", "17px");
		if(Boolean.TRUE.equals(res.getEnable()))
			circle.setColor("#99ff66");
		else
			circle.setColor("#ff0000");
		
		return circle;
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
