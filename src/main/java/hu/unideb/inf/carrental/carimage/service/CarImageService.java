package hu.unideb.inf.carrental.carimage.service;

import hu.unideb.inf.carrental.carimage.resource.model.CarImageResponse;
import hu.unideb.inf.carrental.carimage.service.validator.CarImageValidator;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.CarRepository;
import hu.unideb.inf.carrental.commons.domain.carimage.CarImage;
import hu.unideb.inf.carrental.commons.domain.carimage.CarImageRepository;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarImageService.class);

    private final CarImageValidator carImageValidator;
    private final CarImageRepository carImageRepository;
    private final CarRepository carRepository;

    @Autowired
    public CarImageService(CarImageValidator carImageValidator, CarImageRepository carImageRepository,
                           CarRepository carRepository) {
        this.carImageValidator = carImageValidator;
        this.carImageRepository = carImageRepository;
        this.carRepository = carRepository;
    }

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public void save(long carId, byte[] data) throws NotFoundException, IOException, UnauthorizedAccessException {
        LOGGER.info("Saving picture of car ID {} [size: {} byte]", carId, data.length);
        CarImage carImage = new CarImage();
        carImage.setCar(carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_NOT_FOUND)));
        carImage.setImageData(data);
        carImageValidator.validate(carImage);
        carImageRepository.save(carImage);
    }

    public CarImageResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing image by ID {}", id);
        return new CarImageResponse(carImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND)).getImageData());
    }

    public byte[] getAsByteById(long id) throws NotFoundException {
        LOGGER.info("Providing image as byte by ID {}", id);
        return carImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND)).getImageData();
    }

    public CarImageResponse getCoverByCarId(long carId) throws NotFoundException {
        LOGGER.info("Providing cover image by car ID {}", carId);
        return new CarImageResponse(carImageRepository.findByCarId(carId).stream()
                .map(CarImage::getImageData).findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND)));
    }

    public byte[] getCoverAsByteByCarId(long carId) throws NotFoundException {
        LOGGER.info("Providing cover image as byte by car ID {}", carId);
        return carImageRepository.findByCarId(carId).stream().map(CarImage::getImageData).findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND));
    }

    public List<CarImageResponse> getAllByCarId(long carId) {
        LOGGER.info("Providing images by car ID {}", carId);
        return carImageRepository.findByCarId(carId).stream().map(CarImage::getImageData).map(CarImageResponse::new)
                .collect(Collectors.toList());
    }

    public List<Long> getIdsByCarId(long carId) {
        LOGGER.info("Providing image IDs by car ID {}", carId);
        return carImageRepository.findByCarId(carId).stream().map(CarImage::getId).collect(Collectors.toList());
    }
}
