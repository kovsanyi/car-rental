package hu.unideb.inf.carrental.ui.statistics;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.statistics.service.StatisticsService;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import hu.unideb.inf.carrental.ui.statistics.company.CompanyStatisticsContent;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = StatisticsView.VIEW_NAME)
public class StatisticsView extends VerticalLayout implements View {

    @Autowired
    public StatisticsView(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("statisticsview");

        addComponent(new CarRentalMenu());

        if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_COMPANY)) {
            addComponent(new CompanyStatisticsContent(statisticsService));
        } else {
            UIUtils.showNotification("You have no right to see this page!", Notification.Type.WARNING_MESSAGE);
        }
    }

    private final StatisticsService statisticsService;

    public static final String VIEW_NAME = "statistics";
}
