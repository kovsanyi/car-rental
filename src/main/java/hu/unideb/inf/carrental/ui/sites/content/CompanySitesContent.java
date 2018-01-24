package hu.unideb.inf.carrental.ui.sites.content;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.site.service.SiteService;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.component.item.SiteItem;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;

public class CompanySitesContent extends VerticalLayout {

    public CompanySitesContent(SiteService siteService) {
        this.siteService = siteService;

        setMargin(false);
        setSpacing(false);
        setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        setHeightUndefined();
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponents(
                buildHeader(),
                buildBody()
        );
        return content;
    }

    private AbstractOrderedLayout buildHeader() {
        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setMargin(false);
        headerLayout.setSpacing(false);
        headerLayout.setWidth(100.f, Unit.PERCENTAGE);

        final Button addSite = new Button("Add site");
        addSite.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addSite.setIcon(VaadinIcons.PLUS);
        addSite.addClickListener(e ->
                CarRentalEventBus.post(new CarRentalEvent.OpenSiteWindowForAddingEvent()));

        headerLayout.addComponents(
                buildTitle(),
                addSite
        );
        headerLayout.setComponentAlignment(addSite, Alignment.MIDDLE_RIGHT);
        return headerLayout;
    }

    private AbstractLayout buildBody() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.setMargin(false);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();
        gridLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        gridLayout.setColumns(3);

        siteService.getByCompany().stream().map(SiteItem::new)
                .forEach(gridLayout::addComponent);

        return gridLayout;
    }

    private Label buildTitle() {
        final Label title = new Label("Sites");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        title.setId("title");

        return title;
    }

    private final SiteService siteService;
}
