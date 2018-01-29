package hu.unideb.inf.carrental.ui.cars;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.ui.cars.content.CompanyCarsContent;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = CarsView.VIEW_NAME)
public class CarsView extends VerticalLayout implements View {

    @Autowired
    public CarsView(CarService carService, CarImageService carImageService, CompanyService companyService) {
        this.carService = carService;
        this.carImageService = carImageService;
        this.companyService = companyService;

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addStyleName("carsview");

        addComponent(new CarRentalMenu());

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addComponent(new CompanyCarsContent(carService, carImageService, companyService));
                break;
            default:
                UIUtils.showNotification("You have no right to see this page!", Notification.Type.WARNING_MESSAGE);
        }
    }

    private final CarService carService;
    private final CarImageService carImageService;
    private final CompanyService companyService;

    public final static String VIEW_NAME = "cars";
}
