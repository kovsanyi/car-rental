package hu.unideb.inf.carrental.ui.company.layout.statistics.element;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.statistics.resource.model.IncomeByBrandResponse;
import hu.unideb.inf.carrental.ui.component.chart.IncomeByBrandPieChart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeByBrandLayout extends VerticalLayout {

    public IncomeByBrandLayout() {
        this.incomeByBrandResponses = getDemo();
        addComponent(buildContent());
    }

    private AbstractLayout buildContent() {
        final HorizontalLayout content = new HorizontalLayout();
        content.setMargin(false);
        content.setWidth(100.f, Unit.PERCENTAGE);
        content.setHeightUndefined();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Component datePicker = buildDatePicker();
        content.addComponent(datePicker);

        content.addComponent(buildPieChart());
        return content;
    }

    private AbstractLayout buildDatePicker() {
        final VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setSpacing(true);
        root.setSizeUndefined();

        final DateField firstDate = new DateField("First date");
        final DateField lastDate = new DateField("Last date");

        final Button summary = new Button("Summary");
        summary.setWidth(100.f, Unit.PERCENTAGE);
        summary.setStyleName(ValoTheme.BUTTON_PRIMARY);

        root.addComponents(firstDate, lastDate, summary);
        return root;
    }

    private AbstractLayout buildPieChart() {
        final AbstractOrderedLayout pieChartContainer = new IncomeByBrandPieChart(getDemo(), LocalDate.now(), LocalDate.now());
        pieChartContainer.setMargin(false);
        pieChartContainer.setSpacing(false);
        pieChartContainer.setWidth(550.f, Unit.PIXELS);
        pieChartContainer.setHeightUndefined();

        return pieChartContainer;
    }

    private List<IncomeByBrandResponse> getDemo() {
        List<IncomeByBrandResponse> demo = new ArrayList<>();
        demo.add(new IncomeByBrandResponse("Suzuki", 600000));
        demo.add(new IncomeByBrandResponse("BMW", 230000));
        demo.add(new IncomeByBrandResponse("Ford", 240000));
        return demo;
    }

    private final List<IncomeByBrandResponse> incomeByBrandResponses;
}
