package hu.unideb.inf.carrental.ui.site;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.manager.service.ManagerService;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.component.bar.CompanyBar;
import hu.unideb.inf.carrental.ui.commons.component.bar.CustomerBar;
import hu.unideb.inf.carrental.ui.commons.component.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.content.site.SiteContent;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import hu.unideb.inf.carrental.ui.site.content.CompanySiteContent;

import java.util.Objects;

@UIScope
@SpringView(name = SiteView.VIEW_NAME)
public class SiteView extends VerticalLayout implements View {

    public SiteView(SiteService siteService, CarService carService, ManagerService managerService) {
        this.siteService = siteService;
        this.carService = carService;
        this.managerService = managerService;

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

        if (Objects.nonNull(event.getParameters())) {


            UserRole userRole = SecurityUtils.getLoggedInUser().getRole();
            if (userRole.equals(UserRole.ROLE_COMPANY)) {
                try {
                    Long siteID = Long.parseLong(event.getParameterMap().get("id"));
                    SiteResponse siteResponse = siteService.getById(siteID);

                    addComponent(new CompanyBar());
                    addComponent(new CarRentalMenu());
                    addComponent(new CompanySiteContent(siteResponse, siteService, managerService));
                } catch (NumberFormatException | NotFoundException e) {
                    UIUtils.showNotification(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            } else if (userRole.equals(UserRole.ROLE_MANAGER)) {
                addComponent(new CompanyBar());
                addComponent(new CarRentalMenu());
                try {
                    SiteResponse siteResponse = siteService.getByManager();
                    addComponent(new SiteContent(siteResponse));
                } catch (NotFoundException e) {
                    UIUtils.showNotification("Warning: " + e.getMessage(), Notification.Type.WARNING_MESSAGE);
                }
            } else if (userRole.equals(UserRole.ROLE_CUSTOMER)) {
                try {
                    Long siteID = Long.parseLong(event.getParameterMap().get("id"));
                    SiteResponse siteResponse = siteService.getById(siteID);

                    addComponent(new CustomerBar(carService));
                    addComponent(new CarRentalMenu());
                    addComponent(new SiteContent(siteResponse));
                } catch (NumberFormatException | NotFoundException e) {
                    UIUtils.showNotification(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        }
    }

    private final SiteService siteService;
    private final CarService carService;
    private final ManagerService managerService;

    public static final String VIEW_NAME = "site";
}
