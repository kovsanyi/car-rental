package hu.unideb.inf.carrental.ui.cars.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.company.service.CompanyService;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.component.item.CarItem;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;

import java.util.HashMap;
import java.util.Map;

public class CompanyCarsContent extends VerticalLayout {

    public CompanyCarsContent(CarService carService, CarImageService carImageService, CompanyService companyService) {
        this.carService = carService;
        this.carImageService = carImageService;
        this.companyService = companyService;

        setMargin(false);
        setSpacing(false);
        setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        setHeightUndefined();
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponents(
                buildHeader(),
                buildBody()
        );
        return content;
    }

    private AbstractOrderedLayout buildHeader() {
        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setMargin(false);
        headerLayout.setSpacing(true);
        headerLayout.setWidth(100.f, Unit.PERCENTAGE);

        final Button addCar = new Button("Add car");
        addCar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addCar.setIcon(VaadinIcons.PLUS);
        addCar.addClickListener(e ->
                CarRentalEventBus.post(new CarRentalEvent.OpenCarWindowForAddingEvent()));

        headerLayout.addComponents(
                buildTitle(),
                addCar
        );
        headerLayout.setComponentAlignment(addCar, Alignment.MIDDLE_RIGHT);
        return headerLayout;
    }

    private AbstractLayout buildBody() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.setMargin(false);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();
        gridLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        gridLayout.setColumns(1);

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

        carWithCover.entrySet().stream()
                .map(e -> new CarItem(e.getKey(), e.getValue()))
                .forEach(gridLayout::addComponent);

        return gridLayout;
    }

    private Label buildTitle() {
        final Label title = new Label("Cars");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        title.setId("title");

        return title;
    }

    private final CarService carService;
    private final CarImageService carImageService;
    private final CompanyService companyService;
}
