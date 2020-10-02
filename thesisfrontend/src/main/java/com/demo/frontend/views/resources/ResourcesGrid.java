package com.demo.frontend.views.resources;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

public class ResourcesGrid extends Grid<Book> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourcesGrid() { 
		
		setSizeFull();
		
        addColumn(Book::getName).setHeader("Name").setSortable(true).setKey("Name");
        addColumn(Book::getUser).setHeader("User");
		addColumn(Book::getDate).setHeader("Date");
        
        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css
        // and are
        // used here in availabilityTemplate.
        //final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availability]]\"></iron-icon> [[item.availability]]";
        
        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
       /* UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));*/
    }

   /* private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(true);
            getColumnByKey("stock").setVisible(true);
            getColumnByKey("category").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(false);
            getColumnByKey("stock").setVisible(false);
            getColumnByKey("category").setVisible(true);
        } else {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
            getColumnByKey("availability").setVisible(false);
            getColumnByKey("stock").setVisible(false);
            getColumnByKey("category").setVisible(false);
        }
    }
 */   

    public Book getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Book book) {
        getDataCommunicator().refresh(book);
    }
}
