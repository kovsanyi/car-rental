package hu.unideb.inf.carrental.ui.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;

public final class SiteItem extends Panel {

    public SiteItem(SiteResponse siteResponse) {
        this.siteResponse = siteResponse;

        final VerticalLayout root = new VerticalLayout();
        root.setSizeUndefined();

        root.addComponent(buildDetails());
        root.addComponent(buildEditButton());

        setSizeUndefined();
        setContent(root);
        setCaption(siteResponse.getZipCode() + " " + siteResponse.getCity());
    }

    private Component buildDetails() {
        final VerticalLayout details = new VerticalLayout();
        details.setSizeUndefined();
        details.setMargin(false);
        details.setSpacing(false);

        final Label city = new Label(bold("City: ") + siteResponse.getZipCode() + " " + siteResponse.getCity(), ContentMode.HTML);
        final Label address = new Label(bold("Address: ") + siteResponse.getAddress(), ContentMode.HTML);
        final Label openingHours = new Label(bold("Opening hours: ") + siteResponse.getOpeningHours(), ContentMode.HTML);
        final Label email = new Label(bold("Email: ") + siteResponse.getEmail(), ContentMode.HTML);
        final Label phone = new Label(bold("Phone: ") + siteResponse.getPhoneNumber(), ContentMode.HTML);

        details.addComponents(city, address, openingHours, email, phone);
        return details;
    }

    private Component buildEditButton() {
        final Button edit = new Button("Edit");
        edit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        edit.setWidth(100, Unit.PERCENTAGE);
        edit.addClickListener(e -> {
            //TODO code it
        });

        return edit;
    }

    private String bold(String value) {
        return "<b>" + value + "</b>";
    }

    private final SiteResponse siteResponse;
}
