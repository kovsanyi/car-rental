package hu.unideb.inf.carrental.ui.reservations;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.component.bar.CompanyBar;
import hu.unideb.inf.carrental.ui.commons.component.bar.CustomerBar;
import hu.unideb.inf.carrental.ui.commons.component.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.reservations.content.CompanyReservationsContent;
import hu.unideb.inf.carrental.ui.reservations.content.CustomerReservationsContent;
import hu.unideb.inf.carrental.ui.reservations.content.ManagerReservationContent;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = ReservationView.VIEW_NAME)
public class ReservationView extends VerticalLayout implements View {

    @Autowired
    public ReservationView(ReservationService reservationService, SiteService siteService, CarService carService) {

        setMargin(false);
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("reservation-view");

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addComponent(new CompanyBar());
                addComponent(new CarRentalMenu());
                addComponent(new CompanyReservationsContent(reservationService));
                break;
            case ROLE_MANAGER:
                addComponent(new CompanyBar());
                addComponent(new CarRentalMenu());
                addComponent(new ManagerReservationContent(reservationService, siteService));
                break;
            case ROLE_CUSTOMER:
                addComponent(new CustomerBar());
                addComponent(new CarRentalMenu());
                addComponent(new CustomerReservationsContent(reservationService));
                break;
        }
    }

    public static final String VIEW_NAME = "reservation";
}
