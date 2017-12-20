package hu.unideb.inf.carrental.reservation.service.converter;

import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.reservation.resource.model.ReservationResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationResponseConverter {
    private static final Logger logger = LoggerFactory.getLogger(ReservationResponseConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public ReservationResponseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservationResponse from(Reservation reservation) {
        logger.info("Converting Reservation to ReservationResponse");
        return modelMapper.map(reservation, ReservationResponse.class);
    }
}
