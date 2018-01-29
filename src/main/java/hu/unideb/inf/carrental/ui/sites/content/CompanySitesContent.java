package hu.unideb.inf.carrental.ui.sites.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.component.item.SiteItem;
import hu.unideb.inf.carrental.ui.commons.content.CarRentalContent;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;

public class CompanySitesContent extends CarRentalContent {
    public CompanySitesContent(SiteService siteService) {
        super("Sites");
        this.siteService = siteService;

        setupHeader();
        setupBody();
    }

    private void setupHeader() {
        final Button addSite = new Button("Add site");
        addSite.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addSite.setIcon(VaadinIcons.PLUS);
        addSite.addClickListener(e ->
                CarRentalEventBus.post(new CarRentalEvent.OpenSiteWindowForAddingEvent())
        );

        getHeader().addComponent(addSite);
        getHeader().setComponentAlignment(addSite, Alignment.MIDDLE_RIGHT);
    }

    private void setupBody() {
        siteService.getByCompany().stream()
                .map(SiteItem::new)
                .forEach(getBody()::addComponent);
    }

    @Override
    protected AbstractLayout buildBody() {
        final GridLayout body = new GridLayout();
        body.setMargin(false);
        body.setSpacing(true);
        body.setSizeFull();
        body.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        body.setColumns(4);
        return body;
    }

    private final SiteService siteService;
}
