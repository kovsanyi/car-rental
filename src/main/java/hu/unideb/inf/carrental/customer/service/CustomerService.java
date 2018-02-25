package hu.unideb.inf.carrental.customer.service;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.commons.domain.customer.CustomerRepository;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.enumeration.UserRole;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import hu.unideb.inf.carrental.customer.resource.model.CustomerResponse;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import hu.unideb.inf.carrental.customer.service.converter.CreateCustomerRequestConverter;
import hu.unideb.inf.carrental.customer.service.converter.CustomerResponseConverter;
import hu.unideb.inf.carrental.customer.service.converter.UpdateCustomerRequestConverter;
import hu.unideb.inf.carrental.customer.service.delete.DeleteCustomer;
import hu.unideb.inf.carrental.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Service class for registering and managing a customer as a user.
 *
 * @see CreateCustomerRequest
 * @see UpdateCustomerRequest
 * @see CustomerResponse
 */
@Service
public class CustomerService {
    /**
     * Registers a new customer as a user. If the registration is successful, it returns the ID of the registered
     * customer.
     *
     * @param createCustomerRequest a request object to register a new customer
     * @return the ID of the registered customer
     * @throws UsernameAlreadyInUseException if the given username is already in use
     * @throws EmailAlreadyInUseException    if the given user email is already in use
     */
    @Transactional
    public long save(CreateCustomerRequest createCustomerRequest)
            throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        LOGGER.info("Saving customer");
        Customer customer = createCustomerRequestConverter.from(createCustomerRequest);
        customer.getUser().setRole(UserRole.ROLE_CUSTOMER);
        userService.save(customer.getUser());
        return customerRepository.save(customer).getId();
    }

    /**
     * Updates the details of the logged in customer using the given {@code UpdateCustomerRequest}.<br>
     * <b>This method can only be called by a customer.</b>
     *
     * @param updateCustomerRequest a request object to update the details of the logged in customer
     */
    @Secured("ROLE_CUSTOMER")
    public void update(UpdateCustomerRequest updateCustomerRequest) {
        LOGGER.info("Updating customer");
        Customer update = updateCustomerRequestConverter.from(updateCustomerRequest);
        User user = getUser();
        Customer customer = getCustomer();
        update.setId(customer.getId());
        update.setUser(user);
        customerRepository.save(update);
    }

    /**
     * Deletes the logged in customer.<br>
     * <b>This method can only be called by a customer.</b>
     *
     * @throws CarInRentException if the customer can not be deleted due to it has active reservation
     * @see hu.unideb.inf.carrental.reservation.service.ReservationService
     */
    @Secured("ROLE_CUSTOMER")
    public void delete() throws CarInRentException {
        LOGGER.info("Deleting customer");
        deleteCustomer.delete(getCustomer());
    }

    /**
     * Returns the details of the logged in customer.<br>
     * <b>This method can only be called by a customer.</b>
     *
     * @return the details of the logged in customer
     */
    @Secured("ROLE_CUSTOMER")
    public CustomerResponse get() {
        LOGGER.info("Providing customer details");
        return customerResponseConverter.from(customerRepository.findByUser(getUser()).get());
    }

    /**
     * Returns the customer details by <b>customer ID</b>.<br>
     * <b>Customer ID does not equal to user ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the customer ID is unique only among the customers.
     *
     * @param id ID of the customer
     * @return the customer details by customer ID
     * @throws NotFoundException if the customer ID does not identify a customer
     */
    public CustomerResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing customer details by ID {}", id);
        return customerResponseConverter.from(customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CUSTOMER_NOT_FOUND)));
    }

    /**
     * Returns the customer details by <b>user ID</b>.<br>
     * <b>User ID does not equal to customer ID!</b> The user ID is unique to the whole system including company,
     * manager and customer IDs, whereas the customer ID is unique only among the customers.
     *
     * @param userId user ID of the customer
     * @return the customer details by user ID
     * @throws NotFoundException if the user ID does not identify a customer
     */
    public CustomerResponse getByUserId(long userId) throws NotFoundException {
        LOGGER.info("Providing customer details by user ID {}", userId);
        return customerResponseConverter.from(customerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Constants.CUSTOMER_NOT_FOUND)));
    }

    /**
     * Returns the customer details by username.
     *
     * @param username username of the customer
     * @return the customer details by username
     * @throws NotFoundException if the username does not identify a customer
     */
    public CustomerResponse getByUsername(String username) throws NotFoundException {
        LOGGER.info("Providing manager details by username {}", username);
        return customerResponseConverter.from(customerRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.CUSTOMER_NOT_FOUND)));
    }

    private Customer getCustomer() {
        return customerRepository.findByUser(getUser()).get();
    }

    private User getUser() {
        return SecurityUtils.getLoggedInUser();
    }

    @Autowired
    public CustomerService(UserService userService, CustomerRepository customerRepository, DeleteCustomer deleteCustomer,
                           CreateCustomerRequestConverter createCustomerRequestConverter,
                           UpdateCustomerRequestConverter updateCustomerRequestConverter,
                           CustomerResponseConverter customerResponseConverter) {
        this.userService = userService;
        this.customerRepository = customerRepository;
        this.deleteCustomer = deleteCustomer;
        this.createCustomerRequestConverter = createCustomerRequestConverter;
        this.updateCustomerRequestConverter = updateCustomerRequestConverter;
        this.customerResponseConverter = customerResponseConverter;
    }

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final DeleteCustomer deleteCustomer;

    private final CreateCustomerRequestConverter createCustomerRequestConverter;
    private final UpdateCustomerRequestConverter updateCustomerRequestConverter;
    private final CustomerResponseConverter customerResponseConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
}
