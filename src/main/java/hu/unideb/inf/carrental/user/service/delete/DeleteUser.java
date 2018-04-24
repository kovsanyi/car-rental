package hu.unideb.inf.carrental.user.service.delete;

import hu.unideb.inf.carrental.commons.constant.Constants;
import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.commons.domain.user.UserRepository;
import hu.unideb.inf.carrental.commons.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUser {
    public static final Logger LOGGER = LoggerFactory.getLogger(DeleteUser.class);

    private final UserRepository userRepository;

    @Autowired
    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   public void delete(User user) {
        LOGGER.info("Deleting user named {}", user.getUsername());
        userRepository.delete(user);
    }
}
