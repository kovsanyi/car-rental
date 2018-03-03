package hu.unideb.inf.carrental.ui.cars;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.cars.content.CompanyCarsContent;
import hu.unideb.inf.carrental.ui.cars.content.ManagerCarsContent;
import hu.unideb.inf.carrental.ui.commons.component.bar.CompanyBar;
import hu.unideb.inf.carrental.ui.commons.component.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = CarsView.VIEW_NAME)
public class CarsView extends VerticalLayout implements View {

    @Autowired
    public CarsView(CarService carService, CarImageService carImageService, CompanyService companyService,
                    SiteService siteService) {
        this.carService = carService;
        this.carImageService = carImageService;
        this.companyService = companyService;
        this.siteService = siteService;

        setMargin(false);
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addStyleName("cars-view");

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addComponent(new CompanyBar());
                addComponent(new CarRentalMenu());
                addComponent(new CompanyCarsContent(carService, carImageService, companyService));
                break;
            case ROLE_MANAGER:
                addComponent(new CompanyBar());
                addComponent(new CarRentalMenu());
                addComponent(new ManagerCarsContent(siteService, carService, carImageService));
                break;
            default:
                UIUtils.showNotification("You have no right to see this page!",
                        Notification.Type.WARNING_MESSAGE);
        }
    }

    private final CarService carService;
    private final CarImageService carImageService;
    private final CompanyService companyService;
    private final SiteService siteService;

    public final static String VIEW_NAME = "cars";
}
