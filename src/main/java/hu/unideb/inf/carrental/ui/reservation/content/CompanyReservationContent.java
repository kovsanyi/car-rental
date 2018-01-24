package hu.unideb.inf.carrental.ui.reservation.content;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.util.List;

public class CompanyReservationContent extends VerticalLayout {

    public CompanyReservationContent(ReservationService reservationService) {
        this.reservationService = reservationService;

        setMargin(false);
        setSpacing(false);
        setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        setHeightUndefined();
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponents(
                buildHeader(),
                buildAccordion()
        );

        return content;
    }

    private AbstractOrderedLayout buildHeader() {
        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setMargin(false);
        headerLayout.setSpacing(false);
        headerLayout.setWidth(100.f, Unit.PERCENTAGE);

        Button button = buildMarkAsReturnedButton();

        headerLayout.addComponents(
                buildTitle(),
                button
        );
        headerLayout.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
        return headerLayout;
    }

    private Accordion buildAccordion() {
        final Accordion accordion = new Accordion();
        accordion.setSizeFull();

        Grid<ReservationResponse> activeReservations = buildGrid(reservationService.getActiveByCompany());
        activeReservations.addSelectionListener(e -> {
            if (activeReservations.getSelectedItems().isEmpty()) {
                markAsClosed.setEnabled(false);
            } else {
                markAsClosed.setEnabled(true);
                activeReservations.getSelectedItems().stream().findFirst().ifPresent(reservationResponse -> selectedItem = reservationResponse);
            }
        });

        accordion.addTab(activeReservations, "Reservations - Active");
        accordion.addTab(buildGrid(reservationService.getClosedByCompany()), "Reservations - Closed");
        accordion.addTab(buildGrid(reservationService.getAllByCompany()), "Reservations - All");

        accordion.addSelectedTabChangeListener(e -> activeReservations.deselectAll());
        return accordion;
    }

    private Grid<ReservationResponse> buildGrid(List<ReservationResponse> reservationResponses) {
        final Grid<ReservationResponse> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
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

    private Label buildTitle() {
        final Label title = new Label("Reservations");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        title.setId("title");

        return title;
    }

    private Button buildMarkAsReturnedButton() {
        markAsClosed = new Button("Mark as closed");
        markAsClosed.setDescription("If the customer has returned the car mark it as closed " +
                "then the reservation will be closed");
        markAsClosed.setStyleName(ValoTheme.BUTTON_PRIMARY);
        markAsClosed.setEnabled(false);
        markAsClosed.addClickListener(e -> {
            try {
                reservationService.close(selectedItem.getId());
                refresh();
                UIUtils.showNotification("Reservation closed successfully!", Notification.Type.HUMANIZED_MESSAGE);
            } catch (NotFoundException ex) {
                UIUtils.showNotification("Error", "Something went wrong, please contact the developer!\n" +
                        "Error message: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            } catch (UnauthorizedAccessException ex) {
                UIUtils.showNotification("Error", "You do not have right to save a car!",
                        Notification.Type.ERROR_MESSAGE);
            }
        });

        return markAsClosed;
    }

    private void refresh() {
        removeAllComponents();
        addComponent(buildContent());
    }

    private Button markAsClosed;

    private ReservationResponse selectedItem;

    private final ReservationService reservationService;
}
