package hu.unideb.inf.carrental.customer.service.delete;

import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.commons.domain.customer.CustomerRepository;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.reservation.service.delete.DeleteReservation;
import hu.unideb.inf.carrental.user.service.delete.DeleteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCustomer.class);

    private final CustomerRepository customerRepository;
    private final DeleteReservation deleteReservation;
    private final DeleteUser deleteUser;

    @Autowired
    public DeleteCustomer(CustomerRepository customerRepository, DeleteReservation deleteReservation, DeleteUser deleteUser) {
        this.customerRepository = customerRepository;
        this.deleteReservation = deleteReservation;
        this.deleteUser = deleteUser;
    }

    public void delete(Customer customer) throws CarInRentException {
        LOGGER.trace("Deleting customer ID {}", customer.getId());
        deleteReservation.deleteCustomer(customer);
        customerRepository.delete(customer);
        deleteUser.delete(SecurityUtils.getLoggedInUser());
    }
}
