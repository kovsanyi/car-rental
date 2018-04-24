package hu.unideb.inf.carrental.carimage.service.validator;

import hu.unideb.inf.carrental.commons.domain.carimage.CarImage;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.site.service.validator.SiteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CarImageValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarImageValidator.class);

    private final SiteValidator siteValidator;

    @Autowired
    public CarImageValidator(SiteValidator siteValidator) {
        this.siteValidator = siteValidator;
    }

    public void validate(CarImage carImage) throws IOException, UnauthorizedAccessException {
        LOGGER.info("Validating car image");
        if (carImage.getImageData().length > 2000000) {
            throw new IOException("Car image size is to big!");
        }
        if (carImage.getImageData().length == 0) {
            throw new IOException("Car image size can not be empty!");
        }
        siteValidator.validate(carImage.getCar().getSite());
    }
}
