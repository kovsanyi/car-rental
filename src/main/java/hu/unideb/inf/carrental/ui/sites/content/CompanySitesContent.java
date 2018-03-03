package hu.unideb.inf.carrental.ui.sites.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.component.item.SiteItem;
import hu.unideb.inf.carrental.ui.commons.content.root.CarRentalContent;
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

        if (siteService.getByCompany().isEmpty()) {
            Label noCars = new Label("No sites!");
            noCars.addStyleName(ValoTheme.LABEL_H1);

            ((GridLayout) getBody()).setColumns(1);
            getBody().addComponent(noCars);
        }
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
