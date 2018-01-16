package hu.unideb.inf.carrental.ui.component;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;

public final class CarItem extends Panel {

    public CarItem(CarResponse carResponse) {
        this.carResponse = carResponse;

        final HorizontalLayout root = new HorizontalLayout();
        root.setSizeUndefined();

        root.addComponent(buildCoverImage());
        root.addComponent(buildDetails());
        final Component rentInfo = buildRentInfo();
        root.addComponent(rentInfo);

        root.setComponentAlignment(rentInfo, Alignment.MIDDLE_RIGHT);

        setSizeUndefined();
        setContent(root);
        setCaption(carResponse.getBrand() + " " + carResponse.getModel());
    }

    private Component buildCoverImage() {
        final Image cover = new Image();
        //TODO resource for debugging
        cover.setSource(new ExternalResource(""));
        cover.setWidth(256, Unit.PIXELS);

        return cover;
    }

    private Component buildDetails() {
        final VerticalLayout details = new VerticalLayout();
        details.setMargin(false);
        details.setSpacing(false);

        final Label name = new Label(carResponse.getBrand() + " " + carResponse.getModel());
        name.setStyleName(ValoTheme.LABEL_H2);

        //TODO initial details!
        final Label year = new Label("Year: " + carResponse.getYear());
        final Label seatNumber = new Label("Seat number: " + carResponse.getSeatNumber());
        final Label trunkCapacity = new Label("Trunk capacity: " + carResponse.getTrunkCapacity());
        final Label fuelConsumption = new Label("Fuel Consumption: " + carResponse.getFuelConsumption());

        details.addComponents(name, year, seatNumber, trunkCapacity, fuelConsumption);

        return details;
    }

    private Component buildRentInfo() {
        final VerticalLayout payInfo = new VerticalLayout();
        payInfo.setSpacing(false);

        final Label price = new Label(carResponse.getPrice() + " HUF/day");
        price.setStyleName(ValoTheme.LABEL_H3);

        final Button next = new Button("Continue");
        next.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        next.addClickListener(e -> {
            //TODO code it
        });

        payInfo.addComponents(price, next);
        payInfo.setComponentAlignment(price, Alignment.MIDDLE_CENTER);
        payInfo.setComponentAlignment(next, Alignment.MIDDLE_CENTER);

        return payInfo;
    }

    private final CarResponse carResponse;
}
