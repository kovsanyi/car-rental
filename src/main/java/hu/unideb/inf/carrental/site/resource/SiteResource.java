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

@RestController
@RequestMapping("/api/site")
public class SiteResource {
    private final SiteService siteService;

    @Autowired
    public SiteResource(SiteService siteService) {
        this.siteService = siteService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody CreateSiteRequest createSiteRequest) throws NotFoundException {
        return new ResponseEntity<>(new CreatedResponse(siteService.save(createSiteRequest)), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateSiteRequest updateSiteRequest)
            throws NotFoundException, UnauthorizedAccessException {
        siteService.update(updateSiteRequest);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id)
            throws NotFoundException, UnauthorizedAccessException, CollisionException {
        siteService.delete(id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{siteId}/setManager/id/{id}")
    public ResponseEntity<?> setManagerById(@PathVariable("siteId") long siteId,
                                            @PathVariable("id") long id)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        siteService.setManagerById(siteId, id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{siteId}/setManager/userId/{userId}")
    public ResponseEntity<?> setManagerByUserId(@PathVariable("siteId") long siteId,
                                                @PathVariable("userId") long userId)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        siteService.setManagerByUserId(siteId, userId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{siteId}/setManager/username/{username}")
    public ResponseEntity<?> setManagerByUsername(@PathVariable("siteId") long siteId,
                                                  @PathVariable("username") String username)
            throws NotFoundException, UnauthorizedAccessException, ManagerCollisionException {
        siteService.setManagerByUsername(siteId, username);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/company")
    public ResponseEntity<?> getByCompany() {
        return new ResponseEntity<>(siteService.getByCompany(), HttpStatus.OK);
    }

    @GetMapping("/manager")
    public ResponseEntity<?> getByManager() throws NotFoundException {
        return new ResponseEntity<>(siteService.getByManager(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(siteService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/companyName/{companyName}")
    public ResponseEntity<?> getByCompanyName(@PathVariable("companyName") String companyName) {
        return new ResponseEntity<>(siteService.getByCompanyName(companyName), HttpStatus.OK);
    }
}
