package hu.unideb.inf.carrental.ui.site.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.manager.service.ManagerService;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.content.site.SiteContent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;


public class CompanySiteContent extends SiteContent {

    public CompanySiteContent(SiteResponse siteResponse, SiteService siteService, ManagerService managerService) {
        super(siteResponse);
        this.siteService = siteService;
        this.managerService = managerService;

        setupHeader();
    }

    private void setupHeader() {
        final Button setManager = new Button("Set manager",
                e -> CarRentalEventBus.post(new CarRentalEvent.OpenManagerWindow(getSiteResponse().getId())));
        setManager.addStyleName(ValoTheme.BUTTON_PRIMARY);
        setManager.setIcon(VaadinIcons.USER);

        getHeader().addComponent(setManager);
        getHeader().setComponentAlignment(setManager, Alignment.MIDDLE_RIGHT);
    }

    private final SiteService siteService;
    private final ManagerService managerService;
}
