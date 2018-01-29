package hu.unideb.inf.carrental.ui.reservations;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.reservation.service.ReservationService;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import hu.unideb.inf.carrental.ui.reservations.content.CompanyReservationsContent;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = ReservationsView.VIEW_NAME)
public class ReservationsView extends VerticalLayout implements View {

    @Autowired
    public ReservationsView(ReservationService reservationService) {
        this.reservationService = reservationService;

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("reservationsview");

        addComponent(new CarRentalMenu());

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addComponent(new CompanyReservationsContent(reservationService));
                break;
            case ROLE_MANAGER:
                //TODO code it
                break;
            case ROLE_CUSTOMER:
                //TODO code it
                break;
            default:
                UIUtils.showNotification("You have no right to see this page!", Notification.Type.WARNING_MESSAGE);
        }
    }

    private final ReservationService reservationService;

    public static final String VIEW_NAME = "reservation";
}
