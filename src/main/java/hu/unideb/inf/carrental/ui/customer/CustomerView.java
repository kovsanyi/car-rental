package hu.unideb.inf.carrental.ui.customer;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.ui.component.item.CarItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Deprecated
@UIScope
@SpringView(name = CustomerView.VIEW_NAME)
public class CustomerView extends VerticalLayout implements View {

    @Autowired
    public CustomerView(CarService carService) {
        this.carService = carService;

        setWidth(100, Unit.PERCENTAGE);
        setMargin(false);
        setSpacing(false);
        setDefaultComponentAlignment(Alignment.TOP_CENTER);

        VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Component searchForm = buildSearchForm();
        root.addComponent(searchForm);

        searchResults = new GridLayout();
        searchResults.setSizeUndefined();
        searchResults.setColumns(2);
        root.addComponent(searchResults);

        addComponent(root);
    }

    private Component buildSearchForm() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeUndefined();
        layout.setMargin(false);

        buildCategory();
        buildBrand();
        buildFuelType();
        buildSeatNumber();
        buildSearchButton();
        buildMinAndMaxPrices();

        layout.addComponents(category, brand, fuelType, seatNumber, minPrice, maxPrice, search);
        layout.setComponentAlignment(search, Alignment.BOTTOM_RIGHT);

        return layout;
    }

    private void buildCategory() {
        List<String> categoryData = new ArrayList<>();
        categoryData.add(SELECT_ALL);
        carService.getUsedCarCategories().forEach(e -> categoryData.add((String) e));

        category = new NativeSelect<>("Category", categoryData);
        category.setWidth(150, Unit.PIXELS);
        category.setEmptySelectionAllowed(false);
        category.setSelectedItem(categoryData.get(0));
        category.addValueChangeListener(e -> refreshBrand());
    }

    private void buildBrand() {
        brand = new NativeSelect<>("Brand");
        brand.setWidth(150, Unit.PIXELS);
        brand.setEmptySelectionAllowed(false);
        refreshBrand();
    }

    private void buildFuelType() {
        List<String> fuelTypeData = new ArrayList<>();
        fuelTypeData.add(SELECT_ALL);
        carService.getAllFuelType().forEach(e -> fuelTypeData.add(e.toString()));

        fuelType = new NativeSelect<>("Fuel type", fuelTypeData);
        fuelType.setWidth(120, Unit.PIXELS);
        fuelType.setEmptySelectionAllowed(false);
        fuelType.setSelectedItem(fuelTypeData.get(0));
    }

    private void buildSeatNumber() {
        List<String> seatNumberData = new ArrayList<>();
        seatNumberData.add(SELECT_ALL);
        IntStream.rangeClosed(2, 8).mapToObj(String::valueOf).collect(Collectors.toCollection(() -> seatNumberData));

        seatNumber = new NativeSelect<>("Seat number", seatNumberData);
        seatNumber.setWidth(80, Unit.PIXELS);
        seatNumber.setEmptySelectionAllowed(false);
        seatNumber.setSelectedItem(seatNumberData.get(0));
    }

    private void buildMinAndMaxPrices() {
        minPrice = new TextField("Min. Price");
        maxPrice = new TextField("Max. Price");

        minPrice.setWidth(100, Unit.PIXELS);
        maxPrice.setWidth(100, Unit.PIXELS);
    }

    private void buildSearchButton() {
        search = new Button("Search");
        search.setIcon(VaadinIcons.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_PRIMARY);
        search.addClickListener(e -> {
            searchResults.removeAllComponents();

            carService.getByParams(
                    !category.getSelectedItem().get().equals(SELECT_ALL) ? CarCategory.valueOf(category.getSelectedItem().get()) : null,
                    !brand.getSelectedItem().get().equals(SELECT_ALL) ? brand.getSelectedItem().get() : null,
                    !fuelType.getSelectedItem().get().equals(SELECT_ALL) ? FuelType.valueOf(fuelType.getSelectedItem().get()) : null,
                    !seatNumber.getSelectedItem().get().equals(SELECT_ALL) ? Integer.parseInt(seatNumber.getSelectedItem().get()) : null,
                    //TODO what to do if input is not valid
                    minPrice.getOptionalValue().isPresent() ? Integer.parseInt(minPrice.getValue()) : null,
                    maxPrice.getOptionalValue().isPresent() ? Integer.parseInt(maxPrice.getValue()) : null
            ).stream().map(carResponse -> new CarItem(carResponse, null)).forEach(searchResults::addComponent);
        });

    }

    private void refreshBrand() {
        brand.clear();

        List<String> brandData = new ArrayList<>();
        brandData.add("All");
        if (category.getSelectedItem().get().equals(SELECT_ALL)) {
            carService.getUsedBrands().forEach(e -> brandData.add((String) e));
        } else {
            carService.getUsedBrandsByCategory(CarCategory.valueOf(category.getSelectedItem().get()))
                    .forEach(e -> brandData.add((String) e));
        }
        brand.setItems(brandData);
        brand.setSelectedItem(brandData.get(0));
    }

    private GridLayout searchResults;
    private NativeSelect<String> category;
    private NativeSelect<String> brand;
    private NativeSelect<String> fuelType;
    private NativeSelect<String> seatNumber;
    private TextField minPrice;
    private TextField maxPrice;
    private Button search;

    private final CarService carService;

    private static final String SELECT_ALL = "All";
    public static final String VIEW_NAME = "customer_home";
}
