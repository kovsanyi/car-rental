package hu.unideb.inf.carrental.customer.service.converter;

import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.customer.resource.model.CustomerResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(CustomerResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CustomerResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerResponse from(Customer customer) {
        logger.info("Converting Customer to CustomerResponse");
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
