package hu.unideb.inf.carrental.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.company.CompanyView;
import hu.unideb.inf.carrental.ui.company.component.CarWindow;
import hu.unideb.inf.carrental.ui.company.component.SiteWindow;
import hu.unideb.inf.carrental.ui.customer.CustomerView;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenCarWindowForAddingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenSiteWindowForAddingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenSiteWindowForEditingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import hu.unideb.inf.carrental.ui.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

@Title("Car Rental")
@SpringUI
public class CarRentalUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLocale(Locale.US);

        CarRentalEventBus.register(this);

        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        if (SecurityUtils.isLoggedIn()) {
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_CUSTOMER)) {
                navigator.navigateTo(CustomerView.VIEW_NAME);
            }
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_COMPANY)) {
                navigator.navigateTo(CompanyView.VIEW_NAME);
            }
            if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_MANAGER)) {
                navigator.navigateTo(CustomerView.VIEW_NAME);
            }
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

    public static void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        VaadinSession.getCurrent().close();
        getCurrent().getPage().reload();
    }

    public static CarRentalEventBus getCarRentalEventBus() {
        return ((CarRentalUI) getCurrent()).carRentalEventBus;
    }

    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private SiteService siteService;

    @Autowired
    private CarService carService;

    @Autowired
    private CarImageService carImageService;

    private CarRentalEventBus carRentalEventBus = new CarRentalEventBus();
}
