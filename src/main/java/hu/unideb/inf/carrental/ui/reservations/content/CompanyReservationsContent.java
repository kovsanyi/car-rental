package hu.unideb.inf.carrental.ui.reservations.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.ui.commons.content.CarRentalContent;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.util.List;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public class CompanyReservationsContent extends CarRentalContent {
    public CompanyReservationsContent(ReservationService reservationService) {
        super("Reservation");
        this.reservationService = reservationService;

        setupHeader();
        setupBody();
    }

    private void setupHeader() {
        markAsClosed = buildMarkAsClosedButton();
        getHeader().addComponent(markAsClosed);
        getHeader().setComponentAlignment(markAsClosed, Alignment.MIDDLE_RIGHT);
    }

    private void setupBody() {
        Accordion accordion = new Accordion();
        accordion.setSizeFull();
        accordion.setTabCaptionsAsHtml(true);

        Grid<ReservationResponse> activeReservations = buildGrid(reservationService.getActiveByCompany());
        activeReservations.addSelectionListener(e -> {
            if (!activeReservations.getSelectedItems().isEmpty()) {
                markAsClosed.setEnabled(true);
                activeReservations.getSelectedItems().stream()
                        .findFirst()
                        .ifPresent(reservationResponse -> selectedItem = reservationResponse);
            } else {
                markAsClosed.setEnabled(false);
            }
        });

        accordion.addTab(activeReservations,
                String.format("%s - %s", "Reservations", bold("Active")));
        accordion.addTab(buildGrid(reservationService.getClosedByCompany()),
                String.format("%s - %s", "Reservations", bold("Closed")));
        accordion.addTab(buildGrid(reservationService.getAllByCompany()),
                String.format("%s - %s", "Reservations", bold("All")));

        getBody().addComponent(accordion);
    }

    private Button buildMarkAsClosedButton() {
        Button markAsClosed = new Button("Mark as closed");
        markAsClosed.setDescription("If the customer has returned the car " +
                "mark it as closed then the reservation will be closed");
        markAsClosed.setEnabled(false);
        markAsClosed.setStyleName(ValoTheme.BUTTON_PRIMARY);
        markAsClosed.setIcon(VaadinIcons.CHECK);
        markAsClosed.addClickListener(e -> {
            try {
                reservationService.close(selectedItem.getId());
                UIUtils.showNotification("Reservation closed successfully!",
                        Notification.Type.HUMANIZED_MESSAGE);
            } catch (NotFoundException ex) {
                UIUtils.showNotification("Error",
                        "Something went wrong, please contact the developer!\nError message: " + ex.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
            } catch (UnauthorizedAccessException ex) {
                UIUtils.showNotification("Error",
                        "You do not have right to save a car!",
                        Notification.Type.ERROR_MESSAGE);
            } finally {
                refreshBody();
            }
        });

        return markAsClosed;
    }

    private Grid<ReservationResponse> buildGrid(List<ReservationResponse> reservationResponses) {
        final Grid<ReservationResponse> grid = new Grid<>();
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(reservationResponses);

        grid.addColumn(ReservationResponse::getCarBrand).setCaption("Car brand");
        grid.addColumn(ReservationResponse::getCarModel).setCaption("Car model");
        grid.addColumn(ReservationResponse::getCustomerUserUsername).setCaption("Customer username");
        grid.addColumn(ReservationResponse::getCustomerFullName).setCaption("Customer full name");
        grid.addColumn(ReservationResponse::getReceiveDate).setCaption("Receive date");
        grid.addColumn(ReservationResponse::getPlannedReturnDate).setCaption("Planned return date");
        grid.addColumn(ReservationResponse::getReturnedDate).setCaption("Returned date");
        grid.addColumn(ReservationResponse::getPrice).setCaption("Price");

        return grid;
    }

    private void refreshBody() {
        getBody().removeAllComponents();
        setupBody();
    }

    @Override
    protected AbstractLayout buildBody() {
        return buildDefaultVerticalBody();
    }

    private ReservationResponse selectedItem;

    private Button markAsClosed;

    private final ReservationService reservationService;
}
