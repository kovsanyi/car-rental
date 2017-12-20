package hu.unideb.inf.carrental.company.service.converter;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.company.resource.model.CreateCompanyRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(CreateCompanyRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CreateCompanyRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Company from(CreateCompanyRequest createCompanyRequest) {
        logger.info("Converting CreateCompanyRequest to Company");
        return modelMapper.map(createCompanyRequest, Company.class);
    }
}
