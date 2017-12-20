package hu.unideb.inf.carrental.company.service.delete;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.company.CompanyRepository;
import hu.unideb.inf.carrental.commons.domain.site.SiteRepository;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.CollisionException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.reservation.service.delete.DeleteReservation;
import hu.unideb.inf.carrental.user.service.delete.DeleteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteCompany {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCompany.class);

    private final CompanyRepository companyRepository;
    private final SiteRepository siteRepository;
    private final DeleteUser deleteUser;
    private final DeleteReservation deleteReservation;

    @Autowired
    public DeleteCompany(CompanyRepository companyRepository, SiteRepository siteRepository, DeleteUser deleteUser,
                         DeleteReservation deleteReservation) {
        this.companyRepository = companyRepository;
        this.siteRepository = siteRepository;
        this.deleteUser = deleteUser;
        this.deleteReservation = deleteReservation;
    }

    public void delete(Company company) throws CollisionException, CarInRentException {
        LOGGER.trace("Deleting company ID {}", company.getId());
        if (!siteRepository.findByCompany(company).isEmpty()) {
            throw new CollisionException(Constants.COMPANY_HAS_SITES);
        }
        deleteReservation.deleteCompany(company);
        companyRepository.delete(company);
        deleteUser.delete(SecurityUtils.getLoggedInUser());
    }
}
