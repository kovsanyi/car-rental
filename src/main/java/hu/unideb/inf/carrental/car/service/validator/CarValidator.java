package hu.unideb.inf.carrental.car.service.validator;

import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.site.service.validator.SiteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarValidator {
    private static final Logger logger = LoggerFactory.getLogger(CarValidator.class);

    private final SiteValidator siteValidator;

    @Autowired
    public CarValidator(SiteValidator siteValidator) {
        this.siteValidator = siteValidator;
    }

    public void validate(Car car) throws UnauthorizedAccessException {
        logger.info("Validating car");
        siteValidator.validate(car.getSite());
    }
}
