package hu.unideb.inf.carrental.site.service.converter;

import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.site.resource.model.UpdateSiteRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateSiteRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(UpdateSiteRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public UpdateSiteRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Site from(UpdateSiteRequest updateSiteRequest) {
        logger.info("Converting UpdateSiteRequest to Site");
        return modelMapper.map(updateSiteRequest, Site.class);
    }
}
