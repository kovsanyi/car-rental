package hu.unideb.inf.carrental.ui.reservations.content;

import com.vaadin.ui.Accordion;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.ui.commons.content.reservation.ReservationContent;

import java.util.Collections;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public class CustomerReservationsContent extends ReservationContent {

    public CustomerReservationsContent(ReservationService reservationService) {
        this.reservationService = reservationService;

        setupBody();
    }

    private void setupBody() {
        final Accordion accordion = new Accordion();
        accordion.setSizeFull();
        accordion.setTabCaptionsAsHtml(true);

        try {
            accordion.addTab(buildGrid(Collections.singletonList(reservationService.getActiveByCustomer())),
                    String.format("%s - %s", "Reservations", bold("Active")));
        } catch (NotFoundException e) {
            accordion.addTab(buildGrid(Collections.emptyList()),
                    String.format("%s - %s", "Reservations", bold("Active")));
        }
        accordion.addTab(buildGrid(reservationService.getClosedByCustomer()),
                String.format("%s - %s", "Reservations", bold("Closed")));
        accordion.addTab(buildGrid(reservationService.getAllByCustomer()),
                String.format("%s - %s", "Reservations", bold("All")));

        getBody().addComponent(accordion);
    }

    @Override
    public void refreshBody() {
        getBody().removeAllComponents();
        setupBody();
    }

    private final ReservationService reservationService;
}
