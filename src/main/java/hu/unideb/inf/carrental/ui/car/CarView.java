package hu.unideb.inf.carrental.ui.car;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = CarView.VIEW_NAME)
public class CarView extends VerticalLayout implements View {

    public CarView() {
    }

    public static final String VIEW_NAME = "car";
}
