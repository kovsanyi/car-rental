package hu.unideb.inf.carrental.commons.domain.customer;

import hu.unideb.inf.carrental.commons.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findById(long id);

    Optional<Customer> findByUser(User user);

    Optional<Customer> findByUserId(long id);

    Optional<Customer> findByUserUsername(String username);
}
