package hu.unideb.inf.carrental.ui.commons.component.item;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.event.CarRentalEvent;
import hu.unideb.inf.carrental.ui.event.CarRentalEventBus;
import hu.unideb.inf.carrental.ui.site.SiteView;

import static hu.unideb.inf.carrental.ui.commons.util.UIUtils.HTML.bold;

public final class SiteItem extends Panel {

    public SiteItem(SiteResponse siteResponse) {
        this.siteResponse = siteResponse;

        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setCaption(siteResponse.getZipCode() + " " + siteResponse.getCity());
        addStyleName("siteitem");

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
        final GridLayout details = new GridLayout(2, 6);
        details.setMargin(false);
        details.setSpacing(false);
        details.setSizeUndefined();

        final Label keyZipCode = buildKeyLabel("Zip code:");
        final Label keyCity = buildKeyLabel("City:");
        final Label keyAddress = buildKeyLabel("Address:");
        final Label keyOpeningHours = buildKeyLabel("Open:");
        keyOpeningHours.setDescription("Opening hours");
        final Label keyPhoneNumber = buildKeyLabel("Phone:");
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

    private Component buildDetailsButton() {
        final Button details = new Button("Details");
        details.setWidth(100.f, Unit.PERCENTAGE);
        details.setIcon(VaadinIcons.INFO_CIRCLE_O);
        details.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        details.addClickListener(e ->
                CarRentalUI.getCurrent().getPage().open("/#!" + SiteView.VIEW_NAME + "/id=" + siteResponse.getId(), "")
        );
        return details;
    }

    private Component buildEditButton() {
        final Button edit = new Button("Edit");
        edit.setWidth(100, Unit.PERCENTAGE);
        edit.setIcon(VaadinIcons.EDIT);
        edit.addClickListener(e ->
                CarRentalEventBus.post(new CarRentalEvent.OpenSiteWindowForEditingEvent(siteResponse))
        );
        return edit;
    }

    private Label buildKeyLabel(String text) {
        final Label label = new Label(bold(text), ContentMode.HTML);
        label.setId("key");
        return label;
    }

    private Label buildValueLabel(String text) {
        final Label label = new Label(text);
        label.setId("value");
        return label;
    }

    private final SiteResponse siteResponse;
}
