package hu.unideb.inf.carrental.ui.commons.bar;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;

public class CompanyBar extends VerticalLayout {

    public CompanyBar() {
        setWidth(100.f, Unit.PERCENTAGE);
        setHeight(310.f, Unit.PIXELS);
        setMargin(false);
        setSpacing(false);
        setId("bar");
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("company-bar");

        addComponents(
                buildLogo()
        );
    }

    private AbstractOrderedLayout buildLogo() {
        final VerticalLayout logo = new VerticalLayout();
        logo.setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        logo.setMargin(false);
        logo.setSpacing(false);
        logo.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        final Label title = new Label("Car Rental");
        title.setHeight(100.f, Unit.PERCENTAGE);
        title.setId("logo");

        logo.addComponent(title);
        return logo;
    }
}
