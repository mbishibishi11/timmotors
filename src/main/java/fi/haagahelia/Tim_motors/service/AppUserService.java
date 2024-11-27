package fi.haagahelia.tim_motors.service;

import org.springframework.beans.factory.annotation.Autowired;

import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.repository.AppUserRepository;

public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
