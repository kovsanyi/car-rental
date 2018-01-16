package hu.unideb.inf.carrental.ui.company;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.company.component.AddCarWindow;
import hu.unideb.inf.carrental.ui.company.component.AddSiteWindow;
import hu.unideb.inf.carrental.ui.company.layout.CarListLayout;
import hu.unideb.inf.carrental.ui.company.layout.SiteListLayout;

@UIScope
@SpringView(name = CompanyView.VIEW_NAME)
public class CompanyView extends VerticalLayout implements View {

    public CompanyView(CompanyService companyService, SiteService siteService, CarService carService) {
        this.companyService = companyService;
        this.siteService = siteService;
        this.carService = carService;

        buildLayout();
    }

    private void buildLayout() {
        setWidth(100, Unit.PERCENTAGE);

        body = new VerticalLayout();
        body.setSizeUndefined();
        body.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        addComponent(buildMenu());
        addComponent(body);

        loadCarList();
    }

    private Component buildMenu() {
        final MenuBar menu = new MenuBar();
        menu.setWidth(100, Unit.PERCENTAGE);

        final MenuBar.MenuItem cars = menu.addItem("Cars", null);
        final MenuBar.MenuItem displayCars = cars.addItem("Display all", e -> loadCarList());
        final MenuBar.MenuItem addCar = cars.addItem("Add car", e -> showNewCarWindow());

        final MenuBar.MenuItem sites = menu.addItem("Sites", null);
        final MenuBar.MenuItem displaySites = sites.addItem("Display all", e -> loadSiteList());
        final MenuBar.MenuItem addSite = sites.addItem("Add site", e -> showNewSiteWindow());

        final MenuBar.MenuItem info = menu.addItem("Company information", e -> loadCompanyInfo());
        final MenuBar.MenuItem statistics = menu.addItem("Statistics", e -> loadStatistics());
        final MenuBar.MenuItem logout = menu.addItem("Logout", e -> {
            CarRentalUI.logout();
            CarRentalUI.getCurrent().getPage().reload();
        });
        logout.setIcon(VaadinIcons.EXIT);

        return menu;
    }

    private void showNewCarWindow() {
        CarRentalUI.getCurrent().addWindow(new AddCarWindow(siteService, carService));
    }

    private void showNewSiteWindow() {
        CarRentalUI.getCurrent().addWindow(new AddSiteWindow(siteService));
    }

    private void loadCarList() {
        body.removeAllComponents();
        body.addComponent(new CarListLayout(carService.getByCompanyId(companyService.get().getId())));
    }

    private void loadSiteList() {
        body.removeAllComponents();
        body.addComponent(new SiteListLayout(siteService.getByCompany()));
    }

    private void loadCompanyInfo() {
        body.removeAllComponents();
    }

    private void loadStatistics() {
        body.removeAllComponents();
    }

    private VerticalLayout body;

    private final CompanyService companyService;
    private final SiteService siteService;
    private final CarService carService;

    public static final String VIEW_NAME = "company_view";
}
