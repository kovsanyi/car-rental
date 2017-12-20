package hu.unideb.inf.carrental.user.resource;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PATH_USER)
public class UserResource {
    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(GET_ROOT)
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(userService.get(), HttpStatus.OK);
    }

    @GetMapping(GET_BY_ID)
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws NotFoundException {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping(GET_BY_USERNAME)
    public ResponseEntity<?> getByUsername(@PathVariable("username") String username) throws NotFoundException {
        return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.OK);
    }

    public static final String GET_ROOT = "/";
    public static final String GET_BY_ID = "/id/{id}";
    public static final String GET_BY_USERNAME = "/username/{username}";
}
