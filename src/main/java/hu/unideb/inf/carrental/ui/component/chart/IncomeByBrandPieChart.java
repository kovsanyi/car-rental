package hu.unideb.inf.carrental.ui.component.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.statistics.resource.model.IncomeByBrandResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeByBrandPieChart extends VerticalLayout {

    public IncomeByBrandPieChart(List<IncomeByBrandResponse> incomeByBrandResponses, LocalDate firstDate, LocalDate lastDate) {
        this.incomeByBrandResponses = incomeByBrandResponses;
        this.firstDate = firstDate;
        this.lastDate = lastDate;

        setSizeUndefined();
        setMargin(false);
        setSpacing(false);

        addComponent(buildContent());
    }

    private AbstractLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeight(100.f, Unit.PERCENTAGE);
        content.setMargin(false);
        content.setSpacing(false);

        content.addComponent(buildPieChart());
        return content;
    }

    private Component buildPieChart() {
        PieChartConfig config = new PieChartConfig();
        config.data()
                .labelsAsList(incomeByBrandResponses.stream().map(IncomeByBrandResponse::getBrand)
                        .collect(Collectors.toList()))
                .addDataset(new PieDataset());

        config.options()
                .responsive(true)
                .title().display(true).text(String.format("Income by brand between %s and %s", firstDate.toString(), lastDate.toString())).and()
                .animation().animateRotate(true).and()
                .done();

        Map<Double, String> incomeWithColor = new HashMap<>();
        incomeByBrandResponses.stream().map(IncomeByBrandResponse::getIncome)
                .forEach(e -> incomeWithColor.put(e.doubleValue(), ColorUtils.randomColor(0.7)));

        PieDataset dataset = (PieDataset) config.data().getFirstDataset();
        dataset.dataAsList(new ArrayList<>(incomeWithColor.keySet()));
        dataset.backgroundColor(incomeWithColor.values().toArray(new String[incomeWithColor.size()]));

        ChartJs chartJs = new ChartJs(config);
        chartJs.setJsLoggingEnabled(false);
        chartJs.setWidth(100.f, Unit.PERCENTAGE);
        chartJs.setHeightUndefined();
        return chartJs;
    }

    private final LocalDate firstDate;
    private final LocalDate lastDate;
    private final List<IncomeByBrandResponse> incomeByBrandResponses;
}
