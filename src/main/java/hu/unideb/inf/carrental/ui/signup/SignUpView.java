package hu.unideb.inf.carrental.ui.signup;

import com.vaadin.navigator.View;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.CompanyEmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NameAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import hu.unideb.inf.carrental.customer.service.CustomerService;
import hu.unideb.inf.carrental.manager.resource.model.CreateManagerRequest;
import hu.unideb.inf.carrental.manager.service.ManagerService;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.login.LoginView;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@UIScope
@SpringView(name = SignUpView.VIEW_NAME)
public class SignUpView extends VerticalLayout implements View {

    @Autowired
    public SignUpView(CustomerService customerService, CompanyService companyService, ManagerService managerService) {
        this.customerService = customerService;
        this.companyService = companyService;
        this.managerService = managerService;

        componentWithValidation = new HashMap<>();

        setSizeFull();
        setMargin(false);
        setSpacing(false);

        final VerticalLayout wrapper = new VerticalLayout();
        wrapper.setMargin(false);
        wrapper.setWidth(hu.unideb.inf.carrental.ui.commons.constant.Constants.Size.WIDTH, Unit.PIXELS);
        wrapper.setHeightUndefined();
        wrapper.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        wrapper.addComponent(buildContent());

        addComponent(wrapper);
        setComponentAlignment(wrapper, Alignment.MIDDLE_CENTER);
    }

    private AbstractLayout buildContent() {
        final FormLayout signUpLayout = new FormLayout();
        signUpLayout.setWidth(400.f, Unit.PIXELS);
        signUpLayout.setHeightUndefined();

        signUpLayout.addComponents(
                buildUsername(),
                buildPassword(),
                buildEmail(),
                buildFullName(),
                buildPhoneNumber());

        final HorizontalLayout containerOfZipCodeAndCity = new HorizontalLayout();
        containerOfZipCodeAndCity.setWidth(100.f, Unit.PERCENTAGE);
        containerOfZipCodeAndCity.setMargin(false);
        containerOfZipCodeAndCity.addComponents(
                buildZipCode(),
                buildCity());

        signUpLayout.addComponents(
                containerOfZipCodeAndCity,
                buildAddress(),
                buildManager(),
                buildCompany(),
                buildCompanyName(),
                buildCompanyEmail(),
                buildCreateButton(),
                buildCancelButton());

        return signUpLayout;
    }

    private Component buildUsername() {
        username = new TextField("Username");
        username.setWidth(100.f, Unit.PERCENTAGE);
        username.setMaxLength(Constants.User.USERNAME_MAX_LENGTH);
        username.addBlurListener(e -> validateTextField(username, Constants.User.USERNAME_MIN_LENGTH));
        return username;
    }

    private Component buildPassword() {
        password = new PasswordField("Password");
        password.setWidth(100.f, Unit.PERCENTAGE);
        password.setMaxLength(Constants.User.PASSWORD_MAX_LENGTH);
        password.addBlurListener(e -> validateTextField(password, Constants.User.PASSWORD_MIN_LENGTH));
        return password;
    }

    private Component buildEmail() {
        email = new TextField("Email");
        email.setWidth(100.f, Unit.PERCENTAGE);
        email.setMaxLength(Constants.User.EMAIL_MAX_LENGTH);
        email.addBlurListener(e -> validateEmailTextField(email));
        return email;
    }

    private Component buildFullName() {
        fullName = new TextField("Full name");
        fullName.setWidth(100.f, Unit.PERCENTAGE);
        fullName.setMaxLength(Constants.User.FULL_NAME_MAX_LENGTH);
        fullName.addBlurListener(e -> validateTextField(fullName, Constants.User.FULL_NAME_MIN_LENGTH));
        return fullName;
    }

    private Component buildPhoneNumber() {
        phoneNumber = new TextField("Phone number");
        phoneNumber.setWidth(100.f, Unit.PERCENTAGE);
        phoneNumber.setMaxLength(Constants.User.PHONE_MAX_LENGTH);
        phoneNumber.addBlurListener(e -> validateTextField(phoneNumber, Constants.User.PHONE_MIN_LENGTH));
        return phoneNumber;
    }

    private Component buildZipCode() {
        zipCode = new TextField("Zip code");
        zipCode.setWidth(100.f, Unit.PERCENTAGE);
        zipCode.setMaxLength(getLength(Constants.User.ZIP_CODE_MAX_VALUE));
        zipCode.addBlurListener(e -> validateZipCodeTextField(zipCode));
        return zipCode;
    }

    private Component buildCity() {
        city = new TextField("City");
        city.setWidth(100.f, Unit.PERCENTAGE);
        city.setMaxLength(Constants.User.CITY_MAX_LENGTH);
        city.addBlurListener(e -> validateTextField(city, Constants.User.CITY_MIN_LENGTH));
        return city;
    }

    private Component buildAddress() {
        address = new TextField("Address");
        address.setWidth(100.f, Unit.PERCENTAGE);
        address.setMaxLength(Constants.User.ADDRESS_MAX_LENGTH);
        address.addBlurListener(e -> validateTextField(address, Constants.User.ADDRESS_MIN_LENGTH));
        return address;
    }

    private Component buildCompanyName() {
        companyName = new TextField("Company name");
        companyName.setWidth(100.f, Unit.PERCENTAGE);
        companyName.setMaxLength(Constants.User.COMPANY_NAME_MAX_LENGTH);
        companyName.addBlurListener(e -> validateTextField(companyName, Constants.User.COMPANY_NAME_MIN_LENGTH));
        companyName.setVisible(false);
        return companyName;
    }

    private Component buildCompanyEmail() {
        companyEmail = new TextField("Company email");
        companyEmail.setWidth(100.f, Unit.PERCENTAGE);
        companyEmail.setMaxLength(Constants.User.EMAIL_MAX_LENGTH);
        companyEmail.addBlurListener(e -> validateEmailTextField(companyEmail));
        companyEmail.setVisible(false);
        return companyEmail;
    }

    private Component buildManager() {
        manager = new CheckBox("I am a manager");
        manager.addValueChangeListener(e -> {
            if (manager.getValue()) {
                company.setValue(false);
            }
        });
        return manager;
    }

    private Component buildCompany() {
        company = new CheckBox("I am a company owner");
        company.addValueChangeListener(e -> {
            if (company.getValue()) {
                manager.setValue(Boolean.FALSE);
                companyName.setVisible(true);
                companyEmail.setVisible(true);
            } else {
                companyName.setVisible(false);
                companyEmail.setVisible(false);
            }
        });
        return company;
    }

    private Component buildCreateButton() {
        Button create = new Button("Create an account", e -> create());
        create.setWidth(100.f, Unit.PERCENTAGE);
        create.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        return create;
    }

    private Component buildCancelButton() {
        Button cancel = new Button("Back",
                e -> CarRentalUI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME));
        cancel.setWidth(100.f, Unit.PERCENTAGE);
        return cancel;
    }

    private void create() {
        if (isAllComponentValid()) {
            try {
                if (company.getValue()) {
                    CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest(
                            username.getValue(),
                            password.getValue(),
                            email.getValue(),
                            companyName.getValue(),
                            companyEmail.getValue(),
                            fullName.getValue(), phoneNumber.getValue(),
                            Integer.parseInt(zipCode.getValue()),
                            city.getValue(),
                            address.getValue()
                    );
                    companyService.save(createCompanyRequest);
                } else if (manager.getValue()) {
                    CreateManagerRequest createManagerRequest = new CreateManagerRequest(
                            username.getValue(),
                            password.getValue(),
                            email.getValue(),
                            fullName.getValue(),
                            phoneNumber.getValue(),
                            Integer.parseInt(zipCode.getValue()),
                            city.getValue(),
                            address.getValue()
                    );
                    managerService.save(createManagerRequest);
                } else {
                    CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(
                            username.getValue(),
                            password.getValue(),
                            email.getValue(),
                            fullName.getValue(),
                            phoneNumber.getValue(),
                            Integer.parseInt(zipCode.getValue()),
                            city.getValue(),
                            address.getValue()
                    );
                    customerService.save(createCustomerRequest);
                }

                CarRentalUI.getCurrent().getPage().reload();
            } catch (UsernameAlreadyInUseException e) {
                Notification.show("Entered username already in use!",
                        Notification.Type.WARNING_MESSAGE);
            } catch (EmailAlreadyInUseException e) {
                Notification.show("Entered email already in use!",
                        Notification.Type.WARNING_MESSAGE);
            } catch (NameAlreadyInUseException e) {
                Notification.show("Entered company name already in use!",
                        Notification.Type.WARNING_MESSAGE);
            } catch (CompanyEmailAlreadyInUseException e) {
                Notification.show("Entered company email already in use!",
                        Notification.Type.WARNING_MESSAGE);
            }
        } else {
            Notification.show("To create an account, please fill in all fields correctly!",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    private boolean isAllComponentValid() {
        if (!company.getValue()) {
            componentWithValidation.remove(companyName.getCaption());
            componentWithValidation.remove(companyEmail.getCaption());
        }
        Long validComponents = componentWithValidation.entrySet().stream()
                .filter(e -> e.getValue().equals(Boolean.TRUE)).count();
        return company.getValue() ? validComponents.equals(10L) : validComponents.equals(8L);
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

    private void validateEmailTextField(TextField textField) {
        textField.setComponentError(null);
        componentWithValidation.put(textField.getCaption(), Boolean.TRUE);

        if (!EmailValidator.getInstance().isValid(textField.getValue())) {
            warning(textField, FORMAT_INVALID, Notification.Type.WARNING_MESSAGE);
        }
    }

    private void validateZipCodeTextField(TextField textField) {
        textField.setComponentError(null);
        componentWithValidation.put(textField.getCaption(), Boolean.TRUE);

        if (!textField.isEmpty()) {
            try {
                Integer value = Integer.valueOf(textField.getValue());
                if (!((value >= Constants.User.ZIP_CODE_MIN_VALUE)
                        && (value <= Constants.User.ZIP_CODE_MAX_VALUE))) {
                    warning(textField,
                            String.format("value can be between %s and %s only!",
                                    Constants.User.ZIP_CODE_MIN_VALUE,
                                    Constants.User.ZIP_CODE_MAX_VALUE),
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

    private TextField username;
    private PasswordField password;
    private TextField email;
    private TextField fullName;
    private TextField phoneNumber;
    private TextField zipCode;
    private TextField city;
    private TextField address;
    private TextField companyName;
    private TextField companyEmail;

    private CheckBox manager;
    private CheckBox company;

    private Map<String, Boolean> componentWithValidation;

    private final CustomerService customerService;
    private final ManagerService managerService;
    private final CompanyService companyService;

    private static final String INPUT_EMPTY = "can not be empty!";
    private static final String FORMAT_INVALID = "has invalid format!";

    public static final String VIEW_NAME = "signup";
}
