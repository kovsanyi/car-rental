package hu.unideb.inf.carrental.car.service.converter;

import hu.unideb.inf.carrental.car.resource.model.CarResponse;
import hu.unideb.inf.carrental.commons.domain.car.Car;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(CarResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CarResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CarResponse from(Car car) {
        logger.info("Converting Car to CarResponse");
        return modelMapper.map(car, CarResponse.class);
    }
}
