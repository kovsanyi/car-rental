package hu.unideb.inf.carrental.ui.car;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.ui.car.content.CompanyCarContent;
import hu.unideb.inf.carrental.ui.car.content.CustomerCarContent;
import hu.unideb.inf.carrental.ui.commons.component.bar.CustomerBar;
import hu.unideb.inf.carrental.ui.commons.component.bar.CompanyBar;
import hu.unideb.inf.carrental.ui.commons.component.menu.CarRentalMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@UIScope
@SpringView(name = CarView.VIEW_NAME)
public class CarView extends VerticalLayout implements View {

    public CarView(CarService carService, CarImageService carImageService) {
        this.carService = carService;
        this.carImageService = carImageService;

        setMargin(false);
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("car-view");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();

        if (!Objects.isNull(event.getParameters())) {
            try {
                Long carID = Long.parseLong(event.getParameterMap().get("id"));
                CarResponse carResponse = carService.getById(carID);
                switch (SecurityUtils.getLoggedInUser().getRole()) {
                    case ROLE_COMPANY:
                        addComponent(new CompanyBar());
                        addComponent(new CarRentalMenu());
                        addComponent(new CompanyCarContent(carResponse, carImageService.getAllByCarId(carID),
                                carService, carImageService));
                        break;
                    case ROLE_CUSTOMER:
                        addComponent(new CustomerBar(carService));
                        addComponent(new CarRentalMenu());
                        addComponent(new CustomerCarContent(carResponse, carImageService.getAllByCarId(carID)));
                        break;
                    case ROLE_MANAGER:
                        addComponent(new CompanyBar());
                        addComponent(new CarRentalMenu());
                        addComponent(new CompanyCarContent(carResponse, carImageService.getAllByCarId(carID),
                                carService, carImageService));
                        break;
                }
            } catch (NotFoundException | NumberFormatException e) {
                LOGGER.warn("Invalid ID in URI, message: {}", e.getMessage());
            }
        }
    }

    private final CarService carService;
    private final CarImageService carImageService;

    public static final String VIEW_NAME = "car";
    private static final Logger LOGGER = LoggerFactory.getLogger(CarView.class);
}
