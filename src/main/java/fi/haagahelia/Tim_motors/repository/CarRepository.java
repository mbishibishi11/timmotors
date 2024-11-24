package fi.haagahelia.tim_motors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fi.haagahelia.tim_motors.domain.Car;

@RepositoryRestResource
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.appUser.id = :id")
    List<Car> findCarByUserId(@Param("id") Long id);

}
