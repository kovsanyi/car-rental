package hu.unideb.inf.carrental.ui.company.layout.reservation;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import hu.unideb.inf.carrental.reservation.service.ReservationService;

public class ReservationLayout extends VerticalLayout {

    public ReservationLayout(ReservationService reservationService) {
        this.reservationService = reservationService;

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(false);
        setWidth(1366.f, Unit.PIXELS);
        setHeightUndefined();

        Label title = new Label("Reservations");
        title.setStyleName(ValoTheme.LABEL_H1);

        addComponent(title);
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(false);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponent(buildGrid());
        return content;
    }

    private Grid<?> buildGrid() {
        Grid<ReservationResponse> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth(1366.f, Unit.PIXELS);
        grid.setHeightUndefined();
        grid.setItems(reservationService.getAllByCompany());

        grid.addColumn(ReservationResponse::getCarId).setCaption("Car ID");
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

    private final ReservationService reservationService;
}
