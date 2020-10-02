package com.demo.frontend.views.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import com.demo.frontend.views.main.MainView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "resourcesView", layout = MainView.class)
@PageTitle("Resources")
public class ResourcesView extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField filter;
	private Button newResourceButton;
	private final ResourcesGrid grid;
	private final BookResourceForm bookForm = new BookResourceForm();
	
	public ResourcesView() {

		setSizeFull();

		final HorizontalLayout topLayout = createTopBar();
		//resourceCard = new ResourceCard();
		
		LocalDate d1 = LocalDate.of(2020, 1, 31);
		ArrayList<Book> books = new ArrayList<Book>();
		books.add(new Book("Server A", "Marco Rossi", d1));
		books.add(new Book("Server B", "Danilo Bianchi", d1));

		grid = new ResourcesGrid();
		grid.setItems(books);
		//add(resourceCard);
		
		grid.setSelectionMode(SelectionMode.NONE);
		grid.addItemClickListener(event -> bookForm.showForm(true, event.getItem()));
		

		final VerticalLayout barAndGridLayout = new VerticalLayout();
		barAndGridLayout.add(topLayout);
		barAndGridLayout.add(grid);
		barAndGridLayout.setFlexGrow(1, grid);
		barAndGridLayout.setFlexGrow(0, topLayout);
		barAndGridLayout.setSizeFull();
		barAndGridLayout.expand(grid);

		add(barAndGridLayout);
		add(bookForm);
	}

	public HorizontalLayout createTopBar() {
		filter = new TextField();
		filter.setPlaceholder("Filter name, availability or ..");
		// Apply the filter to grid's data provider. TextField value is never
		/*
		 * filter.addValueChangeListener( event ->
		 * dataProvider.setFilter(event.getValue()));
		 */
		// A shortcut to focus on the textField by pressing ctrl + F
		filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

		newResourceButton = new Button("New resource");
		// Setting theme variant of new production button to LUMO_PRIMARY that
		// changes its background color to blue and its text color to white
		newResourceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		newResourceButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
		// newResourceButton.addClickListener(click -> viewLogic.newProduct());
		// A shortcut to click the new product button by pressing ALT + N
		newResourceButton.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
		final HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setWidth("50%");
		topLayout.add(filter);
		topLayout.add(newResourceButton);
		topLayout.setVerticalComponentAlignment(Alignment.START, filter);
		topLayout.expand(filter);
		return topLayout;
	}

	public void showNotification(String msg) {
		Notification.show(msg);
	}
	

}
