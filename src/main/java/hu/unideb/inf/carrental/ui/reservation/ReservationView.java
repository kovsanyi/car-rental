package hu.unideb.inf.carrental.ui.reservation;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.ui.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.reservation.content.CompanyReservationContent;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = ReservationView.VIEW_NAME)
public class ReservationView extends VerticalLayout implements View {

    @Autowired
    public ReservationView(ReservationService reservationService) {
        this.reservationService = reservationService;

        //setMargin(false);
        //TODO debug
        setMargin(new MarginInfo(true, false, false, false));

        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addStyleName("reservationview");

        addComponent(new CarRentalMenu());

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addComponent(buildCompanyContent());
                break;
            case ROLE_MANAGER:
                //TODO code it
                break;
            case ROLE_CUSTOMER:
                //TODO code it
                break;
            default:

        }
    }

    private AbstractOrderedLayout buildCompanyContent() {
        AbstractOrderedLayout content = new CompanyReservationContent(reservationService);
        return content;
    }

    private final ReservationService reservationService;

    public static final String VIEW_NAME = "reservation";
}
