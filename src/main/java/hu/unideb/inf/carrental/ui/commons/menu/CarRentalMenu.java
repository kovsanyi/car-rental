package hu.unideb.inf.carrental.ui.commons.menu;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.MenuBar;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.cars.CarsView;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import hu.unideb.inf.carrental.ui.overview.OverviewView;
import hu.unideb.inf.carrental.ui.reservations.ReservationsView;
import hu.unideb.inf.carrental.ui.site.SiteView;
import hu.unideb.inf.carrental.ui.sites.SitesView;
import hu.unideb.inf.carrental.ui.statistics.StatisticsView;

public class CarRentalMenu extends MenuBar {

    public CarRentalMenu() {
        setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        setHeightUndefined();
        buildMenuBar();
    }

    private void buildMenuBar() {
        if (SecurityUtils.isLoggedIn().equals(Boolean.FALSE)) {
            return;
        }
        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addItem("Overview", VaadinIcons.HOME, e -> navigate(OverviewView.VIEW_NAME));
                addItem("Cars", e -> navigate(CarsView.VIEW_NAME));
                addItem("Sites", e -> navigate(SitesView.VIEW_NAME));
                addItem("Reservations", e -> navigate(ReservationsView.VIEW_NAME));
                addItem("Statistics", e -> navigate(StatisticsView.VIEW_NAME));
                addItem("Logout", VaadinIcons.EXIT, e -> CarRentalEventBus.post(new CarRentalEvent.LogoutRequestEvent()));
                break;
            case ROLE_MANAGER:
                addItem("Overview", VaadinIcons.HOME, e -> navigate(OverviewView.VIEW_NAME));
                addItem("Cars", e -> navigate(CarsView.VIEW_NAME));
                //TODO code it
                addItem("Site", e -> navigate(SiteView.VIEW_NAME));
                addItem("Reservations", e -> navigate(ReservationsView.VIEW_NAME));
                addItem("Logout", VaadinIcons.EXIT, e -> CarRentalEventBus.post(new CarRentalEvent.LogoutRequestEvent()));
                break;
            case ROLE_CUSTOMER:
                //TODO code it
                addItem("Logout", VaadinIcons.EXIT, e -> CarRentalEventBus.post(new CarRentalEvent.LogoutRequestEvent()));
                break;
        }
    }

    private void navigate(String viewName) {
        CarRentalUI.getCurrent().getNavigator().navigateTo(viewName);
    }
}
