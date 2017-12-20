package hu.unideb.inf.carrental.site.service.validator;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.commons.domain.manager.ManagerRepository;
import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteValidator {
    private static final Logger logger = LoggerFactory.getLogger(SiteValidator.class);

    private final SiteRepository siteRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public SiteValidator(SiteRepository siteRepository, ManagerRepository managerRepository) {
        this.siteRepository = siteRepository;
        this.managerRepository = managerRepository;
    }

    public void validate(Site site) throws UnauthorizedAccessException {
        logger.info("Validating site");
        UserRole role = SecurityUtils.getLoggedInUser().getRole();
        Site originalSite = siteRepository.findById(site.getId()).get();
        Company companyOfOriginalSite = originalSite.getCompany();
        Long userId = SecurityUtils.getLoggedInUser().getId();
        if (role.equals(UserRole.ROLE_COMPANY)) {
            if (!companyOfOriginalSite.getUser().getId().equals(userId)) {
                throw new UnauthorizedAccessException(Constants.NO_RIGHTS);
            }
        } else if (role.equals(UserRole.ROLE_MANAGER)) {
            Manager manager = originalSite.getManager();
            if (!((manager != null) && managerRepository.findByUserId(userId).get().getId().equals(manager.getId()))) {
                throw new UnauthorizedAccessException(Constants.NO_RIGHTS);
            }
        }
    }
}
