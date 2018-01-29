package hu.unideb.inf.carrental.ui.commons.component.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import hu.unideb.inf.carrental.statistics.resource.model.IncomeByBrandResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeByBrandPieChart extends ChartJs {

    public IncomeByBrandPieChart() {
        config = new PieChartConfig();

        configure(config);
        setJsLoggingEnabled(false);
        setSizeFull();
    }

    public void setData(List<IncomeByBrandResponse> incomeByBrandResponses) {
        config.data()
                .clear()
                .labelsAsList(incomeByBrandResponses.stream()
                        .map(IncomeByBrandResponse::getBrand)
                        .collect(Collectors.toList()))
                .addDataset(new PieDataset());

        config.options()
                .responsive(true)
                .animation().animateRotate(true).and()
                .done();

        final Map<Double, String> incomeWithColor = new HashMap<>();
        incomeByBrandResponses.stream()
                .map(IncomeByBrandResponse::getIncome)
                .forEach(e -> incomeWithColor.put(e.doubleValue(), ColorUtils.randomColor(0.7)));

        final PieDataset dataset = (PieDataset) config.data().getFirstDataset();
        dataset.dataAsList(new ArrayList<>(incomeWithColor.keySet()));
        dataset.backgroundColor(incomeWithColor.values().toArray(new String[incomeWithColor.size()]));

        refreshData();
    }

    private final PieChartConfig config;
}
