package fi.haagahelia.Tim_motors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fi.haagahelia.Tim_motors.domain.Car;

@RepositoryRestResource
public interface CarRepository extends JpaRepository<Car, Long>{

}
