package hu.unideb.inf.carrental.ui.company;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.statistics.service.StatisticsService;
import hu.unideb.inf.carrental.ui.company.layout.car.CarLayout;
import hu.unideb.inf.carrental.ui.company.layout.reservation.ReservationLayout;
import hu.unideb.inf.carrental.ui.company.layout.site.SiteLayout;
import hu.unideb.inf.carrental.ui.company.layout.statistics.StatisticsLayout;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenCarWindowForAddingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent.OpenSiteWindowForAddingEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@UIScope
@SpringView(name = CompanyView.VIEW_NAME)
public class CompanyView extends VerticalLayout implements View {

    @Autowired
    public CompanyView(CompanyService companyService, SiteService siteService, CarService carService,
                       CarImageService carImageService, StatisticsService statisticsService,
                       ReservationService reservationService) {
        this.companyService = companyService;
        this.siteService = siteService;
        this.carService = carService;
        this.carImageService = carImageService;
        this.statisticsService = statisticsService;
        this.reservationService = reservationService;

        setDefaultComponentAlignment(Alignment.TOP_CENTER);

        final VerticalLayout wrapper = new VerticalLayout();
        wrapper.setMargin(false);
        wrapper.setWidth(1366.f, Unit.PIXELS);
        wrapper.setHeightUndefined();
        wrapper.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        wrapper.addComponent(buildContent());
        addComponent(wrapper);

        displayCarList();
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponent(buildMenu());
        content.addComponent(buildBody());
        content.setComponentAlignment(body, Alignment.TOP_CENTER);

        return content;
    }

    private Component buildMenu() {
        final MenuBar menu = new MenuBar();
        menu.setWidth(100.f, Unit.PERCENTAGE);

        final MenuBar.MenuItem overview = menu.addItem("Overview",
                e -> displayOverview());
        overview.setIcon(VaadinIcons.HOME);

        final MenuBar.MenuItem cars = menu.addItem("Cars", null);
        final MenuBar.MenuItem displayCars = cars.addItem("Display all",
                e -> displayCarList());
        final MenuBar.MenuItem addCar = cars.addItem("Add car",
                e -> CarRentalEventBus.post(new OpenCarWindowForAddingEvent()));

        final MenuBar.MenuItem sites = menu.addItem("Sites", null);
        final MenuBar.MenuItem displaySites = sites.addItem("Display all",
                e -> displaySiteList());
        final MenuBar.MenuItem addSite = sites.addItem("Add site",
                e -> CarRentalEventBus.post(new OpenSiteWindowForAddingEvent()));

        final MenuBar.MenuItem reservations = menu.addItem("Reservations",
                e -> displayReservations());

        final MenuBar.MenuItem statistics = menu.addItem("Statistics",
                e -> displayStatistics());

        final MenuBar.MenuItem logout = menu.addItem("Logout",
                e -> CarRentalEventBus.post(new CarRentalEvent.LogoutRequestEvent()));
        logout.setIcon(VaadinIcons.EXIT);

        return menu;
    }

    private AbstractOrderedLayout buildBody() {
        body = new VerticalLayout();
        body.setMargin(false);
        body.setSpacing(false);
        body.setWidth(100.f, Unit.PERCENTAGE);
        body.setWidthUndefined();
        //body.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        return body;
    }

    private void displayOverview() {
        body.removeAllComponents();
        //TODO
    }

    private void displayCarList() {
        body.removeAllComponents();

        Map<CarResponse, CarImageResponse> carWithCover = new HashMap<>();
        CarImageResponse carImageResponse;
        for (CarResponse carResponse : carService.getByCompanyId(companyService.get().getId())) {
            try {
                carImageResponse = carImageService.getCoverByCarId(carResponse.getId());
            } catch (NotFoundException e) {
                carImageResponse = null;
            }
            carWithCover.put(carResponse, carImageResponse);
        }

        body.addComponent(new CarLayout(carWithCover));
    }

    private void displaySiteList() {
        body.removeAllComponents();
        body.addComponent(new SiteLayout(siteService.getByCompany()));
    }

    private void displayReservations() {
        body.removeAllComponents();
        body.addComponent(new ReservationLayout(reservationService));
    }

    private void displayStatistics() {
        body.removeAllComponents();
        body.addComponent(new StatisticsLayout(statisticsService));
    }

    private AbstractOrderedLayout body;

    private final CompanyService companyService;
    private final SiteService siteService;
    private final CarService carService;
    private final CarImageService carImageService;
    private final StatisticsService statisticsService;
    private final ReservationService reservationService;

    public static final String VIEW_NAME = "company";
}
