package hu.unideb.inf.carrental.car.service.converter;

import hu.unideb.inf.carrental.car.resource.model.CreateCarRequest;
import hu.unideb.inf.carrental.commons.domain.car.Car;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.CarCategory;
import hu.unideb.inf.carrental.commons.domain.car.enumeration.FuelType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateCarRequestConverterTest {
    @Autowired
    CreateCarRequestConverter createCarRequestConverter;

    @Test
    public void from() throws Exception {
        CreateCarRequest createCarRequest = new CreateCarRequest(1L, CarCategory.Crossover, "Brand", "Model", 1, 1, FuelType.Benzin, 1.0F, 1, 1, 1);
        Car car = createCarRequestConverter.from(createCarRequest);
        assert car.getId() == null;
        assert car.getSite().getId().equals(createCarRequest.getSiteId());
        assert car.getCategory().equals(createCarRequest.getCategory());
        assert car.getBrand().equals(createCarRequest.getBrand());
        assert car.getModel().equals(createCarRequest.getModel());
        assert car.getTrunkCapacity().equals(createCarRequest.getTrunkCapacity());
        assert car.getYear().equals(createCarRequest.getYear());
        assert car.getFuelType().equals(createCarRequest.getFuelType());
        assert car.getFuelConsumption().equals(createCarRequest.getFuelConsumption());
        assert car.getTankCapacity().equals(createCarRequest.getTankCapacity());
        assert car.getSeatNumber().equals(createCarRequest.getSeatNumber());
        assert car.getPrice().equals(createCarRequest.getPrice());
    }
}
