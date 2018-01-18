package hu.unideb.inf.carrental.ui.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;

public final class SiteItem extends Panel {

    public SiteItem(SiteResponse siteResponse) {
        this.siteResponse = siteResponse;

        setWidth(400.f, Unit.PIXELS);
        setHeightUndefined();
        setCaption(siteResponse.getZipCode() + " " + siteResponse.getCity());

        setContent(buildContent());
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeFull();

        final HorizontalLayout containerOfButton = new HorizontalLayout();
        containerOfButton.setMargin(false);
        containerOfButton.setWidth(100.f, Unit.PERCENTAGE);
        containerOfButton.setHeightUndefined();
        containerOfButton.addComponents(
                buildDetailsButton(),
                buildEditButton()
        );

        content.addComponent(buildDetails());
        content.addComponent(containerOfButton);

        return content;
    }

    private AbstractLayout buildDetails() {
        final HorizontalLayout root = new HorizontalLayout();
        root.setSizeUndefined();

        final VerticalLayout keyLayout = new VerticalLayout();
        keyLayout.setMargin(false);
        keyLayout.setSpacing(false);
        keyLayout.setWidth(80.f, Unit.PIXELS);
        keyLayout.setHeightUndefined();

        final VerticalLayout valueLayout = new VerticalLayout();
        valueLayout.setMargin(false);
        valueLayout.setSpacing(false);
        valueLayout.setSizeUndefined();

        final Label zipCodeKey = new Label(bold("Zip code:"), ContentMode.HTML);
        final Label cityKey = new Label(bold("City:"), ContentMode.HTML);
        final Label addressKey = new Label(bold("Address:"), ContentMode.HTML);
        final Label openingHoursKey = new Label(bold("Open:"), ContentMode.HTML);
        openingHoursKey.setDescription("Opening hours");
        final Label phoneKey = new Label(bold("Phone:"), ContentMode.HTML);
        final Label emailKey = new Label(bold("Email:"), ContentMode.HTML);

        final Label zipCodeValue = new Label(siteResponse.getZipCode().toString());
        final Label cityValue = new Label(siteResponse.getCity());
        final Label addressValue = new Label(siteResponse.getAddress());
        final Label openingHoursValue = new Label(siteResponse.getOpeningHours());
        final Label phoneValue = new Label(siteResponse.getPhoneNumber());
        final Label emailValue = new Label(siteResponse.getEmail());

        keyLayout.addComponents(zipCodeKey, cityKey, addressKey, openingHoursKey, phoneKey, emailKey);
        valueLayout.addComponents(zipCodeValue, cityValue, addressValue, openingHoursValue, phoneValue, emailValue);

        root.addComponents(keyLayout, valueLayout);
        return root;
    }

    private Component buildDetailsButton() {
        final Button details = new Button("Show details");
        details.setWidth(100.f, Unit.PERCENTAGE);
        details.setStyleName(ValoTheme.BUTTON_PRIMARY);

        return details;
    }

    private Component buildEditButton() {
        final Button edit = new Button("Edit site");
        edit.setWidth(100, Unit.PERCENTAGE);
        edit.addClickListener(e -> CarRentalEventBus.post(new CarRentalEvent.OpenSiteWindowForEditingEvent(siteResponse)));
        return edit;
    }

    private String bold(String text) {
        return String.format("<b> %s </b>", text);
    }

    private final SiteResponse siteResponse;
}
