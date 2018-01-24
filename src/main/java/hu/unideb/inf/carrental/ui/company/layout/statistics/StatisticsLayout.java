package hu.unideb.inf.carrental.ui.company.layout.statistics;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.statistics.service.StatisticsService;
import hu.unideb.inf.carrental.ui.company.layout.statistics.element.IncomeByBrandLayout;

public class StatisticsLayout extends VerticalLayout {

    public StatisticsLayout(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;

        setMargin(false);
        setSpacing(false);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(false);
        content.setSpacing(true);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();

        final Accordion accordion = new Accordion();
        accordion.setWidth(100.f, Unit.PERCENTAGE);
        accordion.setHeightUndefined();
        accordion.addTab(new IncomeByBrandLayout(), "Income by brand");

        final Label title = new Label("Statistics");
        title.setStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        content.addComponents(
                title,
                accordion
        );
        return content;
    }

    private final StatisticsService statisticsService;
}
