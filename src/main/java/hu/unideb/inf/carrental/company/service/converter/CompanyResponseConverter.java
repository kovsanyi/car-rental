package hu.unideb.inf.carrental.company.service.converter;

import hu.unideb.inf.carrental.commons.domain.company.Company;
import hu.unideb.inf.carrental.company.resource.model.CompanyResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(CompanyResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CompanyResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CompanyResponse from(Company company) {
        logger.info("Converting Company to CompanyResponse");
        return modelMapper.map(company, CompanyResponse.class);
    }
}
