package hu.unideb.inf.carrental.ui.commons.content.reservation;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Grid;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.ui.commons.content.root.CarRentalContent;

import java.util.List;

public abstract class ReservationContent extends CarRentalContent {
    protected ReservationContent() {
        super("Reservation");
    }

    protected Grid<ReservationResponse> buildGrid(List<ReservationResponse> reservationResponses) {
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

    public abstract void refreshBody();

    @Override
    protected AbstractLayout buildBody() {
        return buildDefaultVerticalBody();
    }

}
