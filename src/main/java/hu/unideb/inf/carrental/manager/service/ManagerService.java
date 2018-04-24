package hu.unideb.inf.carrental.manager.service;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.commons.domain.manager.ManagerRepository;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.manager.resource.model.CreateManagerRequest;
import hu.unideb.inf.carrental.manager.resource.model.ManagerResponse;
import hu.unideb.inf.carrental.manager.resource.model.UpdateManagerRequest;
import hu.unideb.inf.carrental.manager.service.converter.CreateManagerRequestConverter;
import hu.unideb.inf.carrental.manager.service.converter.ManagerResponseConverter;
import hu.unideb.inf.carrental.manager.service.converter.UpdateManagerRequestConverter;
import hu.unideb.inf.carrental.manager.service.delete.DeleteManager;
import hu.unideb.inf.carrental.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Service class for registering and managing a manager as a user.
 *
 * @see CreateManagerRequest
 * @see UpdateManagerRequest
 * @see ManagerResponse
 */
@Service
public class ManagerService {
    /**
     * Registers a new manager as a user. If the registration is successful, it returns the ID of the registered
     * manager.
     *
     * @param createManagerRequest a request object to register a new manager
     * @return the ID of the registered manager
     * @throws UsernameAlreadyInUseException if the given username is already in use
     * @throws EmailAlreadyInUseException    if the given user email is already in use
     */
    @Transactional
    public long save(CreateManagerRequest createManagerRequest)
            throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        LOGGER.info("Saving manager");
        Manager manager = createManagerRequestConverter.from(createManagerRequest);
        manager.getUser().setRole(UserRole.ROLE_MANAGER);
        userService.save(manager.getUser());
        return managerRepository.save(manager).getId();
    }

    /**
     * Updates the details of the logged in manager using the given {@code UpdateManagerRequest}.<br>
     * <b>This method can only be called by a manager.</b>
     *
     * @param updateManagerRequest a request object to register a new manager
     */
    @Secured("ROLE_MANAGER")
    public void update(UpdateManagerRequest updateManagerRequest) {
        LOGGER.info("Updating manager");
        Manager update = updateManagerRequestConverter.from(updateManagerRequest);
        User user = SecurityUtils.getLoggedInUser();
        Manager manager = managerRepository.findByUserId(user.getId()).get();
        update.setId(manager.getId());
        update.setUser(user);
        managerRepository.save(update);
    }

    /**
     * Deletes the logged in manager.<br>
     * <b>This method can only be called by a manager.</b>
     */
    @Secured("ROLE_MANAGER")
    public void delete() {
        LOGGER.info("Deleting manager");
        deleteManager.delete(getManager());
    }

    /**
     * Returns the details of the logged in manager.<br>
     * <b>This method can only be called by a manager.</b>
     *
     * @return the details of the logged in manager
     */
    @Secured("ROLE_MANAGER")
    public ManagerResponse get() {
        LOGGER.info("Providing manager details");
        return managerResponseConverter.from(getManager());
    }

    /**
     * Returns the manager details by <b>manager ID</b>.<br>
     * <b>Manager ID does not equal to user ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the manager ID is unique only among the managers.
     *
     * @param id ID of the manager
     * @return the company owner details by company ID
     * @throws NotFoundException if the manager ID does not identify a manager
     */
    public ManagerResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing manager details by ID");
        return managerResponseConverter.from(managerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
    }

    /**
     * Returns the manager details by <b>user ID</b>.<br>
     * <b>User ID does not equal to manager ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the manager ID is unique only among the managers.
     *
     * @param userId user ID of the manager
     * @return the manager details by user ID
     * @throws NotFoundException if the user ID does not identify a manager
     */
    public ManagerResponse getByUserId(long userId) throws NotFoundException {
        LOGGER.info("Providing manager details by user ID");
        return managerResponseConverter.from(managerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
    }

    /**
     * Returns the manager details by username.
     *
     * @param username username of the manager
     * @return the manager details by username
     * @throws NotFoundException if the username does not identify a manager
     */
    public ManagerResponse getByUsername(String username) throws NotFoundException {
        LOGGER.info("Providing manager details by username");
        return managerResponseConverter.from(managerRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
    }

    private Manager getManager() {
        return managerRepository.findByUser(SecurityUtils.getLoggedInUser()).get();
    }

    @Autowired
    public ManagerService(UserService userService, ManagerRepository managerRepository, DeleteManager deleteManager,
                          CreateManagerRequestConverter createManagerRequestConverter,
                          UpdateManagerRequestConverter updateManagerRequestConverter,
                          ManagerResponseConverter managerResponseConverter) {
        this.userService = userService;
        this.managerRepository = managerRepository;
        this.deleteManager = deleteManager;
        this.createManagerRequestConverter = createManagerRequestConverter;
        this.updateManagerRequestConverter = updateManagerRequestConverter;
        this.managerResponseConverter = managerResponseConverter;
    }

    private final UserService userService;
    private final ManagerRepository managerRepository;
    private final DeleteManager deleteManager;

    private final CreateManagerRequestConverter createManagerRequestConverter;
    private final UpdateManagerRequestConverter updateManagerRequestConverter;
    private final ManagerResponseConverter managerResponseConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerService.class);
}
