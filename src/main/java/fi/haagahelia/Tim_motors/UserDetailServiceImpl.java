package fi.haagahelia.tim_motors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.repository.AppUserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public UserDetailServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email) // Find user by email
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

        return new AppUserDetails(appUser); // Return found user
    }
}