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
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.component.window.CarWindow;
import hu.unideb.inf.carrental.ui.commons.component.window.SiteWindow;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.LogoutRequestEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenCarWindowForAddingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenSiteWindowForAddingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenSiteWindowForEditingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import hu.unideb.inf.carrental.ui.login.LoginView;
import hu.unideb.inf.carrental.ui.overview.OverviewView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.annotation.WebServlet;
import java.util.Locale;

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
            navigator.navigateTo(OverviewView.VIEW_NAME);
        } else {
            navigator.navigateTo(LoginView.VIEW_NAME);
        }
    }

    @Subscribe
    private void openCarWindowForAdding(OpenCarWindowForAddingEvent openCarWindowForAddingEvent) {
        getCurrent().addWindow(new CarWindow(siteService, carService));
    }

    @Subscribe
    private void openSiteWindowForAdding(OpenSiteWindowForAddingEvent openSiteWindowForAddingEvent) {
        getCurrent().addWindow(new SiteWindow(siteService));
    }

    @Subscribe
    private void openSiteWindowForEditing(OpenSiteWindowForEditingEvent openSiteWindowForEditingEvent) {
        getCurrent().addWindow(new SiteWindow(siteService));
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
    public CarRentalUI(SpringViewProvider viewProvider, SiteService siteService, CarService carService, CarImageService carImageService) {
        this.viewProvider = viewProvider;
        this.siteService = siteService;
        this.carService = carService;
        this.carImageService = carImageService;

        this.carRentalEventBus = new CarRentalEventBus();
    }

    private final SpringViewProvider viewProvider;

    private final SiteService siteService;
    private final CarService carService;
    private final CarImageService carImageService;

    private final CarRentalEventBus carRentalEventBus;
}
