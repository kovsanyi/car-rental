package hu.unideb.inf.carrental.site.service;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.car.CarRepository;
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.commons.domain.manager.ManagerRepository;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.exception.CollisionException;
import hu.unideb.inf.carrental.commons.exception.ManagerCollisionException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import hu.unideb.inf.carrental.site.resource.model.UpdateSiteRequest;
import hu.unideb.inf.carrental.site.service.converter.CreateSiteRequestConverter;
import hu.unideb.inf.carrental.site.service.converter.SiteResponseConverter;
import hu.unideb.inf.carrental.site.service.converter.UpdateSiteRequestConverter;
import hu.unideb.inf.carrental.site.service.delete.DeleteSite;
import hu.unideb.inf.carrental.site.service.validator.SiteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiteService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SiteService.class);

    private final SiteRepository siteRepository;
    private final CompanyRepository companyRepository;
    private final ManagerRepository managerRepository;
    private final CarRepository carRepository;
    private final SiteValidator siteValidator;
    private final DeleteSite deleteSite;

    private final CreateSiteRequestConverter createSiteRequestConverter;
    private final UpdateSiteRequestConverter updateSiteRequestConverter;
    private final SiteResponseConverter siteResponseConverter;

    @Autowired
    public SiteService(SiteRepository siteRepository, SiteValidator siteValidator, CompanyRepository companyRepository,
                       ManagerRepository managerRepository, CarRepository carRepository, DeleteSite deleteSite,
                       CreateSiteRequestConverter createSiteRequestConverter,
                       UpdateSiteRequestConverter updateSiteRequestConverter,
                       SiteResponseConverter siteResponseConverter) {
        this.siteRepository = siteRepository;
        this.siteValidator = siteValidator;
        this.companyRepository = companyRepository;
        this.managerRepository = managerRepository;
        this.carRepository = carRepository;
        this.deleteSite = deleteSite;
        this.createSiteRequestConverter = createSiteRequestConverter;
        this.updateSiteRequestConverter = updateSiteRequestConverter;
        this.siteResponseConverter = siteResponseConverter;
    }

    @Secured("ROLE_COMPANY")
    public long save(CreateSiteRequest createSiteRequest) throws NotFoundException {
        LOGGER.info("Saving site");
        Site site = createSiteRequestConverter.from(createSiteRequest);
        site.setCompany(companyRepository.findByUser(getUser()).get());
        if (site.getManager() != null) {
            site.setManager(managerRepository.findByUserUsername(site.getManager().getUser().getUsername())
                    .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
        }
        return siteRepository.save(site).getId();
    }

    @Secured({"ROLE_COMPANY", "ROLE_MANAGER"})
    public void update(UpdateSiteRequest updateSiteRequest) throws NotFoundException, UnauthorizedAccessException {
        LOGGER.info("Updating site");
        Site site = updateSiteRequestConverter.from(updateSiteRequest);
        if (!siteRepository.existsById(site.getId())) {
            throw new NotFoundException(Constants.INVALID_SITE_ID);
        }
        site.setCompany(siteRepository.findOne(site.getId()).getCompany());
        site.setManager(siteRepository.findOne(site.getId()).getManager());
        siteValidator.validate(site);
        siteRepository.save(site);
    }

    @Secured("ROLE_COMPANY")
    public void delete(long id) throws NotFoundException, UnauthorizedAccessException, CollisionException {
        LOGGER.info("Deleting site by ID {}", id);
        Site site = siteRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND));
        siteValidator.validate(site);
        deleteSite.delete(site);
    }

    @Secured("ROLE_COMPANY")
    public void setManagerById(long siteId, long managerId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.info("Setting manager by ID {} to site ID {}", managerId, siteId);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND));
        setManager(manager, siteId);
    }

    @Secured("ROLE_COMPANY")
    public void setManagerByUserId(long siteId, long managerUserId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.info("Setting manager by user ID {} to site ID {}", managerUserId, siteId);
        Manager manager = managerRepository.findByUserId(managerUserId)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND));
        setManager(manager, siteId);
    }

    @Secured("ROLE_COMPANY")
    public void setManagerByUsername(long siteId, String managerUsername)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.info("Setting manager by username {} to site ID {}", managerUsername, siteId);
        Manager manager = managerRepository.findByUserUsername(managerUsername)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND));
        setManager(manager, siteId);
    }

    @Secured("ROLE_COMPANY")
    public List<SiteResponse> getByCompany() {
        LOGGER.info("Providing sites to company owner");
        return siteRepository.findByCompany(companyRepository.findByUser(getUser()).get())
                .stream().map(siteResponseConverter::from).collect(Collectors.toList());
    }

    @Secured("ROLE_MANAGER")
    public SiteResponse getByManager() throws NotFoundException {
        LOGGER.info("Providing site to its manager");
        return siteResponseConverter.from(siteRepository.findByManager(managerRepository.findByUser(getUser()).get())
                .orElseThrow(() -> new NotFoundException(Constants.NO_MANAGED_SITE)));
    }

    public SiteResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing site by ID {}", id);
        return siteResponseConverter.from(siteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND)));
    }

    public List<SiteResponse> getByCompanyName(String companyName) {
        LOGGER.info("Providing sites of company named {}", companyName);
        return siteRepository.findByCompanyName(companyName).stream().map(siteResponseConverter::from)
                .collect(Collectors.toList());
    }

    private void setManager(Manager manager, long siteId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.debug("Setting manager");
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND));
        siteValidator.validate(site);
        if (siteRepository.findByManager(manager).isPresent()) {
            throw new ManagerCollisionException(Constants.MANAGER_ALREADY_SITE_MANAGER);
        }
        site.setManager(manager);
        siteRepository.save(site);
    }

    private User getUser() {
        LOGGER.trace("Getting user");
        return SecurityUtils.getLoggedInUser();
    }
}
