package hu.unideb.inf.carrental.site.service.converter;

import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.site.resource.model.CreateSiteRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSiteRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(CreateSiteRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CreateSiteRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Site from(CreateSiteRequest createSiteRequest) {
        logger.info("Converting CreateSiteRequest to Site");
        return modelMapper.map(createSiteRequest, Site.class);
    }
}
