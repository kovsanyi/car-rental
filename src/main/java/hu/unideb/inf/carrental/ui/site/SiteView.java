package hu.unideb.inf.carrental.ui.site;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.bar.CompanyBar;
import hu.unideb.inf.carrental.ui.commons.bar.CustomerBar;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.site.content.SiteContent;

import java.util.Objects;

@UIScope
@SpringView(name = SiteView.VIEW_NAME)
public class SiteView extends VerticalLayout implements View {

    public SiteView(SiteService siteService, CarService carService) {
        this.siteService = siteService;
        this.carService = carService;

        setMargin(false);
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("site-view");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();

        if (!Objects.isNull(event.getParameters())) {
            try {
                Long siteID = Long.parseLong(event.getParameterMap().get("id"));
                SiteResponse siteResponse = siteService.getById(siteID);


                switch (SecurityUtils.getLoggedInUser().getRole()) {
                    case ROLE_COMPANY:
                    case ROLE_MANAGER:
                        addComponent(new CompanyBar());
                        addComponent(new CarRentalMenu());
                        addComponent(new SiteContent(siteResponse));
                        break;
                    case ROLE_CUSTOMER:
                        addComponent(new CustomerBar(carService));
                        addComponent(new CarRentalMenu());
                        addComponent(new SiteContent(siteResponse));
                        break;
                }
            } catch (NumberFormatException | NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private final SiteService siteService;
    private final CarService carService;

    public static final String VIEW_NAME = "site";
}
