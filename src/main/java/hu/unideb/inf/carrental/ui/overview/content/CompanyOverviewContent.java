package hu.unideb.inf.carrental.ui.overview.content;

import com.vaadin.ui.AbstractLayout;
import hu.unideb.inf.carrental.ui.commons.content.CarRentalContent;

public class CompanyOverviewContent extends CarRentalContent {
    public CompanyOverviewContent() {
        super("Overview");

    }

    @Override
    protected AbstractLayout buildBody() {
        return buildDefaultVerticalBody();
    }
}
