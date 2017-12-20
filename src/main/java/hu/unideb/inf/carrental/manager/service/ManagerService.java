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

@Service
public class ManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerService.class);

    private final UserService userService;
    private final ManagerRepository managerRepository;
    private final DeleteManager deleteManager;

    private final CreateManagerRequestConverter createManagerRequestConverter;
    private final UpdateManagerRequestConverter updateManagerRequestConverter;
    private final ManagerResponseConverter managerResponseConverter;

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

    public long save(CreateManagerRequest createManagerRequest)
            throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        LOGGER.info("Saving manager");
        Manager manager = createManagerRequestConverter.from(createManagerRequest);
        manager.getUser().setRole(UserRole.ROLE_MANAGER);
        userService.save(manager.getUser());
        return managerRepository.save(manager).getId();
    }

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

    @Secured("ROLE_MANAGER")
    public void delete() {
        LOGGER.info("Deleting manager");
        deleteManager.delete(getManager());
    }

    @Secured("ROLE_MANAGER")
    public ManagerResponse get() {
        LOGGER.info("Providing manager details");
        return managerResponseConverter.from(getManager());
    }

    public ManagerResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing manager details by ID");
        return managerResponseConverter.from(managerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
    }

    public ManagerResponse getByUserId(long userId) throws NotFoundException {
        LOGGER.info("Providing manager details by user ID");
        return managerResponseConverter.from(managerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
    }

    public ManagerResponse getByUsername(String username) throws NotFoundException {
        LOGGER.info("Providing manager details by ID");
        return managerResponseConverter.from(managerRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.MANAGER_NOT_FOUND)));
    }

    private Manager getManager() {
        return managerRepository.findByUser(SecurityUtils.getLoggedInUser()).get();
    }
}
