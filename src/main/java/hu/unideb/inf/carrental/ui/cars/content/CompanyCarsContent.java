package hu.unideb.inf.carrental.ui.cars.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.ui.commons.component.item.CarItem;
import hu.unideb.inf.carrental.ui.commons.content.car.CarsContent;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;

import java.util.HashMap;
import java.util.Map;

public class CompanyCarsContent extends CarsContent {
    public CompanyCarsContent(CarService carService, CarImageService carImageService, CompanyService companyService) {
        super("Cars");
        this.carService = carService;
        this.carImageService = carImageService;
        this.companyService = companyService;

        setupHeader();
        setupBody();
    }

    private void setupHeader() {
        final Button addCar = new Button("Add car");
        addCar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addCar.setIcon(VaadinIcons.PLUS);
        addCar.addClickListener(e ->
                CarRentalEventBus.post(new CarRentalEvent.OpenCarWindowForAddingEvent()));

        getHeader().addComponent(addCar);
        getHeader().setComponentAlignment(addCar, Alignment.MIDDLE_RIGHT);
    }

    private void setupBody() {
        Map<CarResponse, CarImageResponse> carWithCover = new HashMap<>();
        CarImageResponse carImageResponse;

        try {
            for (CarResponse carResponse : carService.getByCompanyId(companyService.get().getId())) {
                try {
                    carImageResponse = carImageService.getCoverByCarId(carResponse.getId());
                } catch (NotFoundException e) {
                    carImageResponse = null;
                }
                carWithCover.put(carResponse, carImageResponse);
            }
        } catch (NotFoundException e) {
            UIUtils.showNotification(e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

        carWithCover.entrySet().stream()
                .map(e -> new CarItem(e.getKey(), e.getValue()))
                .forEach(getBody()::addComponent);
    }

    private final CarService carService;
    private final CarImageService carImageService;
    private final CompanyService companyService;
}
