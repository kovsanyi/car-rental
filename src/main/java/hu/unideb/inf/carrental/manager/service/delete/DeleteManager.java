package hu.unideb.inf.carrental.manager.service.delete;

import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.commons.domain.manager.ManagerRepository;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.UserRepository;
import hu.unideb.inf.carrental.user.service.delete.DeleteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteManager.class);

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final SiteRepository siteRepository;
    private final DeleteUser deleteUser;

    @Autowired
    public DeleteManager(UserRepository userRepository, ManagerRepository managerRepository,
                         SiteRepository siteRepository, DeleteUser deleteUser) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.siteRepository = siteRepository;
        this.deleteUser = deleteUser;
    }

    @Transactional
    public void delete(Manager manager) {
        LOGGER.trace("Deleting manager ID {}", manager.getId());
        if (siteRepository.findByManager(manager).isPresent()) {
            Site site = siteRepository.findByManager(manager).get();
            site.setManager(null);
            siteRepository.save(site);
        }
        User user = userRepository.findById(manager.getUser().getId()).get();
        managerRepository.delete(manager);
        deleteUser.delete(user);
    }
}
