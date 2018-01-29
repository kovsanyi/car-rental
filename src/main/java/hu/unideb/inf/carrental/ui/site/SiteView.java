package hu.unideb.inf.carrental.ui.site;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.site.content.SiteContent;

import java.util.Objects;

@UIScope
@SpringView(name = SiteView.VIEW_NAME)
public class SiteView extends VerticalLayout implements View {

    public SiteView(SiteService siteService) {
        this.siteService = siteService;

        setMargin(new MarginInfo(true, false, false, false));
        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setStyleName("siteview");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();

        if (!Objects.isNull(event.getParameters())) {
            try {
                Long siteID = Long.parseLong(event.getParameterMap().get("id"));
                SiteResponse siteResponse = siteService.getById(siteID);
                siteContent = new SiteContent(siteResponse);

                addComponent(new CarRentalMenu());
                addComponent(siteContent);
            } catch (NumberFormatException | NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private SiteContent siteContent;

    private final SiteService siteService;

    public static final String VIEW_NAME = "site";
}
