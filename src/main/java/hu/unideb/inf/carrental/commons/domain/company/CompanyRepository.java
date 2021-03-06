package hu.unideb.inf.carrental.commons.domain.company;

import hu.unideb.inf.carrental.commons.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findById(long id);

    Optional<Company> findByUser(User user);

    Optional<Company> findByUserId(long id);

    Optional<Company> findByUserUsername(String owner);

    Optional<Company> findByName(String name);

    Optional<Company> findByEmail(String email);

    boolean existsById(long id);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    @Override
    List<Company> findAll();
}
