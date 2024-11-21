package fi.haagahelia.tim_motors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import fi.haagahelia.tim_motors.domain.AppUser;

@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findById(Long id);

    Boolean existsByEmail(String email);
}
