package hu.unideb.inf.carrental.ui.commons.component.item;

import com.vaadin.icons.VaadinIcons;
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

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public final class CarItem extends Panel {
    public CarItem(CarResponse carResponse, CarImageResponse carImageResponse) {
        this.carResponse = carResponse;
        this.carImageResponse = carImageResponse;

        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setCaption(carResponse.getBrand() + " " + carResponse.getModel());
        addStyleName("caritem");

        setContent(buildContent());
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeFull();

        final HorizontalLayout containerOfPictureAndDetails = new HorizontalLayout();
        containerOfPictureAndDetails.setMargin(false);
        containerOfPictureAndDetails.setSpacing(true);
        containerOfPictureAndDetails.setWidth(100.f, Unit.PERCENTAGE);

        containerOfPictureAndDetails.addComponents(
                buildCoverImage(),
                buildDetails()
        );

        content.addComponent(containerOfPictureAndDetails);
        return content;
    }

    private Component buildCoverImage() {
        final VerticalLayout imageContainer = new VerticalLayout();
        imageContainer.setMargin(false);
        imageContainer.setSpacing(false);
        imageContainer.setSizeFull();
        imageContainer.setId("imagecontainer");

        if (!Objects.isNull(carImageResponse)) {
            final Image image = new Image();
            image.setSource(
                    new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(Base64.getDecoder().decode(carImageResponse.getData())), ""));
            image.setWidth(100.f, Unit.PERCENTAGE);
            image.setHeightUndefined();

            imageContainer.addComponent(image);
        }

        return imageContainer;
    }

    private VerticalLayout buildDetails() {
        final VerticalLayout root = new VerticalLayout();
        root.setMargin(false);
        root.setSpacing(true);
        root.setWidth(100.f, Unit.PERCENTAGE);
        root.setHeightUndefined();

        final Label title = new Label(carResponse.getBrand() + " " + carResponse.getModel());
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        final GridLayout details = new GridLayout(2, 4);
        details.setMargin(false);
        details.setSpacing(false);
        details.setWidthUndefined();
        details.setHeightUndefined();

        final Label keyYear = buildKeyLabel("Year:");
        final Label keySeatNumber = buildKeyLabel("Seat number:");
        final Label keyTrunkCapacity = buildKeyLabel("Trunk capacity:");

        final Label valueYear = buildValueLabel(carResponse.getYear().toString());
        final Label valueSeatNumber = buildValueLabel(carResponse.getSeatNumber().toString());
        final Label valueTrunkCapacity = buildValueLabel(carResponse.getTrunkCapacity().toString() + " liter");

        details.addComponents(
                keyYear, valueYear,
                keySeatNumber, valueSeatNumber,
                keyTrunkCapacity, valueTrunkCapacity
        );

        final HorizontalLayout containerOfPriceAndContinueButton = new HorizontalLayout();
        containerOfPriceAndContinueButton.setMargin(false);
        containerOfPriceAndContinueButton.setSpacing(true);
        containerOfPriceAndContinueButton.setWidth(100.f, Unit.PERCENTAGE);
        containerOfPriceAndContinueButton.setHeightUndefined();
        containerOfPriceAndContinueButton.addComponents(
                buildPriceContent(),
                buildContinueButton()
        );

        root.addComponents(
                title,
                details,
                containerOfPriceAndContinueButton
        );
        return root;
    }

    private HorizontalLayout buildPriceContent() {
        final HorizontalLayout priceLayout = new HorizontalLayout();
        priceLayout.setMargin(false);
        priceLayout.setSpacing(false);
        priceLayout.setWidth(100.f, Unit.PERCENTAGE);
        priceLayout.setHeight(100.f, Unit.PERCENTAGE);
        priceLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        priceLayout.setId("pricecontent");

        final Label price = new Label(
                carResponse.getPrice().toString() + " HUF<sub>/day</sub>",
                ContentMode.HTML);
        price.addStyleName(ValoTheme.LABEL_H3);
        price.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        priceLayout.addComponent(price);
        return priceLayout;
    }

    private Button buildContinueButton() {
        final Button button = new Button("Continue");
        button.setWidth(100.f, Unit.PERCENTAGE);
        button.setHeightUndefined();
        button.setIcon(VaadinIcons.CAR);
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        button.addClickListener(e ->
                CarRentalUI.getCurrent().getPage().open("/#!" + CarView.VIEW_NAME + "/id=" + carResponse.getId(), "")
        );
        return button;
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

    private final CarResponse carResponse;
    private final CarImageResponse carImageResponse;
}
