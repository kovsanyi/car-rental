package hu.unideb.inf.carrental.commons.config;

import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.reservation.Reservation;
import hu.unideb.inf.carrental.reservation.resource.model.CreateReservationRequest;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Configuration
public class AppConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<CreateCarRequest, Car>() {
            @Override
            protected void configure() {
                map().setId(null);
            }
        });

        Converter<CreateReservationRequest, Reservation> converter = new AbstractConverter<CreateReservationRequest, Reservation>() {
            @Override
            protected Reservation convert(CreateReservationRequest source) {
                Reservation reservation = new Reservation();
                Car car = new Car();
                car.setId(source.getCarId());
                reservation.setCar(car);
                reservation.setReceiveDate(LocalDate.parse(source.getReceiveDate()));
                reservation.setPlannedReturnDate(LocalDate.parse(source.getPlannedReturnDate()));
                return reservation;
            }
        };
        modelMapper.createTypeMap(CreateReservationRequest.class, Reservation.class);
        modelMapper.addConverter(converter);
        return modelMapper;
    }
}
