package hu.unideb.inf.carrental.company.resource;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.*;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
import hu.unideb.inf.carrental.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(Constants.PATH_COMPANY)
public class CompanyResource {
    private final CompanyService companyService;

    @Autowired
    public CompanyResource(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(SAVE)
    public ResponseEntity<?> save(@Valid @RequestBody CreateCompanyRequest createCompanyRequest)
            throws EmailAlreadyInUseException, NameAlreadyInUseException, UsernameAlreadyInUseException,
            CompanyEmailAlreadyInUseException {
        return new ResponseEntity<>(new CreatedResponse(companyService.save(createCompanyRequest)), HttpStatus.CREATED);
    }

    @PutMapping(UPDATE)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateCompanyRequest updateCompanyRequest)
            throws NameAlreadyInUseException, CompanyEmailAlreadyInUseException {
        companyService.update(updateCompanyRequest);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<?> delete() throws CollisionException, CarInRentException {
        companyService.delete();
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping(GET_ROOT)
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(companyService.get(), HttpStatus.OK);
    }

    @GetMapping(GET_BY_ID)
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(companyService.getById(id), HttpStatus.OK);
    }

    @GetMapping(GET_BY_USER_ID)
    public ResponseEntity<?> getByUserId(@PathVariable("userId") long userId) throws NotFoundException {
        return new ResponseEntity<>(companyService.getByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(GET_BY_USERNAME)
    public ResponseEntity<?> getByUsername(@PathVariable("username") String username) throws NotFoundException {
        return new ResponseEntity<>(companyService.getByUsername(username), HttpStatus.OK);
    }

    @GetMapping(GET_BY_NAME)
    public ResponseEntity<?> getByName(@PathVariable("name") String name) throws NotFoundException {
        return new ResponseEntity<>(companyService.getByName(name), HttpStatus.OK);
    }

    @GetMapping(GET_ALL)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(companyService.getAll(), HttpStatus.OK);
    }

    public static final String SAVE = "/save";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String GET_ROOT = "/";
    public static final String GET_BY_ID = "/id/{id}";
    public static final String GET_BY_USER_ID = "/userId/{userId}";
    public static final String GET_BY_USERNAME = "/username/{username}";
    public static final String GET_BY_NAME = "/name/{name}";
    public static final String GET_ALL = "/all";
}
