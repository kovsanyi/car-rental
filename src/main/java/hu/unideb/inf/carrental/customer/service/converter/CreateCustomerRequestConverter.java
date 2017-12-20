package hu.unideb.inf.carrental.customer.service.converter;

import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.customer.resource.model.CreateCustomerRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(CreateCustomerRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CreateCustomerRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Customer from(CreateCustomerRequest createCustomerRequest) {
        logger.info("Converting CreateCustomerRequest to Customer");
        return modelMapper.map(createCustomerRequest, Customer.class);
    }
}
