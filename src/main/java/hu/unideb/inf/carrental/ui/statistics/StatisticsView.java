package hu.unideb.inf.carrental.ui.statistics;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import hu.unideb.inf.carrental.statistics.service.StatisticsService;
import hu.unideb.inf.carrental.ui.menu.CarRentalMenu;
import hu.unideb.inf.carrental.ui.statistics.company.CompanyStatisticsContent;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = StatisticsView.VIEW_NAME)
public class StatisticsView extends VerticalLayout implements View {

    @Autowired
    public StatisticsView(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;

        //setMargin(false);
        //TODO debug
        setMargin(new MarginInfo(true, false, false, false));

        setSpacing(true);
        setWidth(100.f, Unit.PERCENTAGE);
        setHeightUndefined();
        setDefaultComponentAlignment(Alignment.TOP_CENTER);

        addComponent(new CarRentalMenu());
        addComponent(buildContent());
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new CompanyStatisticsContent(statisticsService);
        return content;
    }

    private final StatisticsService statisticsService;

    public static final String VIEW_NAME = "statistics";
}
