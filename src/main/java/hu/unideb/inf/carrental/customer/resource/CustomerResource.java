package hu.unideb.inf.carrental.customer.resource;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.CarInRentException;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import hu.unideb.inf.carrental.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(Constants.PATH_CUSTOMER)
public class CustomerResource {
    private final CustomerService customerService;

    @Autowired
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(SAVE)
    public ResponseEntity<?> save(@Valid @RequestBody CreateCustomerRequest createCustomerRequest)
            throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        return new ResponseEntity<>(new CreatedResponse(customerService.save(createCustomerRequest)), HttpStatus.CREATED);
    }

    @PutMapping(UPDATE)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        customerService.update(updateCustomerRequest);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<?> delete() throws CarInRentException {
        customerService.delete();
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping(GET_ROOT)
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(customerService.get(), HttpStatus.OK);
    }

    @GetMapping(GET_BY_ID)
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(customerService.getById(id), HttpStatus.OK);
    }

    @GetMapping(GET_BY_USER_ID)
    public ResponseEntity<?> getByUserId(@PathVariable("userId") long userId) throws NotFoundException {
        return new ResponseEntity<>(customerService.getByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(GET_BY_USERNAME)
    public ResponseEntity<?> getByUsername(@PathVariable("username") String username) throws NotFoundException {
        return new ResponseEntity<>(customerService.getByUsername(username), HttpStatus.OK);
    }

    public static final String SAVE = "/save";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String GET_ROOT = "/";
    public static final String GET_BY_ID = "/id/{id}";
    public static final String GET_BY_USER_ID = "/userId/{userId}";
    public static final String GET_BY_USERNAME = "/username/{username}";
}
