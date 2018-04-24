package hu.unideb.inf.carrental.user.service;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.UserRepository;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import hu.unideb.inf.carrental.commons.security.SecurityUtils;
import hu.unideb.inf.carrental.user.resource.model.UserResponse;
import hu.unideb.inf.carrental.user.service.converter.UserResponseConverter;
import hu.unideb.inf.carrental.user.service.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    public void save(User user) throws EmailAlreadyInUseException, UsernameAlreadyInUseException {
        logger.info("Saving user");
        user.setUsername(user.getUsername().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userValidator.validate(user);
        userRepository.save(user);
    }

    @Secured({"ROLE_CUSTOMER", "ROLE_COMPANY", "ROLE_MANAGER"})
    public UserResponse get() {
        logger.info("Providing details of logged in user");
        return userResponseConverter.from(userRepository.findOne(SecurityUtils.getLoggedInUser().getId()));
    }

    public UserResponse getById(long id) throws NotFoundException {
        logger.info("Providing user details by ID");
        return userResponseConverter.from(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND)));
    }

    public UserResponse getByUsername(String username) throws NotFoundException {
        logger.info("Providing user details by username");
        return userResponseConverter.from(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder, UserValidator userValidator,
                       UserRepository userRepository, UserResponseConverter userResponseConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
        this.userRepository = userRepository;
        this.userResponseConverter = userResponseConverter;
    }

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    private final UserResponseConverter userResponseConverter;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
}
