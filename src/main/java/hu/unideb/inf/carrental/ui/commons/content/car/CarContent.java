package hu.unideb.inf.carrental.ui.commons.content.car;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.commons.content.root.CarRentalContent;
import org.tepi.imageviewer.ImageViewer;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public abstract class CarContent extends CarRentalContent {
    public CarContent(CarResponse carResponse, List<CarImageResponse> carImageResponses) {
        super(String.format("Car - %s %s", carResponse.getBrand(), carResponse.getModel()));
        this.carResponse = carResponse;
        this.carImageResponses = carImageResponses;

        setupBody();
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
        details.setSizeUndefined();

        final Label keyCategory = buildKeyLabel("Category:");
        final Label keyBrand = buildKeyLabel("Brand:");
        final Label keyModel = buildKeyLabel("Model:");
        final Label keyYear = buildKeyLabel("Year:");
        final Label keySeatNumber = buildKeyLabel("Seat number:");
        final Label keyTrunkCapacity = buildKeyLabel("Trunk capacity:");
        final Label keyFuelType = buildKeyLabel("Fuel type:");
        final Label keyFuelConsumption = buildKeyLabel("Fuel Consumption:");
        final Label keyTankCapacity = buildKeyLabel("Tank capacity:");
        final Label keyPrice = buildKeyLabel("Price:");

        final Label valueCategory = buildValueLabel(carResponse.getCategory().toString());
        final Label valueBrand = buildValueLabel(carResponse.getBrand());
        final Label valueModel = buildValueLabel(carResponse.getModel());
        final Label valueYear = buildValueLabel(carResponse.getYear().toString());
        final Label valueSeatNumber = buildValueLabel(carResponse.getSeatNumber().toString());
        final Label valueTrunkCapacity = buildValueLabel(carResponse.getTrunkCapacity().toString() + " liter");
        final Label valueFuelType = buildValueLabel(carResponse.getFuelType().toString());
        final Label valueFuelConsumption = buildValueLabel(carResponse.getFuelConsumption().toString() + " liter/100km");
        final Label valueTankCapacity = buildValueLabel(carResponse.getTankCapacity().toString() + " liter");
        final Label valuePrice = buildValueLabel(String.format("%s %s%s", carResponse.getPrice().toString(), Constants.CURRENCY, "/day"));

        details.addComponents(
                keyCategory, valueCategory,
                keyBrand, valueBrand,
                keyModel, valueModel,
                keyYear, valueYear,
                keySeatNumber, valueSeatNumber,
                keyTrunkCapacity, valueTrunkCapacity,
                keyFuelType, valueFuelType,
                keyFuelConsumption, valueFuelConsumption,
                keyTankCapacity, valueTankCapacity,
                keyPrice, valuePrice
        );
        return details;
    }

    private VerticalLayout buildImageViewer() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setSizeFull();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layout.setId("image-container");

        final ImageViewer imageViewer = new ImageViewer();
        imageViewer.setSizeFull();
        imageViewer.setAnimationEnabled(false);
        imageViewer.setSideImageRelativeWidth(0.7f);
        imageViewer.setHeight(358.f, Unit.PIXELS);

        //TODO file name must be added!
        List<Resource> resources = carImageResponses.stream()
                .map(CarImageResponse::getData)
                .map(e -> Base64.getDecoder().decode(e))
                .map(e -> new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(e), String.valueOf(e.length)))
                .collect(Collectors.toList());

        imageViewer.setImages(resources);

        layout.addComponent(imageViewer);
        layout.setExpandRatio(imageViewer, 0.8f);
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

    protected CarResponse getCarResponse() {
        return carResponse;
    }

    @Override
    protected AbstractLayout buildBody() {
        return buildDefaultHorizontalBody();
    }

    private final CarResponse carResponse;
    private final List<CarImageResponse> carImageResponses;
}
