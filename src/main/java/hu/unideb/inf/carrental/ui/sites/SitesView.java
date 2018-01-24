package hu.unideb.inf.carrental.ui.sites;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.sites.content.CompanySitesContent;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringView(name = SitesView.VIEW_NAME)
public class SitesView extends VerticalLayout implements View {

    @Autowired
    public SitesView(SiteService siteService) {
        this.siteService = siteService;

        //setMargin(false);
        //TODO debug
        setMargin(new MarginInfo(true, false, false, false));

        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        addStyleName("sitesview");

        addComponent(new CarRentalMenu());
        addComponent(buildContent());
    }

    private AbstractLayout buildContent() {
        AbstractOrderedLayout content = new CompanySitesContent(siteService);
        return content;
    }


    private final SiteService siteService;

    public static final String VIEW_NAME = "sites";
}
