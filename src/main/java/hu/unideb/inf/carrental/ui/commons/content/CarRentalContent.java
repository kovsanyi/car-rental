package hu.unideb.inf.carrental.ui.commons.content;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;

public abstract class CarRentalContent extends VerticalLayout {
    protected CarRentalContent(String titleValue) {
        this.titleValue = titleValue;

        setMargin(false);
        setSpacing(false);
        setWidth(Constants.Size.WIDTH, Unit.PIXELS);
        setHeightUndefined();
        setStyleName("carrental-content");

        addComponent(buildContent());
    }

    private VerticalLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        header = buildHeader();
        body = buildBody();
        body.setId("body");

        content.addComponents(
                header,
                body
        );
        return content;
    }

    private HorizontalLayout buildHeader() {
        final HorizontalLayout header = new HorizontalLayout();
        header.setMargin(false);
        header.setSpacing(false);
        header.setWidth(100.f, Unit.PERCENTAGE);
        header.setHeightUndefined();

        header.addComponents(
                buildTitle()
        );
        return header;
    }

    protected abstract AbstractLayout buildBody();

    private Label buildTitle() {
        final Label title = new Label(titleValue);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        title.setId("title");
        return title;
    }

    protected VerticalLayout buildDefaultVerticalBody() {
        final VerticalLayout body = new VerticalLayout();
        body.setMargin(false);
        body.setSpacing(false);
        body.setSizeFull();
        return body;
    }

    protected HorizontalLayout buildDefaultHorizontalBody() {
        final HorizontalLayout body = new HorizontalLayout();
        body.setMargin(false);
        body.setSpacing(false);
        body.setSizeFull();
        return body;
    }

    protected HorizontalLayout getHeader() {
        return header;
    }

    protected AbstractLayout getBody() {
        return body;
    }

    private HorizontalLayout header;
    private AbstractLayout body;

    private final String titleValue;
}
