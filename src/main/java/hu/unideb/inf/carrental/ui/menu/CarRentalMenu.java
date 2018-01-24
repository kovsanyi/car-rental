package hu.unideb.inf.carrental.ui.menu;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.MenuBar;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.cars.CarsView;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import hu.unideb.inf.carrental.ui.reservation.ReservationView;
import hu.unideb.inf.carrental.ui.sites.SitesView;
import hu.unideb.inf.carrental.ui.statistics.StatisticsView;

public class CarRentalMenu extends MenuBar {

    public CarRentalMenu() {
        setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        setHeightUndefined();
        buildMenuBar();
    }

    private void buildMenuBar() {
        addItem("Overview", VaadinIcons.HOME, null);
        addItem("Cars", e -> navigate(CarsView.VIEW_NAME));
        addItem("Sites", e -> navigate(SitesView.VIEW_NAME));
        addItem("Reservations", e -> navigate(ReservationView.VIEW_NAME));
        addItem("Statistics", e -> navigate(StatisticsView.VIEW_NAME));
        addItem("Logout", VaadinIcons.EXIT, e -> CarRentalEventBus.post(new CarRentalEvent.LogoutRequestEvent()));
    }

    private void navigate(String viewName) {
        CarRentalUI.getCurrent().getNavigator().navigateTo(viewName);
    }
}
