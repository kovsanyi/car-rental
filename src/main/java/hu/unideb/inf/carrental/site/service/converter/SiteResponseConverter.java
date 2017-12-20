package hu.unideb.inf.carrental.site.service.converter;

import hu.unideb.inf.carrental.commons.domain.site.Site;
import hu.unideb.inf.carrental.site.resource.model.SiteResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(SiteResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public SiteResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SiteResponse from(Site site) {
        logger.info("Converting Site to SiteResponse");
        return modelMapper.map(site, SiteResponse.class);
    }
}
