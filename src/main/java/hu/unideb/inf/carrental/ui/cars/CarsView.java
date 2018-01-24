package hu.unideb.inf.carrental.ui.cars;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.ui.cars.content.CompanyCarsContent;
import hu.unideb.inf.carrental.ui.menu.CarRentalMenu;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = CarsView.VIEW_NAME)
public class CarsView extends VerticalLayout implements View {

    @Autowired
    public CarsView(CarService carService, CarImageService carImageService, CompanyService companyService) {
        this.carService = carService;
        this.carImageService = carImageService;
        this.companyService = companyService;

        //TODO debug
        //setMargin(false);
        setMargin(new MarginInfo(true, false, false, false));

        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addStyleName("carsview");

        addComponent(new CarRentalMenu());
        addComponent(buildContent());
    }

    private AbstractLayout buildContent() {
        AbstractLayout content = new AbsoluteLayout();

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                content = new CompanyCarsContent(carService, carImageService, companyService);
        }

        return content;
    }

    private final CarService carService;
    private final CarImageService carImageService;
    private final CompanyService companyService;

    public final static String VIEW_NAME = "cars";
}
