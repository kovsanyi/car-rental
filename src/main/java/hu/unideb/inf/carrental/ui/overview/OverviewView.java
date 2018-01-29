package hu.unideb.inf.carrental.ui.overview;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import hu.unideb.inf.carrental.ui.overview.content.CompanyOverviewContent;

@UIScope
@SpringView(name = OverviewView.VIEW_NAME)
public class OverviewView extends VerticalLayout implements View {

    public OverviewView() {

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);

        addComponent(new CarRentalMenu());

        switch (SecurityUtils.getLoggedInUser().getRole()) {
            case ROLE_COMPANY:
                addComponent(new CompanyOverviewContent());
                break;
            default:
                UIUtils.showNotification("You have no right to see this page!", Notification.Type.WARNING_MESSAGE);
        }

    }

    public static final String VIEW_NAME = "overview";
}
