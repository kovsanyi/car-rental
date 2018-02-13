package hu.unideb.inf.carrental.ui.car.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.commons.content.car.CarContent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;

import java.util.List;

public class CustomerCarContent extends CarContent {

    public CustomerCarContent(CarResponse carResponse, List<CarImageResponse> carImageResponses) {
        super(carResponse, carImageResponses);

        setupHeader();
    }

    private void setupHeader() {
        final HorizontalLayout container = new HorizontalLayout();
        container.setMargin(false);
        container.setSpacing(true);
        container.setSizeUndefined();

        final Button viewSite = new Button("View site");
        viewSite.setSizeUndefined();
        viewSite.setIcon(VaadinIcons.LOCATION_ARROW);
        viewSite.addClickListener(e -> CarRentalUI.getCurrent().getPage()
                .open("/#!site/id=" + getCarResponse().getSiteId(), ""));

        final Button rentNow = new Button("Rent now!");
        rentNow.setSizeUndefined();
        rentNow.addStyleName(ValoTheme.BUTTON_PRIMARY);
        rentNow.setIcon(VaadinIcons.CAR);
        rentNow.addClickListener(e ->
                CarRentalEventBus.post(new CarRentalEvent.OpenReservationWindow(getCarResponse().getId())));

        container.addComponents(
                viewSite,
                rentNow
        );

        getHeader().addComponent(container);
        getHeader().setComponentAlignment(container, Alignment.MIDDLE_RIGHT);
    }
}
