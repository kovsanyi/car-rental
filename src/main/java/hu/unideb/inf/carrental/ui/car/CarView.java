package hu.unideb.inf.carrental.ui.car;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = CarView.VIEW_NAME)
public class CarView extends VerticalLayout implements View {

    public CarView() {
        setMargin(false);
        setSpacing(false);
        setWidth(1366.f, Unit.PIXELS);
        setHeightUndefined();

        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(false);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        return content;
    }

    public static final String VIEW_NAME = "car";
}
