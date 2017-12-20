package hu.unideb.inf.carrental.manager.service.converter;

import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.manager.resource.model.CreateManagerRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateManagerRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(CreateManagerRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CreateManagerRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Manager from(CreateManagerRequest createManagerRequest) {
        logger.info("Converting CreateManagerRequest to Manager");
        return modelMapper.map(createManagerRequest, Manager.class);
    }
}
