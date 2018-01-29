package hu.unideb.inf.carrental.ui.car.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.ui.commons.content.CarRentalContent;
import org.tepi.imageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public class CarContent extends CarRentalContent {
    public CarContent(CarResponse carResponse, List<CarImageResponse> carImageResponses) {
        super(String.format("Car - %s %s", carResponse.getBrand(), carResponse.getModel()));
        this.carResponse = carResponse;
        this.carImageResponses = carImageResponses;

        setupHeader();
        setupBody();
    }

    private void setupHeader() {
        final HorizontalLayout container = new HorizontalLayout();
        container.setMargin(false);
        container.setSpacing(true);
        container.setSizeUndefined();

        final Button viewSite = new Button("View site");
        viewSite.setSizeUndefined();
        viewSite.setIcon(VaadinIcons.LOCATION_ARROW);

        final Button rentNow = new Button("Rent now!");
        rentNow.setSizeUndefined();
        rentNow.addStyleName(ValoTheme.BUTTON_PRIMARY);
        rentNow.setIcon(VaadinIcons.CAR);

        container.addComponents(
                viewSite,
                rentNow
        );

        getHeader().addComponent(container);
        getHeader().setComponentAlignment(container, Alignment.MIDDLE_RIGHT);
    }

    private void setupBody() {
        getBody().addComponents(
                buildDetails(),
                buildImageViewer()
        );
    }

    private GridLayout buildDetails() {
        final GridLayout details = new GridLayout(2, 8);
        details.setMargin(false);
        details.setSpacing(false);
        details.setWidthUndefined();
        details.setHeightUndefined();

        final Label keyCategory = buildKeyLabel("Category:");
        final Label keyBrand = buildKeyLabel("Brand:");
        final Label keyModel = buildKeyLabel("Model:");
        final Label keyYear = buildKeyLabel("Year:");
        final Label keySeatNumber = buildKeyLabel("Seat number:");
        final Label keyTrunkCapacity = buildKeyLabel("Trunk capacity:");
        final Label keyFuelType = buildKeyLabel("Fuel type:");
        final Label keyFuelConsumption = buildKeyLabel("Fuel Consumption:");
        final Label keyTankCapacity = buildKeyLabel("Tank capacity:");

        final Label valueCategory = buildValueLabel(carResponse.getCategory().toString());
        final Label valueBrand = buildValueLabel(carResponse.getBrand());
        final Label valueModel = buildValueLabel(carResponse.getModel());
        final Label valueYear = buildValueLabel(carResponse.getYear().toString());
        final Label valueSeatNumber = buildValueLabel(carResponse.getSeatNumber().toString());
        final Label valueTrunkCapacity = buildValueLabel(carResponse.getTrunkCapacity().toString() + " liter");
        final Label valueFuelType = buildValueLabel(carResponse.getFuelType().toString());
        final Label valueFuelConsumption = buildValueLabel(carResponse.getFuelConsumption().toString() + " liter/100km");
        final Label valueTankCapacity = buildValueLabel(carResponse.getTankCapacity().toString() + " liter");

        details.addComponents(
                keyCategory, valueCategory,
                keyBrand, valueBrand,
                keyModel, valueModel,
                keyYear, valueYear,
                keySeatNumber, valueSeatNumber,
                keyTrunkCapacity, valueTrunkCapacity,
                keyFuelType, valueFuelType,
                keyFuelConsumption, valueFuelConsumption,
                keyTankCapacity, valueTankCapacity
        );
        return details;
    }

    private VerticalLayout buildImageViewer() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        final ImageViewer imageViewer = new ImageViewer();
        imageViewer.setWidth(600.f, Unit.PIXELS);
        imageViewer.setHeight(600.f, Unit.PIXELS);
        imageViewer.setAnimationEnabled(false);
        imageViewer.setSideImageRelativeWidth(0.7f);

        List<Resource> resources = new ArrayList<>();
        resources.add(new ThemeResource("img/home-background.jpeg"));
        resources.add(new ThemeResource("img/login-background.jpeg"));
        imageViewer.setImages(resources);
        imageViewer.setHiLiteEnabled(true);
        layout.addComponent(imageViewer);
        return layout;
    }

    private Label buildKeyLabel(String text) {
        final Label label = new Label(bold(text), ContentMode.HTML);
        label.setId("key");
        return label;
    }

    private Label buildValueLabel(String text) {
        final Label label = new Label(text);
        label.setId("value");
        return label;
    }

    @Override
    protected AbstractLayout buildBody() {
        return buildDefaultHorizontalBody();
    }

    private final CarResponse carResponse;
    private final List<CarImageResponse> carImageResponses;
}
