package hu.unideb.inf.carrental.ui.company.component;

import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.CarRentalUI;

import java.util.HashMap;
import java.util.Map;

//TODO fuel cons. validate
@UIScope
public class CarWindow extends Window {

    public CarWindow(SiteService siteService, CarService carService) {
        this.siteService = siteService;
        this.carService = carService;

        siteWithID = new HashMap<>();
        componentWithValidation = new HashMap<>();

        siteService.getByCompany().forEach(e -> siteWithID.put(e.getCity() + " - " + e.getAddress(), e.getId()));

        setCaption("Add a car");
        setSizeUndefined();
        setModal(true);
        setResizable(false);
        setDraggable(false);
        center();
        setContent(buildContent());
    }

    private AbstractLayout buildContent() {
        final GridLayout content = new GridLayout(3, 4);
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();

        content.addComponent(buildSite(), 0, 0, 1, 0);
        content.addComponent(buildCategory());

        final HorizontalLayout containerOfYearAndSeatNumber = new HorizontalLayout();
        containerOfYearAndSeatNumber.setWidth(category.getWidth(), Unit.PIXELS);
        containerOfYearAndSeatNumber.setMargin(false);
        containerOfYearAndSeatNumber.addComponents(
                buildYear(),
                buildSeatNumber());

        content.addComponents(
                buildBrand(),
                buildModel(),
                containerOfYearAndSeatNumber);

        content.addComponents(
                buildFuelType(),
                buildFuelConsumption());

        final HorizontalLayout containerOfFuelConsAndTrunk = new HorizontalLayout();
        containerOfFuelConsAndTrunk.setWidth(category.getWidth(), Unit.PIXELS);
        containerOfFuelConsAndTrunk.setMargin(false);
        containerOfFuelConsAndTrunk.addComponents(
                buildTankCapacity(),
                buildTrunkCapacity());

        content.addComponent(containerOfFuelConsAndTrunk);
        content.addComponent(buildPrice());

        final HorizontalLayout containerOfCreateAndCloseButton = new HorizontalLayout();
        containerOfCreateAndCloseButton.setWidth(100.0f, Unit.PERCENTAGE);
        containerOfCreateAndCloseButton.setMargin(false);
        containerOfCreateAndCloseButton.addComponents(buildCreateButton(), buildCloseButton());

        content.addComponent(containerOfCreateAndCloseButton, 2, 3);
        content.setComponentAlignment(containerOfCreateAndCloseButton, Alignment.BOTTOM_CENTER);

        return content;
    }

    private Component buildSite() {
        site = new NativeSelect<>("Site", siteWithID.keySet());
        site.setWidth(300.f, Unit.PIXELS);
        site.setEmptySelectionAllowed(false);
        site.addBlurListener(e -> validateNativeSelect(site));
        return site;
    }

    private Component buildCategory() {
        category = new NativeSelect<>("Category", carService.getAllCarCategory());
        category.setWidth(180.f, Unit.PIXELS);
        category.setEmptySelectionAllowed(false);
        category.addBlurListener(e -> validateNativeSelect(category));
        return category;
    }

    private Component buildBrand() {
        brand = new TextField("Brand");
        brand.setWidth(100.f, Unit.PERCENTAGE);
        brand.setMaxLength(Constants.Car.BRAND_MAX_LENGTH);
        brand.addBlurListener(e -> validateTextField(brand, Constants.Car.BRAND_MIN_LENGTH));
        return brand;
    }

    private Component buildModel() {
        model = new TextField("Model");
        model.setWidth(100.f, Unit.PERCENTAGE);
        model.setMaxLength(Constants.Car.MODEL_MAX_LENGTH);
        model.addBlurListener(e -> validateTextField(model, Constants.Car.MODEL_MIN_LENGTH));
        return model;
    }

    private Component buildYear() {
        year = new TextField("Year");
        year.setWidth(100.f, Unit.PERCENTAGE);
        year.setMaxLength(getLength(Constants.Car.YEAR_MAX_VALUE));
        year.addBlurListener(e -> validateNumberTextField(year,
                Constants.Car.YEAR_MIN_VALUE, Constants.Car.YEAR_MAX_VALUE));
        return year;
    }

    private Component buildSeatNumber() {
        seatNumber = new TextField("Seat number");
        seatNumber.setWidth(100.f, Unit.PERCENTAGE);
        seatNumber.setMaxLength(getLength(Constants.Car.SEAT_NUMBER_MAX_VALUE));
        seatNumber.addBlurListener(e -> validateNumberTextField(seatNumber, 1, Constants.Car.SEAT_NUMBER_MAX_VALUE));
        return seatNumber;
    }

    private Component buildFuelType() {
        fuelType = new NativeSelect<>("Fuel type", carService.getAllFuelType());
        fuelType.setWidth(100.f, Unit.PERCENTAGE);
        fuelType.setEmptySelectionAllowed(false);
        fuelType.addBlurListener(e -> validateNativeSelect(fuelType));
        return fuelType;
    }

    private Component buildFuelConsumption() {
        fuelConsumption = new TextField("Fuel consumption");
        fuelConsumption.setWidth(100.f, Unit.PERCENTAGE);
        fuelConsumption.setMaxLength(4);
        fuelConsumption.setPlaceholder("liter/100km");
        fuelConsumption.addBlurListener(e -> validateNumberTextField(fuelConsumption, 0, 20));
        return fuelConsumption;
    }

    private Component buildTankCapacity() {
        tankCapacity = new TextField("Tank cap.");
        tankCapacity.setWidth(100.f, Unit.PERCENTAGE);
        tankCapacity.setMaxLength(getLength(Constants.Car.TANK_CAPACITY_MAX_VALUE));
        tankCapacity.setDescription("Tank capacity");
        tankCapacity.setPlaceholder("liter");
        tankCapacity.addBlurListener(e -> validateNumberTextField(tankCapacity, 0, Constants.Car.TANK_CAPACITY_MAX_VALUE));
        return tankCapacity;
    }

    private Component buildTrunkCapacity() {
        trunkCapacity = new TextField("Trunk cap.");
        trunkCapacity.setWidth(100.f, Unit.PERCENTAGE);
        trunkCapacity.setMaxLength(getLength(Constants.Car.TRUNK_CAPACITY_MAX_VALUE));
        trunkCapacity.setDescription("Trunk capacity");
        trunkCapacity.setPlaceholder("liter");
        trunkCapacity.addBlurListener(e -> validateNumberTextField(trunkCapacity, 0, Constants.Car.TRUNK_CAPACITY_MAX_VALUE));
        return trunkCapacity;
    }

    private Component buildPrice() {
        price = new TextField("Price");
        price.setWidth(100.f, Unit.PERCENTAGE);
        price.setMaxLength(getLength(Constants.Car.PRICE_MAX_VALUE));
        price.setPlaceholder("HUF/day");
        price.addBlurListener(e -> validateNumberTextField(price, 0, Constants.Car.PRICE_MAX_VALUE));
        return price;
    }

    private Component buildCreateButton() {
        Button create = new Button("Add", e -> create());
        create.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        create.setWidth(100.f, Unit.PERCENTAGE);
        return create;
    }

    private Component buildCloseButton() {
        Button close = new Button("Close", e -> close());
        close.setWidth(100.f, Unit.PERCENTAGE);
        return close;
    }

    private void create() {
        if (isAllComponentValid()) {
            try {
                CreateCarRequest createCarRequest = new CreateCarRequest(
                        siteWithID.get(site.getSelectedItem().get()),
                        CarCategory.valueOf(category.getSelectedItem().get().toString()),
                        brand.getValue(),
                        model.getValue(),
                        Integer.parseInt(trunkCapacity.getValue()),
                        Integer.parseInt(year.getValue()),
                        FuelType.valueOf(fuelType.getSelectedItem().get().toString()),
                        Float.parseFloat(fuelConsumption.getValue()),
                        Integer.parseInt(tankCapacity.getValue()),
                        Integer.parseInt(seatNumber.getValue()),
                        Integer.parseInt(price.getValue())
                );
                carService.save(createCarRequest);
                Notification.show(brand.getValue() + " " + model.getValue()
                        + " successfully added to " + site.getSelectedItem().toString());
                close();
            } catch (UnauthorizedAccessException e) {
                Notification.show("Unauthorized access", "You do not have right to save a car!", Notification.Type.ERROR_MESSAGE);
            } catch (Exception e) {
                Notification.show("Error",
                        "Something went wrong, please contact the developer!\n" +
                                "Error message: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        } else {
            Notification.show("To add a car fill in all fields correctly!", Notification.Type.WARNING_MESSAGE);
        }
    }

    private boolean isAllComponentValid() {
        Long validComponents = componentWithValidation.entrySet().stream()
                .filter(e -> e.getValue().equals(Boolean.TRUE)).count();
        return validComponents.equals(11L);
    }

    private void validateNativeSelect(NativeSelect<?> nativeSelect) {
        nativeSelect.setComponentError(null);
        componentWithValidation.put(nativeSelect.getCaption(), Boolean.TRUE);

        if (!nativeSelect.getSelectedItem().isPresent()) {
            warning(nativeSelect, INPUT_EMPTY, Notification.Type.WARNING_MESSAGE);
        }
    }

    private void validateTextField(TextField textField, int minLength) {
        textField.setComponentError(null);
        componentWithValidation.put(textField.getCaption(), Boolean.TRUE);

        if (textField.isEmpty()) {
            warning(textField, INPUT_EMPTY, Notification.Type.WARNING_MESSAGE);
        } else {
            if (textField.getValue().length() < minLength) {
                warning(textField, String.format("must be at least %s characters long!", minLength),
                        Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    private void validateNumberTextField(TextField textField, Integer minValue, Integer maxValue) {
        textField.setComponentError(null);
        componentWithValidation.put(textField.getCaption(), Boolean.TRUE);

        if (!textField.isEmpty()) {
            try {
                Integer value = Integer.valueOf(textField.getValue());
                if (!((value >= minValue) && (value <= maxValue))) {
                    warning(textField, String.format("value can be between %s and %s only!", minValue, maxValue),
                            Notification.Type.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                warning(textField, FORMAT_INVALID, Notification.Type.WARNING_MESSAGE);
            }
        } else {
            warning(textField, INPUT_EMPTY, Notification.Type.WARNING_MESSAGE);
        }
    }

    private void warning(AbstractComponent component, String message, Notification.Type type) {
        componentWithValidation.put(component.getCaption(), Boolean.FALSE);

        showNotification(component.getCaption() + " " + message, type);
        component.setComponentError(new UserError(component.getCaption() + " " + message));
    }

    private void showNotification(String message, Notification.Type type) {
        Notification notification = new Notification(null, message, type);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(CarRentalUI.getCurrent().getPage());
    }

    private int getLength(int number) {
        return (int) Math.log10((double) number) + 1;
    }

    private NativeSelect<?> site;
    private NativeSelect<?> category;
    private TextField brand;
    private TextField model;
    private TextField trunkCapacity;
    private TextField year;
    private NativeSelect<?> fuelType;
    private TextField fuelConsumption;
    private TextField tankCapacity;
    private TextField seatNumber;
    private TextField price;

    private Map<String, Long> siteWithID;
    private Map<String, Boolean> componentWithValidation;

    private final SiteService siteService;
    private final CarService carService;

    private static final String INPUT_EMPTY = "can not be empty!";
    private static final String FORMAT_INVALID = "has invalid format!";
}
