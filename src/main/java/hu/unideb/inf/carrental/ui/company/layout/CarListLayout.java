package hu.unideb.inf.carrental.ui.company.layout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.ui.component.CarItem;

import java.util.List;

public class CarListLayout extends GridLayout {

    public CarListLayout(List<CarResponse> carResponses) {

        setSizeUndefined();
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.TOP_LEFT);

        carResponses.stream().map(CarItem::new).forEach(this::addComponent);
    }
}
