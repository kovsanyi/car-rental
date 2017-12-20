package hu.unideb.inf.carrental.user.service.validator;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.UserRepository;
import hu.unideb.inf.carrental.commons.exception.EmailAlreadyInUseException;
import hu.unideb.inf.carrental.commons.exception.UsernameAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {
    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO if role not set
    public void validate(User user) throws UsernameAlreadyInUseException, EmailAlreadyInUseException {
        logger.info("Validating user");
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn(Constants.USERNAME_ALREADY_EXISTS);
            throw new UsernameAlreadyInUseException(Constants.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn(Constants.EMAIL_ALREADY_EXISTS);
            throw new EmailAlreadyInUseException(Constants.EMAIL_ALREADY_EXISTS);
        }
        if (user.getRole() == null) {
            logger.error(Constants.ERROR_ROLE_NOT_SET);
        }
    }
}
