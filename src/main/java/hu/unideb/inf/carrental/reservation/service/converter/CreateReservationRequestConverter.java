package hu.unideb.inf.carrental.reservation.service.converter;

import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateReservationRequestConverter {
    private static final Logger logger = LoggerFactory.getLogger(CreateReservationRequestConverter.class);

    private final ModelMapper modelMapper;

    @Autowired
    public CreateReservationRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Reservation from(CreateReservationRequest createReservationRequest) {
        logger.info("Converting CreateReservationRequest to Reservation");
        return modelMapper.map(createReservationRequest, Reservation.class);
    }
}
