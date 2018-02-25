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

/**
 * Service class for {@code CarImage} entity. It is used for saving and deleting car images.
 *
 * @see CarImage
 * @see CarImageResponse
 * @see hu.unideb.inf.carrental.commons.domain.car.Car
 */
@Service
public class CarImageService {
    /**
     * Saves the image and connects it to the given car.
     *
     * @param carId ID of the car
     * @param data  raw data of the image
     * @throws NotFoundException           if the car ID is invalid
     * @throws IOException                 if the image data size is too big
     * @throws UnauthorizedAccessException if the logged in user has no right to access the specified car
     */
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

    /**
     * Returns the image encoded with Base64 by ID in a {@code CarImageResponse}.
     *
     * @param id ID of the image
     * @return the image by ID in a {@code CarImageResponse}
     * @throws NotFoundException if the ID of the image is invalid
     */
    public CarImageResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing image by ID {}", id);
        return new CarImageResponse(carImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND)).getImageData());
    }

    /**
     * Returns the image by ID in a raw byte array.
     *
     * @param id ID of the image
     * @return the image by ID in a raw byte array
     * @throws NotFoundException if the ID of the image is invalid
     */
    public byte[] getAsByteById(long id) throws NotFoundException {
        LOGGER.info("Providing image as byte by ID {}", id);
        return carImageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND)).getImageData();
    }

    /**
     * Returns the first image as a cover image by car ID encoded with Base64 in a {@code CarImageResponse}.
     *
     * @param carId ID of the car
     * @return the first image as a cover image by car ID encoded with Base64 in a {@code CarImageResponse}
     * @throws NotFoundException if the ID of the car is invalid
     */
    public CarImageResponse getCoverByCarId(long carId) throws NotFoundException {
        LOGGER.info("Providing cover image by car ID {}", carId);

        byte[] image = carImageRepository.findByCarId(carId).stream()
                .map(CarImage::getImageData)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND));

        return new CarImageResponse(image);
    }

    /**
     * Returns the first image as a cover image by car ID in a raw byte array.
     *
     * @param carId ID of the car
     * @return the first image as a cover image by car ID in a raw byte array
     * @throws NotFoundException if the car ID is invalid
     */
    public byte[] getCoverAsByteByCarId(long carId) throws NotFoundException {
        LOGGER.info("Providing cover image as byte by car ID {}", carId);

        return carImageRepository.findByCarId(carId).stream()
                .map(CarImage::getImageData)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Constants.CAR_IMAGE_NOT_FOUND));
    }

    /**
     * Returns all the images encoded with Base64 in a list of {@code CarImageResponse}. If the ID of the car is
     * invalid, it will return an empty list.
     *
     * @param carId ID of the car
     * @return all the images encoded with Base64 in a list of {@code CarImageResponse}
     */
    public List<CarImageResponse> getAllByCarId(long carId) {
        LOGGER.info("Providing images by car ID {}", carId);
        return carImageRepository.findByCarId(carId).stream().map(CarImage::getImageData).map(CarImageResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Returns all the image IDs by car ID. If the ID of the car is invalid, it will return an empty list.
     *
     * @param carId ID of the car
     * @return all the image IDs by car ID
     */
    public List<Long> getIdsByCarId(long carId) {
        LOGGER.info("Providing image IDs by car ID {}", carId);
        return carImageRepository.findByCarId(carId).stream().map(CarImage::getId).collect(Collectors.toList());
    }

    @Autowired
    public CarImageService(CarImageValidator carImageValidator, CarImageRepository carImageRepository,
                           CarRepository carRepository) {
        this.carImageValidator = carImageValidator;
        this.carImageRepository = carImageRepository;
        this.carRepository = carRepository;
    }

    private final CarImageValidator carImageValidator;
    private final CarImageRepository carImageRepository;

    private final CarRepository carRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CarImageService.class);
}
