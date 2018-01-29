package hu.unideb.inf.carrental.ui.statistics.company;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.statistics.service.StatisticsService;
import hu.unideb.inf.carrental.ui.commons.component.chart.IncomeByBrandPieChart;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.commons.content.CarRentalContent;

import java.time.LocalDate;
import java.time.Period;

public class CompanyStatisticsContent extends CarRentalContent {

    public CompanyStatisticsContent(StatisticsService statisticsService) {
        super("Statistics");
        this.statisticsService = statisticsService;

        pieChart = new IncomeByBrandPieChart();
        setupBody();
    }

    private void setupBody() {
        final Label text = new Label("Income by car brand");
        text.setSizeUndefined();
        text.addStyleName(ValoTheme.LABEL_H1);
        text.addStyleName(ValoTheme.LABEL_BOLD);
        text.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        final VerticalLayout inputPanelContainer = new VerticalLayout();
        inputPanelContainer.setMargin(false);
        inputPanelContainer.setSpacing(true);
        inputPanelContainer.setSizeUndefined();
        inputPanelContainer.addComponents(
                text,
                buildInputPanel()
        );

        final VerticalLayout pieChartContainer = new VerticalLayout();
        pieChartContainer.setMargin(true);
        pieChartContainer.setSpacing(false);
        pieChartContainer.setSizeUndefined();
        pieChartContainer.addComponent(pieChart);

        getBody().addComponents(
                inputPanelContainer,
                pieChartContainer
        );
    }

    private Panel buildInputPanel() {
        final Panel inputPanel = new Panel();
        inputPanel.setWidth(340.f, Unit.PIXELS);
        inputPanel.setCaption("Period");

        final FormLayout panelContent = new FormLayout();
        panelContent.setMargin(true);
        panelContent.setSpacing(true);
        panelContent.setWidth(100.f, Unit.PERCENTAGE);

        final DateField firstDate = new DateField("First date");
        firstDate.setWidth(100.f, Unit.PERCENTAGE);
        firstDate.setValue(LocalDate.now().minus(Period.ofMonths(1)));
        firstDate.setDateFormat(Constants.DATE_FORMAT);

        final DateField lastDate = new DateField("Last date");
        lastDate.setWidth(100.f, Unit.PERCENTAGE);
        lastDate.setValue(LocalDate.now());
        lastDate.setDateFormat(Constants.DATE_FORMAT);

        final Button summary = new Button("Summary");
        summary.setWidth(100.f, Unit.PERCENTAGE);
        summary.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        summary.setIcon(VaadinIcons.PIE_CHART);
        summary.addClickListener(
                e -> updatePieChart(firstDate.getValue(), lastDate.getValue())
        );

        panelContent.addComponents(
                firstDate,
                lastDate,
                summary
        );

        inputPanel.setContent(panelContent);
        return inputPanel;
    }

    private void updatePieChart(LocalDate firstDate, LocalDate lastDate) {
        pieChart.setData(statisticsService.getIncomeByBrand(firstDate, lastDate));
    }

    @Override
    protected AbstractLayout buildBody() {
        final HorizontalLayout body = new HorizontalLayout();
        body.setMargin(false);
        body.setSpacing(false);
        body.setSizeFull();
        body.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        return body;
    }

    private final IncomeByBrandPieChart pieChart;

    private final StatisticsService statisticsService;
}
