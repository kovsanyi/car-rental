package hu.unideb.inf.carrental.ui.reservation.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.content.reservation.ReservationContent;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public class ManagerReservationContent extends ReservationContent {

    public ManagerReservationContent(ReservationService reservationService, SiteService siteService) {
        this.reservationService = reservationService;
        this.siteService = siteService;

        setupHeader();
        setupBody();
    }

    private void setupHeader() {
        markAsClosed = buildMarkAsClosedButton();
        getHeader().addComponent(markAsClosed);
        getHeader().setComponentAlignment(markAsClosed, Alignment.MIDDLE_RIGHT);
    }

    private void setupBody() {
        final Accordion accordion = new Accordion();
        accordion.setSizeFull();
        accordion.setTabCaptionsAsHtml(true);

        try {
            Grid<ReservationResponse> activeReservations = buildGrid(reservationService.getActiveBySiteId(
                    siteService.getByManager().getId()));
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
            accordion.addTab(buildGrid(reservationService.getClosedBySiteId(siteService.getByManager().getId())),
                    String.format("%s - %s", "Reservations", bold("Closed")));
            accordion.addTab(buildGrid(reservationService.getAllBySiteId(siteService.getByManager().getId())),
                    String.format("%s - %s", "Reservations", bold("All")));

            getBody().addComponent(accordion);
        } catch (NotFoundException e) {
            Label noCars = new Label("No managed site!");
            noCars.addStyleName(ValoTheme.LABEL_H1);
            getBody().addComponent(noCars);
            ((VerticalLayout) getBody()).setComponentAlignment(noCars, Alignment.MIDDLE_CENTER);
        } catch (UnauthorizedAccessException e) {
            UIUtils.showNotification(e.getMessage(), Notification.Type.WARNING_MESSAGE);
        }
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
                markAsClosed.setEnabled(false);
                refreshBody();
            }
        });

        return markAsClosed;
    }

    @Override
    public void refreshBody() {
        getBody().removeAllComponents();
        setupBody();
    }

    private ReservationResponse selectedItem;
    private Button markAsClosed;

    private final ReservationService reservationService;
    private final SiteService siteService;
}
