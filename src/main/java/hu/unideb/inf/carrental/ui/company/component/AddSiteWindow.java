package hu.unideb.inf.carrental.ui.company.component;

import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import org.apache.commons.validator.EmailValidator;

import java.util.HashMap;
import java.util.Map;

public class AddSiteWindow extends Window {

    public AddSiteWindow(SiteService siteService) {
        this.siteService = siteService;

        setCaption("Add a site");
        setSizeUndefined();
        setModal(true);
        setResizable(false);
        setDraggable(false);
        center();
        setContent(buildContent());

        componentWithValidation = new HashMap<>();
    }

    private AbstractLayout buildContent() {
        GridLayout content = new GridLayout(1, 6);
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();

        final HorizontalLayout containerOfZipCodeAndCity = new HorizontalLayout();
        containerOfZipCodeAndCity.setSizeUndefined();
        containerOfZipCodeAndCity.setMargin(false);
        containerOfZipCodeAndCity.addComponents(
                buildZipCode(),
                buildCity());

        content.addComponent(containerOfZipCodeAndCity);
        content.addComponents(
                buildAddress(),
                buildEmail(),
                buildPhoneNumber(),
                buildOpeningHours());

        final HorizontalLayout containerOfCreateAndCloseButton = new HorizontalLayout();
        containerOfCreateAndCloseButton.setWidth(100.f, Unit.PERCENTAGE);
        containerOfCreateAndCloseButton.setMargin(false);
        containerOfCreateAndCloseButton.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        containerOfCreateAndCloseButton.addComponents(
                buildCreateButton(),
                buildCloseButton());

        content.addComponent(containerOfCreateAndCloseButton);

        return content;
    }

    private Component buildZipCode() {
        zipCode = new TextField("Zip code");
        zipCode.setWidth(100.f, Unit.PIXELS);
        zipCode.setMaxLength(getLength(Constants.Site.ZIP_CODE_MAX_VALUE));
        zipCode.addBlurListener(e -> validateZipCodeTextField(zipCode));
        return zipCode;
    }

    private Component buildCity() {
        city = new TextField("City");
        city.setWidth(250.f, Unit.PIXELS);
        city.setMaxLength(Constants.Site.CITY_MAX_LENGTH);
        city.addBlurListener(e -> validateTextField(city, Constants.Site.CITY_MIN_LENGTH));
        return city;
    }

    private Component buildAddress() {
        address = new TextField("Address");
        address.setWidth(100.f, Unit.PERCENTAGE);
        address.setMaxLength(Constants.Site.ADDRESS_MAX_LENGTH);
        address.addBlurListener(e -> validateTextField(address, Constants.Site.ADDRESS_MIN_LENGTH));
        return address;
    }

    private Component buildEmail() {
        email = new TextField("Site email");
        email.setWidth(100.f, Unit.PERCENTAGE);
        email.setMaxLength(Constants.Site.EMAIL_MAX_LENGTH);
        email.setPlaceholder("site@sitemail.com");
        email.addBlurListener(e -> validateEmailTextField(email));
        return email;
    }

    private Component buildPhoneNumber() {
        phoneNumber = new TextField("Phone number");
        phoneNumber.setWidth(100.f, Unit.PERCENTAGE);
        phoneNumber.setMaxLength(Constants.Site.PHONE_MAX_LENGTH);
        phoneNumber.setPlaceholder("+12 34 567 8910");
        phoneNumber.addBlurListener(e -> validateTextField(phoneNumber, Constants.Site.PHONE_MIN_LENGTH));
        return phoneNumber;
    }

    private Component buildOpeningHours() {
        openingHours = new TextField("Opening hours");
        openingHours.setWidth(100.f, Unit.PERCENTAGE);
        openingHours.setMaxLength(Constants.Site.OPENING_HOURS_MAX_LENGTH);
        openingHours.setPlaceholder("Mon.-Fri. 8:00am-2.00pm, Sat.-Sunday closed");
        openingHours.addBlurListener(e -> validateTextField(openingHours, Constants.Site.OPENING_HOURS_MIN_LENGTH));
        return openingHours;
    }

    private Component buildCreateButton() {
        create = new Button("Add", e -> create());
        create.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        create.setWidth(100.f, Unit.PERCENTAGE);
        return create;
    }

    private Component buildCloseButton() {
        close = new Button("Close", e -> close());
        close.setWidth(100.f, Unit.PERCENTAGE);
        return close;
    }

    private void create() {
        if (isAllComponentValid()) {
            try {
                CreateSiteRequest createSiteRequest = new CreateSiteRequest(
                        null,
                        email.getValue(),
                        phoneNumber.getValue(),
                        Integer.parseInt(zipCode.getValue()),
                        city.getValue(),
                        address.getValue(),
                        openingHours.getValue()
                );
                siteService.save(createSiteRequest);
                Notification.show("Site added successfully!");
                close();
            } catch (Exception e) {
                Notification.show("Error",
                        "Something went wrong, please contact the developer!\n" +
                                "Error message: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);

            }
        } else {
            Notification.show("To add a site fill in all fields correctly!", Notification.Type.WARNING_MESSAGE);
        }
    }

    private boolean isAllComponentValid() {
        Long validComponents = componentWithValidation.entrySet().stream()
                .filter(e -> e.getValue().equals(Boolean.TRUE)).count();
        return validComponents.equals(6L);
    }

    private void validateZipCodeTextField(TextField textField) {
        textField.setComponentError(null);
        componentWithValidation.put(textField.getCaption(), Boolean.TRUE);

        if (!textField.isEmpty()) {
            try {
                Integer value = Integer.valueOf(textField.getValue());
                if (!((value >= Constants.Site.ZIP_CODE_MIN_VALUE)
                        && (value <= Constants.Site.ZIP_CODE_MAX_VALUE))) {
                    warning(textField,
                            String.format("value can be between %s and %s only!",
                                    Constants.Site.ZIP_CODE_MIN_VALUE,
                                    Constants.Site.ZIP_CODE_MAX_VALUE),
                            Notification.Type.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                warning(textField, FORMAT_INVALID, Notification.Type.WARNING_MESSAGE);
            }
        } else {
            warning(textField, INPUT_EMPTY, Notification.Type.WARNING_MESSAGE);
        }
    }

    private void validateEmailTextField(TextField textField) {
        textField.setComponentError(null);
        componentWithValidation.put(textField.getCaption(), Boolean.TRUE);

        if (!EmailValidator.getInstance().isValid(textField.getValue())) {
            warning(textField, FORMAT_INVALID, Notification.Type.WARNING_MESSAGE);
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

    private TextField email;
    private TextField phoneNumber;
    private TextField zipCode;
    private TextField city;
    private TextField address;
    private TextField openingHours;

    private Button create;
    private Button close;

    private Map<String, Boolean> componentWithValidation;

    private final SiteService siteService;

    private static final String INPUT_EMPTY = "can not be empty!";
    private static final String FORMAT_INVALID = "has invalid format!";
}
