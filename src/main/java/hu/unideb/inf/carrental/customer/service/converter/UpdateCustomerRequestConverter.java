package hu.unideb.inf.carrental.customer.service.converter;

import hu.unideb.inf.carrental.commons.domain.customer.Customer;
import hu.unideb.inf.carrental.customer.resource.model.UpdateCustomerRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCustomerRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(UpdateCustomerRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public UpdateCustomerRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Customer from(UpdateCustomerRequest updateCustomerRequest) {
        logger.info("Converting UpdateCustomerRequest to Customer");
        return modelMapper.map(updateCustomerRequest, Customer.class);
    }
}
