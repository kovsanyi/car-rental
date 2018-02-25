package hu.unideb.inf.carrental.site.resource;

import hu.unideb.inf.carrental.commons.exception.CollisionException;
import hu.unideb.inf.carrental.commons.exception.ManagerCollisionException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UnauthorizedAccessException;
import hu.unideb.inf.carrental.commons.model.CreatedResponse;
import hu.unideb.inf.carrental.commons.model.SuccessResponse;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import hu.unideb.inf.carrental.site.resource.model.UpdateSiteRequest;
import hu.unideb.inf.carrental.site.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static hu.unideb.inf.carrental.commons.constant.Constants.PATH_SITE;

@RestController
@RequestMapping(PATH_SITE)
public class SiteResource {

    @PostMapping(SAVE)
    public ResponseEntity<?> save(@Valid @RequestBody CreateSiteRequest createSiteRequest) throws NotFoundException {
        return new ResponseEntity<>(new CreatedResponse(siteService.save(createSiteRequest)), HttpStatus.CREATED);
    }

    @PutMapping(UPDATE)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateSiteRequest updateSiteRequest)
            throws NotFoundException, UnauthorizedAccessException {
        siteService.update(updateSiteRequest);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") long id)
            throws NotFoundException, UnauthorizedAccessException, CollisionException {
        siteService.delete(id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @PutMapping(SET_MANAGER_BY_ID)
    public ResponseEntity<?> setManagerById(@PathVariable("siteId") long siteId,
                                            @PathVariable("id") long id)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        siteService.setManagerById(siteId, id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @PutMapping(SET_MANAGER_BY_USER_ID)
    public ResponseEntity<?> setManagerByUserId(@PathVariable("siteId") long siteId,
                                                @PathVariable("userId") long userId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        siteService.setManagerByUserId(siteId, userId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @PutMapping(SET_MANAGER_BY_USERNAME)
    public ResponseEntity<?> setManagerByUsername(@PathVariable("siteId") long siteId,
                                                  @PathVariable("username") String username)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        siteService.setManagerByUsername(siteId, username);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @GetMapping(GET_BY_COMPANY)
    public ResponseEntity<?> getByCompany() {
        return new ResponseEntity<>(siteService.getByCompany(), HttpStatus.OK);
    }

    @GetMapping(GET_BY_MANAGER)
    public ResponseEntity<?> getByManager() throws NotFoundException {
        return new ResponseEntity<>(siteService.getByManager(), HttpStatus.OK);
    }

    @GetMapping(GET_BY_ID)
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(siteService.getById(id), HttpStatus.OK);
    }

    @GetMapping(GET_BY_COMPANY_NAME)
    public ResponseEntity<?> getByCompanyName(@PathVariable("companyName") String companyName)
            throws NotFoundException {
        return new ResponseEntity<>(siteService.getByCompanyName(companyName), HttpStatus.OK);
    }

    @Autowired
    public SiteResource(SiteService siteService) {
        this.siteService = siteService;
    }

    private final SiteService siteService;

    public static final String SAVE = "/save";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete/{id}";
    public static final String SET_MANAGER_BY_ID = "/{siteId}/setManager/id/{id}";
    public static final String SET_MANAGER_BY_USER_ID = "/{siteId}/setManager/userId/{userId}";
    public static final String SET_MANAGER_BY_USERNAME = "/{siteId}/setManager/username/{username}";
    public static final String GET_BY_COMPANY = "/company";
    public static final String GET_BY_MANAGER = "/manager";
    public static final String GET_BY_ID = "/id/{id}";
    public static final String GET_BY_COMPANY_NAME = "/companyName/{companyName}";
}
