package hu.unideb.inf.carrental.ui.component.item;

import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.car.CarView;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Objects;

//TODO what if cover image not available
public final class CarItem extends Panel {

    public CarItem(CarResponse carResponse, CarImageResponse carImageResponse) {
        this.carResponse = carResponse;
        this.carImageResponse = carImageResponse;

        setWidthUndefined();
        setHeightUndefined();
        setCaption(bold(String.format("%s %s", carResponse.getBrand(), carResponse.getModel())));
        setCaptionAsHtml(true);

        setContent(buildContent());
    }

    private AbstractLayout buildContent() {
        final HorizontalLayout content = new HorizontalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();

        content.addComponents(
                buildCoverImage(),
                buildDetails()
        );

        Component rentInfo = buildRentInfo();
        content.addComponent(rentInfo);
        content.setComponentAlignment(rentInfo, Alignment.MIDDLE_CENTER);

        return content;
    }

    private Component buildCoverImage() {
        final VerticalLayout imageContainer = new VerticalLayout();
        imageContainer.setMargin(false);
        imageContainer.setSpacing(false);
        imageContainer.setWidth(260.f, Unit.PIXELS);
        imageContainer.setHeight(160.f, Unit.PIXELS);
        imageContainer.addStyleName("color: red;");

        if (!Objects.isNull(carImageResponse)) {
            final Image cover = new Image();
            cover.setSource(new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(Base64.getDecoder().decode(carImageResponse.getData())), ""));
            cover.setWidth(100.f, Unit.PERCENTAGE);
            cover.setHeightUndefined();

            imageContainer.addComponent(cover);
        }

        return imageContainer;
    }

    private AbstractLayout buildDetails() {
        final VerticalLayout root = new VerticalLayout();
        root.setMargin(false);
        root.setSpacing(true);
        root.setWidth(270.f, Unit.PIXELS);
        root.setHeightUndefined();

        final Label brandModel = new Label(carResponse.getBrand() + " " + carResponse.getModel());
        brandModel.addStyleName(ValoTheme.LABEL_H1);
        brandModel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        root.addComponent(brandModel);

        final HorizontalLayout containerOfKeyValueLayout = new HorizontalLayout();
        containerOfKeyValueLayout.setMargin(false);
        containerOfKeyValueLayout.setSpacing(true);

        final VerticalLayout keyLayout = new VerticalLayout();
        keyLayout.setMargin(false);
        keyLayout.setSpacing(false);

        final VerticalLayout valueLayout = new VerticalLayout();
        valueLayout.setMargin(false);
        valueLayout.setSpacing(false);

        final Label keyYear = new Label(bold("Year:"), ContentMode.HTML);
        final Label keySeatNumber = new Label(bold("Seat number:"), ContentMode.HTML);
        final Label keyTrunkCapacity = new Label(bold("Trunk capacity:"), ContentMode.HTML);
        final Label keyFuelType = new Label(bold("Fuel type:"), ContentMode.HTML);
        final Label keyFuelConsumption = new Label(bold("Fuel Consumption:"), ContentMode.HTML);

        final Label valueYear = new Label(carResponse.getYear().toString());
        final Label valueSeatNumber = new Label(carResponse.getSeatNumber().toString());
        final Label valueTrunkCapacity = new Label(carResponse.getTrunkCapacity().toString() + "l");
        valueTrunkCapacity.setDescription(carResponse.getTrunkCapacity().toString() + " liter");
        final Label valueFuelType = new Label(carResponse.getFuelType().toString());
        final Label valueFuelConsumption = new Label(carResponse.getFuelConsumption().toString() + "l/100km");
        valueFuelConsumption.setDescription(carResponse.getFuelConsumption().toString() + " liter/100km");

        keyLayout.addComponents(keyYear, keySeatNumber, keyTrunkCapacity, keyFuelType, keyFuelConsumption);
        valueLayout.addComponents(valueYear, valueSeatNumber, valueTrunkCapacity, valueFuelType, valueFuelConsumption);

        containerOfKeyValueLayout.addComponents(keyLayout, valueLayout);
        root.addComponents(containerOfKeyValueLayout);
        return root;
    }

    private AbstractLayout buildRentInfo() {
        final VerticalLayout paymentInformation = new VerticalLayout();
        paymentInformation.setMargin(false);
        paymentInformation.setSpacing(true);
        paymentInformation.setWidth(160.f, Unit.PIXELS);
        paymentInformation.setHeightUndefined();
        paymentInformation.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        final HorizontalLayout priceLayout = new HorizontalLayout();
        priceLayout.setMargin(false);
        priceLayout.setSpacing(false);

        final Label valuePrice = new Label(carResponse.getPrice() + " HUF");
        valuePrice.addStyleName(ValoTheme.LABEL_H2);
        valuePrice.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        final Label dayText = new Label("/day");
        priceLayout.addComponents(
                valuePrice,
                dayText
        );
        priceLayout.setComponentAlignment(dayText, Alignment.BOTTOM_CENTER);

        final Button next = new Button("Continue");
        next.setWidth(100.f, Unit.PERCENTAGE);
        next.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        next.addClickListener(e -> {
            CarRentalUI.getCurrent().getNavigator().navigateTo(CarView.VIEW_NAME);
        });

        paymentInformation.addComponents(priceLayout, next);
        paymentInformation.setComponentAlignment(priceLayout, Alignment.MIDDLE_CENTER);
        paymentInformation.setComponentAlignment(next, Alignment.MIDDLE_CENTER);

        return paymentInformation;
    }

    private String bold(String text) {
        return String.format("<b> %s </b>", text);
    }

    private final CarResponse carResponse;
    private final CarImageResponse carImageResponse;
}
