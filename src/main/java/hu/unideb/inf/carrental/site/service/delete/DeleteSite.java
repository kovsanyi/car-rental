package hu.unideb.inf.carrental.site.service.delete;

import hu.unideb.inf.carrental.car.service.delete.DeleteCar;
import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.CarRepository;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
import hu.unideb.inf.carrental.commons.exception.CollisionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteSite {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCar.class);

    private final SiteRepository siteRepository;
    private final CarRepository carRepository;

    @Autowired
    public DeleteSite(SiteRepository siteRepository, CarRepository carRepository) {
        this.siteRepository = siteRepository;
        this.carRepository = carRepository;
    }

    @Transactional(rollbackFor = CollisionException.class)
    public void delete(Site site) throws CollisionException {
        LOGGER.trace("Deleting site ID {}", site.getId());
        if (!carRepository.findBySite(site).isEmpty()) {
            throw new CollisionException(Constants.SITE_HAS_CARS);
        }
        siteRepository.delete(site);
    }
}
