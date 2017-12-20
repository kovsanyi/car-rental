package hu.unideb.inf.carrental.commons.domain.car;

import hu.unideb.inf.carrental.commons.domain.site.Site;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {

    Optional<Car> findById(Long id);

    List<Car> findBySite(Site site);

    List<Car> findBySiteId(Long siteId);

    List<Car> findBySiteCompanyId(long companyId);

    List<Car> findBySiteCompanyName(String companyName);

    // Security issue! Replace them as soon as possible!
    @Query(value = "select * from car where category like ?1 and brand like ?2 and fuel_type like ?3 and seat_number like ?4 and price between ?5 and ?6", nativeQuery = true)
    List<Car> findByParams(String category, String brand, String fuelType, String seatNumber, int minPrice, int maxPrice);

    @Query(value = "select distinct category from car", nativeQuery = true)
    List<Object> getDistinctCategory();

    @Query(value = "select distinct brand from car", nativeQuery = true)
    List<Object> getDistinctBrand();

    @Query(value = "select distinct brand from car where category = ?1", nativeQuery = true)
    List<Object> getDistinctBrandByCategory(String category);

    @Query(value = "select * from car where site_id = ?1 and id not in (select c.id from car c inner join reservation r on  c.id = r.car_id where returned_date is null)", nativeQuery = true)
    List<Car> getAvailableCars(Long siteId);
}
