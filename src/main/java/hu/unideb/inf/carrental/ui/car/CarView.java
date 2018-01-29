package hu.unideb.inf.carrental.ui.car;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.ui.car.content.CarContent;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;

import java.util.Collections;
import java.util.Objects;

@UIScope
@SpringView(name = CarView.VIEW_NAME)
public class CarView extends VerticalLayout implements View {

    public CarView(CarService carService, CarImageService carImageService) {
        this.carService = carService;
        this.carImageService = carImageService;

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("carview");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();

        if (!Objects.isNull(event.getParameters())) {
            try {
                Long carID = Long.parseLong(event.getParameterMap().get("id"));
                CarResponse carResponse = carService.getById(carID);
                carContent = new CarContent(carResponse, Collections.singletonList(carImageService.getById(1)));

                addComponent(new CarRentalMenu());
                addComponent(carContent);
            } catch (NotFoundException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private CarContent carContent;

    private final CarService carService;
    private final CarImageService carImageService;

    public static final String VIEW_NAME = "car";
}
