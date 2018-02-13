package hu.unideb.inf.carrental.ui.sites;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.bar.CompanyBar;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;
import hu.unideb.inf.carrental.ui.sites.content.CompanySitesContent;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = SitesView.VIEW_NAME)
public class SitesView extends VerticalLayout implements View {

    @Autowired
    public SitesView(SiteService siteService) {
        this.siteService = siteService;

        setMargin(false);
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("sites-view");

        if (SecurityUtils.getLoggedInUser().getRole().equals(UserRole.ROLE_COMPANY)) {
            addComponent(new CompanyBar());
            addComponent(new CarRentalMenu());
            addComponent(new CompanySitesContent(siteService));
        } else {
            UIUtils.showNotification("You have no right to see this page!", Notification.Type.WARNING_MESSAGE);
        }
    }

    private final SiteService siteService;

    public static final String VIEW_NAME = "sites";
}
