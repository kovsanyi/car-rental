package hu.unideb.inf.carrental.ui.company.layout.site;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.ui.component.item.SiteItem;

import java.util.List;

public class SiteLayout extends GridLayout {

    public SiteLayout(List<SiteResponse> siteResponses) {

        setSizeUndefined();
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.TOP_LEFT);
        setColumns(3);

        siteResponses.stream().map(SiteItem::new).forEach(this::addComponent);
    }
}
