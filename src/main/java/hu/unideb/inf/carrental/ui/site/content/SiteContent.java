package hu.unideb.inf.carrental.ui.site.content;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.ui.commons.content.root.CarRentalContent;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.buildKeyLabel;
import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.buildValueLabel;

public class SiteContent extends CarRentalContent {
    public SiteContent(SiteResponse siteResponse) {
        super(String.format("%s - %s %s", "Site", siteResponse.getZipCode(), siteResponse.getCity()));
        this.siteResponse = siteResponse;

        setupBody();
    }

    private void setupBody() {
        getBody().addComponents(
                buildDetails()
        );
    }

    private GridLayout buildDetails() {
        final GridLayout details = new GridLayout(2, 8);
        details.setMargin(false);
        details.setSpacing(false);
        details.setWidthUndefined();
        details.setHeightUndefined();

        final Label keyZipCode = buildKeyLabel("Zip code:");
        final Label keyCity = buildKeyLabel("City:");
        final Label keyAddress = buildKeyLabel("Address:");
        final Label keyOpeningHours = buildKeyLabel("Opening hours:");
        final Label keyPhoneNumber = buildKeyLabel("Phone number:");
        final Label keyEmail = buildKeyLabel("Email:");

        final Label valueZipCode = buildValueLabel(siteResponse.getZipCode().toString());
        final Label valueCity = buildValueLabel(siteResponse.getCity());
        final Label valueAddress = buildValueLabel(siteResponse.getAddress());
        final Label valueOpeningHours = buildValueLabel(siteResponse.getOpeningHours());
        final Label valuePhoneNumber = buildValueLabel(siteResponse.getPhoneNumber());
        final Label valueEmail = buildValueLabel(siteResponse.getEmail());

        details.addComponents(
                keyZipCode, valueZipCode,
                keyCity, valueCity,
                keyAddress, valueAddress,
                keyOpeningHours, valueOpeningHours,
                keyPhoneNumber, valuePhoneNumber,
                keyEmail, valueEmail
        );
        return details;
    }

    @Override
    protected AbstractLayout buildBody() {
        return buildDefaultHorizontalBody();
    }

    private final SiteResponse siteResponse;
}
