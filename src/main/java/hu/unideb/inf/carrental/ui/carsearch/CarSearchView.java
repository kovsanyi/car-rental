package hu.unideb.inf.carrental.ui.carsearch;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.ui.carsearch.content.CarSearchContent;
import hu.unideb.inf.carrental.ui.commons.component.bar.CustomerBar;
import hu.unideb.inf.carrental.ui.commons.component.menu.CarRentalMenu;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = CarSearchView.VIEW_NAME)
public class CarSearchView extends VerticalLayout implements View {

    @Autowired
    public CarSearchView(CarService carService, CarImageService carImageService) {
        this.carService = carService;
        this.carImageService = carImageService;

        setMargin(false);
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addStyleName("cars-view");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();

        if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_CUSTOMER)) {
            addComponent(new CustomerBar());
            addComponent(new CarRentalMenu());

            CarSearchContent carSearchContent = new CarSearchContent(carService, carImageService);
            addComponent(carSearchContent);
        }
    }

    private final CarService carService;
    private final CarImageService carImageService;

    public static final String VIEW_NAME = "search";
}
