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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for registering and managing sites.
 *
 * @see CreateSiteRequest
 * @see UpdateSiteRequest
 * @see SiteResponse
 */
@Service
public class SiteService {
    /**
     * Registers a new site to the logged in company (owner). If the registration is successful, it returns the ID of
     * the registered site.
     *
     * @param createSiteRequest a request object to register a new site
     * @return the ID of the registered site
     * @throws NotFoundException if the specified manager (if presented) in {@code CreateSiteRequest} is invalid
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    @Secured("ROLE_COMPANY")
    @Transactional
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

    /**
     * Updates the details of the specified site.<br>
     * <b>This method can only be called by a company owner or a manager.</b>
     *
     * @param updateSiteRequest a request object to update a site
     * @throws NotFoundException           if the specified site ID in {@code UpdateSiteRequest} is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to update the specified site
     */
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

    /**
     * Deletes a site by ID including the registered cars to it.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @param id ID of the site
     * @throws NotFoundException           if the site ID is invalid
     * @throws UnauthorizedAccessException if the logged in company owner has no right to delete the specified site
     * @throws CollisionException          if one of the cars that registered to the site is under rent
     * @see hu.unideb.inf.carrental.car.service.CarService
     */
    @Secured("ROLE_COMPANY")
    public void delete(long id) throws NotFoundException, UnauthorizedAccessException, CollisionException {
        LOGGER.info("Deleting site by ID {}", id);
        Site site = siteRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND));
        siteValidator.validate(site);
        deleteSite.delete(site);
    }

    /**
     * Sets a manager by <b>manager ID</b> to the specified site.<br>
     * <b>Manager ID does not equal to user ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the manager ID is unique only among the managers.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @param siteId    ID of the site
     * @param managerId ID of the manager
     * @throws NotFoundException           if the site ID or the manager ID is invalid
     * @throws UnauthorizedAccessException if the logged in company owner has no right to modify the given site
     * @throws ManagerCollisionException   if the specified manager already has a managed site
     * @see hu.unideb.inf.carrental.manager.service.ManagerService
     */
    @Secured("ROLE_COMPANY")
    public void setManagerById(long siteId, long managerId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.info("Setting manager by ID {} to site ID {}", managerId, siteId);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND));
        setManager(manager, siteId);
    }

    /**
     * Sets a manager by <b>user ID</b> to the specified site.<br>
     * <b>User ID does not equal to manager ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the manager ID is unique only among the managers.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @param siteId        ID of the site
     * @param managerUserId user ID of the manager
     * @throws NotFoundException           if the site ID or the user ID of the manager is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to modify the given site
     * @throws ManagerCollisionException   if the specified manager already has a managed site
     * @see hu.unideb.inf.carrental.manager.service.ManagerService
     */
    @Secured("ROLE_COMPANY")
    public void setManagerByUserId(long siteId, long managerUserId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.info("Setting manager by user ID {} to site ID {}", managerUserId, siteId);
        Manager manager = managerRepository.findByUserId(managerUserId)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND));
        setManager(manager, siteId);
    }

    /**
     * Sets a manager by username to the specified site.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @param siteId          ID of the site
     * @param managerUsername username of the manager
     * @throws NotFoundException           if the site ID or the manager username is invalid
     * @throws UnauthorizedAccessException if the logged in user has no right to modify the given site
     * @throws ManagerCollisionException   if the specified manager already has a managed site
     * @see hu.unideb.inf.carrental.manager.service.ManagerService
     */
    @Secured("ROLE_COMPANY")
    public void setManagerByUsername(long siteId, String managerUsername)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        LOGGER.info("Setting manager by username {} to site ID {}", managerUsername, siteId);
        Manager manager = managerRepository.findByUserUsername(managerUsername)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND));
        setManager(manager, siteId);
    }

    /**
     * Deletes a manager from the specified site.<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @param siteId ID of the site
     * @throws UnauthorizedAccessException if the logged in user has no right to modify the given site
     * @throws NotFoundException           if the site ID is invalid
     */
    @Secured("ROLE_COMPANY")
    public void deleteManagerBySiteId(long siteId) throws UnauthorizedAccessException, NotFoundException {
        LOGGER.info("Deleting manager of site ID {}", siteId);
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND));
        siteValidator.validate(site);
        site.setManager(null);
        siteRepository.save(site);
    }

    /**
     * Returns the details of all the sites connected to logged in company (owner).<br>
     * <b>This method can only be called by a company owner.</b>
     *
     * @return the details of all sites connected to logged in company (owner)
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    @Secured("ROLE_COMPANY")
    @Transactional
    public List<SiteResponse> getByCompany() {
        LOGGER.info("Providing sites to company owner");
        return siteRepository.findByCompany(companyRepository.findByUser(getUser()).get())
                .stream().map(siteResponseConverter::from).collect(Collectors.toList());
    }

    /**
     * Returns the details of the managed site.<br>
     * <b>This method can only be called by a manager.</b>
     *
     * @return the details of the managed site
     * @throws NotFoundException if the manager has not got a managed site
     * @see hu.unideb.inf.carrental.manager.service.ManagerService
     */
    @Secured("ROLE_MANAGER")
    @Transactional
    public SiteResponse getByManager() throws NotFoundException {
        LOGGER.info("Providing site to its manager");
        return siteResponseConverter.from(siteRepository.findByManager(managerRepository.findByUser(getUser()).get())
                .orElseThrow(() -> new NotFoundException(Constants.NO_MANAGED_SITE)));
    }

    /**
     * Returns the site details by ID.
     *
     * @param id ID of the site
     * @return the site details by ID
     * @throws NotFoundException if the site ID is invalid
     */
    @Transactional
    public SiteResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing site by ID {}", id);
        return siteResponseConverter.from(siteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.SITE_NOT_FOUND)));
    }

    /**
     * Returns the details of all sites by company name.
     *
     * @param companyName name of the company
     * @return the site details by company name
     * @throws NotFoundException if the site ID is invalid
     * @see hu.unideb.inf.carrental.company.service.CompanyService
     */
    @Transactional
    public List<SiteResponse> getByCompanyName(String companyName) throws NotFoundException {
        LOGGER.info("Providing sites of company named {}", companyName);
        if (!companyRepository.existsByName(companyName)) {
            throw new NotFoundException(Constants.SITE_NOT_FOUND);
        }
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

    private final SiteRepository siteRepository;
    private final CompanyRepository companyRepository;
    private final ManagerRepository managerRepository;
    private final CarRepository carRepository;
    private final SiteValidator siteValidator;
    private final DeleteSite deleteSite;

    private final CreateSiteRequestConverter createSiteRequestConverter;
    private final UpdateSiteRequestConverter updateSiteRequestConverter;
    private final SiteResponseConverter siteResponseConverter;

    private final static Logger LOGGER = LoggerFactory.getLogger(SiteService.class);
}
