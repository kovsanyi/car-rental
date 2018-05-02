package hu.unideb.inf.carrental.ui.carsearch.content;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.ui.commons.component.item.CarItem;
import hu.unideb.inf.carrental.ui.commons.content.car.CarsContent;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CarSearchContent extends CarsContent {

    public CarSearchContent(CarService carService, CarImageService carImageService) {
        super("Car search");

        this.carService = carService;
        this.carImageService = carImageService;

        setupHeader();
        setupBody();
    }

    private void setupHeader() {
        searchBarContainer = new HorizontalLayout();
        searchBarContainer.setMargin(false);
        searchBarContainer.setSpacing(false);
        searchBarContainer.setWidth(100.f, Unit.PERCENTAGE);
        searchBarContainer.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

        getHeader().addComponent(searchBarContainer);
        getHeader().setComponentAlignment(searchBarContainer, Alignment.MIDDLE_RIGHT);
    }

    private void setupBody() {
        loadSearchForm();
    }

    private void loadSearchForm() {
        searchBarContainer.removeAllComponents();

        final VerticalLayout searchForm = new VerticalLayout();
        searchForm.setWidth(220.f, Unit.PIXELS);
        searchForm.setHeightUndefined();
        searchForm.setMargin(false);
        searchForm.setSpacing(true);
        searchForm.setId("search_form");

        final TextField city = new TextField("City");
        //city.setPlaceholder("City");
        city.setWidth(100.f, Unit.PERCENTAGE);
        city.focus();

        final DateField firstDate = new DateField("First date");
        firstDate.setWidth(100.f, Unit.PERCENTAGE);
        firstDate.setDateFormat("yyyy-MM-dd");
        firstDate.setValue(LocalDate.now());

        final DateField lastDate = new DateField("Last date");
        lastDate.setWidth(100.f, Unit.PERCENTAGE);
        lastDate.setDateFormat("yyyy-MM-dd");
        lastDate.setValue(LocalDate.now());

        final Button search = new Button();
        search.setWidth(100.f, Unit.PERCENTAGE);
        search.setIcon(VaadinIcons.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        search.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        search.addClickListener(e -> {
            getBody().removeAllComponents();
            getBody().setColumns(2);

            searchBarContainer.addComponent(buildSearchBar(firstDate.getValue(), lastDate.getValue(), city.getValue()));
            search(firstDate.getValue(), lastDate.getValue(), city.getValue());
        });

        searchForm.addComponents(
                city,
                firstDate,
                lastDate,
                search
        );

        getBody().removeAllComponents();
        getBody().setColumns(1);
        getBody().addComponent(searchForm);
    }

    private HorizontalLayout buildSearchBar(LocalDate firstDateValue, LocalDate lastDateValue, String _cityValue) {
        final HorizontalLayout searchBar = new HorizontalLayout();
        searchBar.setMargin(false);
        searchBar.setSpacing(true);
        searchBar.setSizeUndefined();

        final TextField city = new TextField();
        city.setWidth(146.f, Unit.PIXELS);
        city.setPlaceholder("City");
        city.setValue(_cityValue);

        final DateField firstDate = new DateField();
        firstDate.setWidth(146.f, Unit.PIXELS);
        firstDate.setDateFormat("yyyy-MM-dd");
        firstDate.setValue(firstDateValue);

        final DateField lastDate = new DateField();
        lastDate.setWidth(146.f, Unit.PIXELS);
        lastDate.setDateFormat("yyyy-MM-dd");
        lastDate.setValue(lastDateValue);

        final Button search = new Button();
        search.setStyleName(ValoTheme.BUTTON_PRIMARY);
        search.setIcon(VaadinIcons.SEARCH);
        search.addClickListener(e -> search(firstDate.getValue(), lastDate.getValue(), city.getValue()));

        searchBar.addComponents(
                city,
                firstDate,
                lastDate,
                search
        );

        return searchBar;
    }

    private void search(LocalDate firstDate, LocalDate lastDate, String city) {
        Map<CarResponse, CarImageResponse> carWithCover = new HashMap<>();
        CarImageResponse carImageResponse;

        for (CarResponse carResponse : carService.getAvailableCarsForRent(firstDate, lastDate, city)) {
            try {
                carImageResponse = carImageService.getCoverByCarId(carResponse.getId());
            } catch (NotFoundException e) {
                carImageResponse = null;
            }
            carWithCover.put(carResponse, carImageResponse);
        }

        if (!carWithCover.isEmpty()) {
            getBody().removeAllComponents();

            carWithCover.entrySet().stream()
                    .map(e -> new CarItem(e.getKey(), e.getValue()))
                    .forEach(getBody()::addComponent);
        } else {
            UIUtils.showNotification("Car not found!", Notification.Type.WARNING_MESSAGE);
            loadSearchForm();
        }
    }

    private HorizontalLayout searchBarContainer;

    private final CarService carService;
    private final CarImageService carImageService;
}
