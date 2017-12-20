package hu.unideb.inf.carrental.user.service.converter;

import hu.unideb.inf.carrental.commons.domain.user.User;
import hu.unideb.inf.carrental.user.resource.model.UserResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(UserResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public UserResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserResponse from(User user) {
        logger.info("Converting User to UserResponse");
        return modelMapper.map(user, UserResponse.class);
    }
}
