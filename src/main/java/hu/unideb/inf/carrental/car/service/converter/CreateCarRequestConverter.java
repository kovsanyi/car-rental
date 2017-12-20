package hu.unideb.inf.carrental.car.service.converter;

import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.commons.domain.car.Car;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCarRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(CreateCarRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CreateCarRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Car from(CreateCarRequest createCarRequest) {
        logger.info("Converting CreateCarRequest to Car");
        return modelMapper.map(createCarRequest, Car.class);
    }
}
