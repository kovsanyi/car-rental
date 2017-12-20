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

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final DeleteCustomer deleteCustomer;

    private final CreateCustomerRequestConverter createCustomerRequestConverter;
    private final UpdateCustomerRequestConverter updateCustomerRequestConverter;
    private final CustomerResponseConverter customerResponseConverter;

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

    public long save(CreateCustomerRequest createCustomerRequest)
            throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        LOGGER.info("Saving customer");
        Customer customer = createCustomerRequestConverter.from(createCustomerRequest);
        customer.getUser().setRole(UserRole.ROLE_CUSTOMER);
        userService.save(customer.getUser());
        return customerRepository.save(customer).getId();
    }

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

    @Secured("ROLE_CUSTOMER")
    public void delete() throws CarInRentException {
        LOGGER.info("Deleting customer");
        deleteCustomer.delete(getCustomer());
    }

    @Secured("ROLE_CUSTOMER")
    public CustomerResponse get() {
        LOGGER.info("Providing customer details");
        return customerResponseConverter.from(customerRepository.findByUser(getUser()).get());
    }

    public CustomerResponse getById(long id) throws NotFoundException {
        LOGGER.info("Providing customer details by ID {}", id);
        return customerResponseConverter.from(customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.CUSTOMER_NOT_FOUND)));
    }

    public CustomerResponse getByUserId(long userId) throws NotFoundException {
        LOGGER.info("Providing customer details by user ID {}", userId);
        return customerResponseConverter.from(customerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Constants.CUSTOMER_NOT_FOUND)));
    }

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
}
