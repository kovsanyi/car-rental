package hu.unideb.inf.carrental.ui.company.layout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.ui.component.CarItem;

import java.util.List;
import java.util.Map;

public class CarListLayout extends GridLayout {

    public CarListLayout(List<CarResponse> carResponses) {
        setup();
        carResponses.stream().map(e -> new CarItem(e, null)).forEach(this::addComponent);
    }

    public CarListLayout(Map<CarResponse, CarImageResponse> carWithCover) {
        setup();
        carWithCover.entrySet().stream().map(e -> new CarItem(e.getKey(), e.getValue())).forEach(this::addComponent);
    }

    private void setup() {
        setSizeUndefined();
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.TOP_LEFT);
        setColumns(1);
    }
}
