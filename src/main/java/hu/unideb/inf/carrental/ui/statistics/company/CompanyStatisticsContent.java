package hu.unideb.inf.carrental.ui.statistics.company;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.statistics.service.StatisticsService;

public class CompanyStatisticsContent extends VerticalLayout {

    public CompanyStatisticsContent(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;

        setMargin(false);
        setSpacing(false);
        setWidth(1366.f, Unit.PIXELS);
        setHeightUndefined();
        addComponent(buildContent());
        addStyleName("statisticsview");
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        content.addComponents(
                buildTitle()
        );

        return content;
    }

    private Label buildTitle() {
        final Label title = new Label("Statistics");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        title.setId("title");

        return title;
    }


    private final StatisticsService statisticsService;
}
