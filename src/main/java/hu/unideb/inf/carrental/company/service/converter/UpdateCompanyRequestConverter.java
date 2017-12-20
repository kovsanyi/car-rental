package hu.unideb.inf.carrental.company.service.converter;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.company.resource.model.UpdateCompanyRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCompanyRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(UpdateCompanyRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public UpdateCompanyRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Company from(UpdateCompanyRequest updateCompanyRequest) {
        logger.info("Converting UpdateCompanyRequest to Company");
        return modelMapper.map(updateCompanyRequest, Company.class);
    }
}
