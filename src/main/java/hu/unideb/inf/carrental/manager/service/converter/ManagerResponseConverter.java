package hu.unideb.inf.carrental.manager.service.converter;

import hu.unideb.inf.carrental.commons.domain.manager.Manager;
import hu.unideb.inf.carrental.manager.resource.model.ManagerResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(ManagerResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public ManagerResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ManagerResponse from(Manager manager) {
        logger.info("Converting Manager to ManagerResponse");
        return modelMapper.map(manager, ManagerResponse.class);
    }
}
