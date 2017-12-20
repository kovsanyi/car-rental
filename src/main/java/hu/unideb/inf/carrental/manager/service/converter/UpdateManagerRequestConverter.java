package hu.unideb.inf.carrental.manager.service.converter;

import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.manager.resource.model.UpdateManagerRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateManagerRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(UpdateManagerRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public UpdateManagerRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Manager from(UpdateManagerRequest updateManagerRequest) {
        logger.info("Converting UpdateManagerRequest to Manager");
        return modelMapper.map(updateManagerRequest, Manager.class);
    }
}
