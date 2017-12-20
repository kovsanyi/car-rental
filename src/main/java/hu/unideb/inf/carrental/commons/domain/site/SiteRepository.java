package hu.unideb.inf.carrental.commons.domain.site;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends CrudRepository<Site, Long> {
    Optional<Site> findById(Long id);

    List<Site> findByCompany(Company company);

    List<Site> findByCompanyName(String CompanyName);

    Optional<Site> findByManager(Manager manager);

    boolean existsById(long id);
}
