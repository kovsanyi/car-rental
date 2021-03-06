package hu.unideb.inf.carrental.ui.commons.component.window;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.InvalidInputException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.ReservationCollisionException;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.buildKeyLabel;
import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.buildValueLabel;
import static java.time.temporal.ChronoUnit.DAYS;

public class ReservationWindow extends Window {

    public ReservationWindow(CarResponse carResponse, SiteResponse siteResponse, ReservationService reservationService) {
        this.carResponse = carResponse;
        this.siteResponse = siteResponse;
        this.reservationService = reservationService;

        receiveDate = buildReceiveDate();
        returnDate = buildReturnDate();
        price = buildPrice();
        content = buildContent();

        setCaption("Make a reservation");
        setWidth(320.f, Unit.PIXELS);
        setHeightUndefined();
        addStyleName("reservation-window");
        setModal(true);
        setResizable(false);
        setDraggable(false);
        center();
        setContent(content);
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponents(
                buildDetails(),
                buildDateFields(),
                price,
                buildButtons()
        );
        return content;
    }

    private AbstractLayout buildDetails() {
        final GridLayout details = new GridLayout(2, 6);
        details.setMargin(false);
        details.setSpacing(false);
        details.setWidth(100.f, Unit.PERCENTAGE);
        details.setHeightUndefined();

        details.addComponents(
                buildKeyLabel("Car:"),
                buildValueLabel(String.format("%s %s", carResponse.getBrand(), carResponse.getModel())),
                buildKeyLabel("Pick up at: "),
                buildValueLabel(String.format("%s %s,", siteResponse.getZipCode(), siteResponse.getCity())),
                buildKeyLabel(""),
                buildValueLabel(siteResponse.getAddress()),
                buildKeyLabel("Price per day: "),
                buildValueLabel(carResponse.getPrice().toString() + " HUF")
        );
        return details;
    }

    private AbstractLayout buildDateFields() {
        final GridLayout controls = new GridLayout(2, 2);
        controls.setMargin(false);
        controls.setSpacing(true);
        controls.setWidth(100.f, Unit.PERCENTAGE);
        controls.setHeightUndefined();

        controls.addComponents(
                receiveDate,
                returnDate
        );
        return controls;
    }

    private AbstractLayout buildButtons() {
        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setMargin(false);
        buttons.setSpacing(true);
        buttons.setWidth(100.f, Unit.PERCENTAGE);
        buttons.setHeightUndefined();

        buttons.addComponents(
                buildReserveButton(),
                buildCancelButton()
        );
        return buttons;
    }

    private DateField buildReceiveDate() {
        final DateField receiveDate = new DateField("Receive date");
        receiveDate.setWidth(100.f, Unit.PERCENTAGE);
        receiveDate.setDateFormat("yyyy-MM-dd");
        receiveDate.setValue(LocalDate.now());
        receiveDate.addValueChangeListener(e -> refreshPrice(receiveDate));
        return receiveDate;
    }

    private DateField buildReturnDate() {
        final DateField returnDate = new DateField("Return date");
        returnDate.setWidth(100.f, Unit.PERCENTAGE);
        returnDate.setDateFormat("yyyy-MM-dd");
        returnDate.setValue(LocalDate.now());
        returnDate.addValueChangeListener(e -> refreshPrice(returnDate));
        return returnDate;
    }

    private Label buildPrice() {
        final Label price = new Label(String.format("Price totally: %s %s", carResponse.getPrice(), "HUF"));
        price.setWidth(100.f, Unit.PERCENTAGE);
        price.setHeightUndefined();
        price.setId("price-content");
        return price;
    }

    private Button buildReserveButton() {
        final Button reserve = new Button("Reserve");
        reserve.setWidth(100.f, Unit.PERCENTAGE);
        reserve.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        reserve.addClickListener(e -> makeReservation());
        return reserve;
    }

    private Button buildCancelButton() {
        final Button cancel = new Button("Cancel");
        cancel.setWidth(100.f, Unit.PERCENTAGE);
        cancel.addClickListener(e -> close());
        return cancel;
    }

    private void makeReservation() {
        try {
            reservationService.reserve(new CreateReservationRequest(
                    carResponse.getId(),
                    receiveDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    returnDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ));
            CarRentalUI.getCurrent().getPage().reload();
        } catch (CarInRentException | ReservationCollisionException | InvalidInputException e) {
            UIUtils.showNotification(e.getMessage(), Notification.Type.WARNING_MESSAGE);
        } catch (NotFoundException e) {
            UIUtils.showNotification(e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void refreshPrice(DateField dateField) {
        if (dateField.getValue().isBefore(LocalDate.now())) {
            dateField.setValue(LocalDate.now());
            UIUtils.showNotification("Invalid date can not be selected!", Notification.Type.WARNING_MESSAGE);
        }
        if (receiveDate.getValue().isAfter(returnDate.getValue())) {
            receiveDate.setValue(LocalDate.now());
            returnDate.setValue(LocalDate.now());
            UIUtils.showNotification("Invalid date can not be selected!", Notification.Type.WARNING_MESSAGE);
        }
        long priceValue = (DAYS.between(receiveDate.getValue(), returnDate.getValue()) + 1) * carResponse.getPrice();
        price.setValue(String.format("Price totally: %s %s", priceValue, "HUF"));
    }

    private final DateField receiveDate;
    private final DateField returnDate;
    private final Label price;
    private final AbstractLayout content;

    private final CarResponse carResponse;
    private final SiteResponse siteResponse;
    private final ReservationService reservationService;
}
