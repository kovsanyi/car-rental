package hu.unideb.inf.carrental.ui.company.layout.car;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.ui.component.item.CarItem;

import java.util.List;
import java.util.Map;

public class CarLayout extends GridLayout {

    public CarLayout(List<CarResponse> carResponses) {
        setup();
        carResponses.stream().map(e -> new CarItem(e, null)).forEach(this::addComponent);
    }

    public CarLayout(Map<CarResponse, CarImageResponse> carWithCover) {
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
