package hu.unideb.inf.carrental.ui.commons.bar;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.ui.CarRentalUI;
import hu.unideb.inf.carrental.ui.carsearch.CarSearchView;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.commons.util.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerBar extends VerticalLayout {

    public CustomerBar(CarService carService) {
        this.carService = carService;

        setWidth(100.f, Unit.PERCENTAGE);
        setHeight(310.f, Unit.PIXELS);
        setMargin(false);
        setSpacing(false);
        setId("bar");
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addStyleName("customer-bar");

        category = buildCategory();
        brand = buildBrand();
        fuelType = buildFuelType();
        seatNumber = buildSeatNumber();
        minPrice = buildMinPrice();
        maxPrice = buildMaxPrice();
        search = buildSearchButton();

        addComponents(
                buildLogo(),
                buildContent()
        );

        refreshBrand();
    }

    private AbstractOrderedLayout buildLogo() {
        final VerticalLayout logo = new VerticalLayout();
        logo.setWidthUndefined();
        logo.setHeight(100.f, Unit.PERCENTAGE);
        logo.setMargin(false);
        logo.setSpacing(false);
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        final Label title = new Label("Car Rental");
        title.setHeight(100.f, Unit.PERCENTAGE);
        title.setId("logo");

        logo.addComponent(title);
        return logo;
    }

    private AbstractOrderedLayout buildContent() {
        final VerticalLayout content = new VerticalLayout();
        content.setWidthUndefined();
        content.setHeight(100.f, Unit.PERCENTAGE);
        content.setMargin(false);
        content.setSpacing(false);
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        final HorizontalLayout searchBar = new HorizontalLayout();
        searchBar.setSizeUndefined();
        searchBar.setMargin(false);
        searchBar.setSpacing(true);
        searchBar.setId("content");

        searchBar.addComponents(
                category,
                brand,
                fuelType,
                seatNumber,
                minPrice,
                maxPrice,
                search
        );
        searchBar.setComponentAlignment(search, Alignment.BOTTOM_LEFT);

        content.addComponent(searchBar);
        return content;
    }

    private NativeSelect<String> buildCategory() {
        List<String> categoryData = new ArrayList<>();
        categoryData.add(SELECT_ALL);
        carService.getUsedCarCategories().forEach(e -> categoryData.add((String) e));

        NativeSelect<String> category = new NativeSelect<>("Category", categoryData);
        category.setWidth(150, Sizeable.Unit.PIXELS);
        category.setEmptySelectionAllowed(false);
        category.setSelectedItem(categoryData.get(0));
        category.addValueChangeListener(e -> refreshBrand());
        return category;
    }

    private NativeSelect<String> buildBrand() {
        NativeSelect<String> brand = new NativeSelect<>("Brand");
        brand.setWidth(150, Sizeable.Unit.PIXELS);
        brand.setEmptySelectionAllowed(false);
        return brand;
    }

    private NativeSelect<String> buildFuelType() {
        List<String> fuelTypeData = new ArrayList<>();
        fuelTypeData.add(SELECT_ALL);
        carService.getAllFuelType().forEach(e -> fuelTypeData.add(e.toString()));

        NativeSelect<String> fuelType = new NativeSelect<>("Fuel type", fuelTypeData);
        fuelType.setWidth(120, Sizeable.Unit.PIXELS);
        fuelType.setEmptySelectionAllowed(false);
        fuelType.setSelectedItem(fuelTypeData.get(0));
        return fuelType;
    }

    private NativeSelect<String> buildSeatNumber() {
        List<String> seatNumberData = new ArrayList<>();
        seatNumberData.add(SELECT_ALL);
        IntStream.rangeClosed(2, 8).mapToObj(String::valueOf).collect(Collectors.toCollection(() -> seatNumberData));

        NativeSelect<String> seatNumber = new NativeSelect<>("Seat number", seatNumberData);
        seatNumber.setWidth(80, Sizeable.Unit.PIXELS);
        seatNumber.setEmptySelectionAllowed(false);
        seatNumber.setSelectedItem(seatNumberData.get(0));
        return seatNumber;
    }

    private TextField buildMinPrice() {
        TextField minPrice = new TextField("Min. price");
        minPrice.setWidth(100, Sizeable.Unit.PIXELS);
        return minPrice;
    }

    private TextField buildMaxPrice() {
        TextField maxPrice = new TextField("Max. price");
        maxPrice.setWidth(100, Sizeable.Unit.PIXELS);
        return maxPrice;
    }

    private Button buildSearchButton() {
        Button search = new Button("Search");
        search.setIcon(VaadinIcons.SEARCH);
        search.setStyleName(ValoTheme.BUTTON_PRIMARY);
        search.addClickListener(e ->
        {
            if (isMinMaxValueValid()) {
                CarRentalUI.getCurrent().getPage().open("/#!"
                                + CarSearchView.VIEW_NAME
                                + getParams(),
                        "");
            }
        });
        return search;
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

    private String getParams() {
        StringBuilder params = new StringBuilder("");
        params.append(!category.getSelectedItem().get().equals(SELECT_ALL) ? UIUtils.Params.createKeyValuePair(Constants.Params.CATEGORY, category.getSelectedItem().get()) : "");
        params.append(!brand.getSelectedItem().get().equals(SELECT_ALL) ? UIUtils.Params.createKeyValuePair(Constants.Params.BRAND, brand.getSelectedItem().get()) : "");
        params.append(!fuelType.getSelectedItem().get().equals(SELECT_ALL) ? UIUtils.Params.createKeyValuePair(Constants.Params.FUEL_TYPE, fuelType.getSelectedItem().get()) : "");
        params.append(!seatNumber.getSelectedItem().get().equals(SELECT_ALL) ? UIUtils.Params.createKeyValuePair(Constants.Params.SEAT_NUMBER, seatNumber.getSelectedItem().get()) : "");
        params.append(minPrice.getOptionalValue().isPresent() ? UIUtils.Params.createKeyValuePair(Constants.Params.MIN_PRICE, minPrice.getValue()) : "");
        params.append(maxPrice.getOptionalValue().isPresent() ? UIUtils.Params.createKeyValuePair(Constants.Params.MAX_PRICE, maxPrice.getValue()) : "");

        return params.toString();
    }

    private boolean isMinMaxValueValid() {
        try {
            Float.valueOf(minPrice.getValue());
            Float.valueOf(maxPrice.getValue());
        } catch (NumberFormatException e) {
            UIUtils.showNotification("Text fields of min/max prices are invalid!", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private NativeSelect<String> category;
    private NativeSelect<String> brand;
    private NativeSelect<String> fuelType;
    private NativeSelect<String> seatNumber;
    private TextField minPrice;
    private TextField maxPrice;
    private Button search;

    private final CarService carService;

    private static final String SELECT_ALL = "All";
}
