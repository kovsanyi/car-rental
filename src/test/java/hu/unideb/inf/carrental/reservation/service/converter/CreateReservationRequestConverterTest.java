package hu.unideb.inf.carrental.reservation.service.converter;

import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.format.DateTimeFormatter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateReservationRequestConverterTest {
    @Autowired
    CreateReservationRequestConverter createReservationRequestConverter;

    @Test
    public void from() throws Exception {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(1L, "2017-11-20", "2017-11-21");
        Reservation reservation = createReservationRequestConverter.from(createReservationRequest);
        assert reservation.getId() == null;
        assert reservation.getCustomer() == null;
        assert reservation.getCar().getId().equals(createReservationRequest.getCarId());
        assert reservation.getReceiveDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(createReservationRequest.getReceiveDate());
        assert reservation.getPlannedReturnDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(createReservationRequest.getPlannedReturnDate());
    }

}
