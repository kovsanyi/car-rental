package hu.unideb.inf.carrental.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.manager.service.ManagerService;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.carsearch.CarSearchView;
import hu.unideb.inf.carrental.ui.commons.component.window.CarWindow;
import hu.unideb.inf.carrental.ui.commons.component.window.ManagerWindow;
import hu.unideb.inf.carrental.ui.commons.component.window.ReservationWindow;
import hu.unideb.inf.carrental.ui.commons.component.window.SiteWindow;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.*;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import hu.unideb.inf.carrental.ui.login.LoginView;
import hu.unideb.inf.carrental.ui.reservation.ReservationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.annotation.WebServlet;
import java.util.Locale;
import java.util.Objects;

@Title("Car Rental")
@Theme("carrental")
@SpringUI
public class CarRentalUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = CarRentalUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLocale(Locale.US);
        setStyleName("carrental");

        CarRentalEventBus.register(this);

        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        if (SecurityUtils.isLoggedIn()) {
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_CUSTOMER)) {
                navigator.navigateTo(CarSearchView.VIEW_NAME);
            } else {
                navigator.navigateTo(ReservationView.VIEW_NAME);
            }
        } else {
            navigator.navigateTo(LoginView.VIEW_NAME);
        }
    }

    @Subscribe
    private void openSetManagerWindow(OpenManagerWindow openManagerWindow) {
        getCurrent().addWindow(new ManagerWindow(managerService, siteService, openManagerWindow.getSiteID()));
    }

    @Subscribe
    private void openReservationWindow(OpenReservationWindow openReservationWindow) {
        try {
            CarResponse carResponse = carService.getById(openReservationWindow.getCarID());
            getCurrent().addWindow(new ReservationWindow(
                    carResponse,
                    siteService.getById(carResponse.getSiteId()),
                    reservationService
            ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    private void openCarWindowForAdding(OpenCarWindowForAddingEvent openCarWindowForAddingEvent) {
        getCurrent().addWindow(new CarWindow(openCarWindowForAddingEvent.getType(), siteService, carService));
    }

    @Subscribe
    private void openSiteWindowForAdding(OpenSiteWindowForAddingEvent openSiteWindowForAddingEvent) {
        getCurrent().addWindow(new SiteWindow(siteService));
    }

    @Subscribe
    private void openSiteWindowForEditing(OpenSiteWindowForEditingEvent openSiteWindowForEditingEvent) {
        getCurrent().addWindow(new SiteWindow(siteService, openSiteWindowForEditingEvent.getSiteResponse().getId()));
    }

    @Subscribe
    private void refreshRequest(RefreshRequestEvent refreshRequestEvent) {
        getPage().reload();
        if (Objects.nonNull(refreshRequestEvent.getNavigateTo())) {
            getNavigator().navigateTo(refreshRequestEvent.getNavigateTo());
        }
    }

    @Subscribe
    private void logoutRequest(LogoutRequestEvent logoutRequestEvent) {
        SecurityContextHolder.getContext().setAuthentication(null);
        VaadinSession.getCurrent().close();
        getCurrent().getPage().reload();
    }

    public static CarRentalEventBus getCarRentalEventBus() {
        return ((CarRentalUI) getCurrent()).carRentalEventBus;
    }

    @Autowired
    public CarRentalUI(SpringViewProvider viewProvider, SiteService siteService, CarService carService, CarImageService carImageService, ReservationService reservationService, ManagerService managerService) {
        this.viewProvider = viewProvider;
        this.siteService = siteService;
        this.carService = carService;
        this.carImageService = carImageService;
        this.reservationService = reservationService;
        this.managerService = managerService;

        this.carRentalEventBus = new CarRentalEventBus();
    }

    private final SpringViewProvider viewProvider;

    private final SiteService siteService;
    private final CarService carService;
    private final CarImageService carImageService;
    private final ReservationService reservationService;
    private final ManagerService managerService;
    private final CarRentalEventBus carRentalEventBus;

    private final static long serialVersionUID = 2411746944547008579L;
}
