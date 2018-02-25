package hu.unideb.inf.carrental.commons.domain.manager;

import hu.unideb.inf.carrental.commons.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ManagerRepository extends CrudRepository<Manager, Long> {
    Optional<Manager> findById(long id);

    Optional<Manager> findByUser(User user);

    Optional<Manager> findByUserId(long id);

    Optional<Manager> findByUserUsername(String username);
}
