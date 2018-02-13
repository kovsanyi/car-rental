package hu.unideb.inf.carrental.ui.carsearch.content;

import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.car.service.CarService;
import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.CarImageService;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.ui.commons.component.item.CarItem;
import hu.unideb.inf.carrental.ui.commons.constant.Constants;
import hu.unideb.inf.carrental.ui.commons.content.car.CarsContent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CarSearchContent extends CarsContent {

    public CarSearchContent(Map<String, String> params, CarService carService, CarImageService carImageService) {
        super("Search a car");

        this.params = params;
        this.carService = carService;
        this.carImageService = carImageService;

        setupBody();
    }

    private void setupBody() {
        Map<CarResponse, CarImageResponse> carWithCover = new HashMap<>();
        CarImageResponse carImageResponse;

        for (CarResponse carResponse : carService.getByParams(
                Objects.nonNull(params.get(Constants.Params.CATEGORY)) ? CarCategory.valueOf(params.get(Constants.Params.CATEGORY)) : null,
                params.get(Constants.Params.BRAND),
                Objects.nonNull(params.get(Constants.Params.FUEL_TYPE)) ? FuelType.valueOf(params.get(Constants.Params.FUEL_TYPE)) : null,
                Objects.nonNull(params.get(Constants.Params.SEAT_NUMBER)) ? Integer.parseInt(params.get(Constants.Params.SEAT_NUMBER)) : null,
                Objects.nonNull(params.get(Constants.Params.MIN_PRICE)) ? Integer.parseInt(params.get(Constants.Params.MIN_PRICE)) : null,
                Objects.nonNull(params.get(Constants.Params.MAX_PRICE)) ? Integer.parseInt(params.get(Constants.Params.MAX_PRICE)) : null
        )) {
            try {
                carImageResponse = carImageService.getCoverByCarId(carResponse.getId());
            } catch (NotFoundException e) {
                carImageResponse = null;
            }
            carWithCover.put(carResponse, carImageResponse);
        }

        carWithCover.entrySet().stream()
                .map(e -> new CarItem(e.getKey(), e.getValue()))
                .forEach(getBody()::addComponent);
    }

    private final Map<String, String> params;

    private final CarService carService;
    private final CarImageService carImageService;
}
