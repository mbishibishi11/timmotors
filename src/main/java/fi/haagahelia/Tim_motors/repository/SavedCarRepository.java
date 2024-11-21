package fi.haagahelia.tim_motors.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fi.haagahelia.tim_motors.domain.SavedCar;

@RepositoryRestResource
public interface SavedCarRepository extends JpaRepository<SavedCar, Long> {

}
