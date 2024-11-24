package fi.haagahelia.tim_motors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import fi.haagahelia.tim_motors.domain.ResetPassword;

@RepositoryRestResource
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    Optional<ResetPassword> findByToken(String token);

    Optional<ResetPassword> findByEmail(String email);

}
