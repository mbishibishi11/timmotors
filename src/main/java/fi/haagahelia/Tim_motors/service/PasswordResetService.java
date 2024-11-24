package fi.haagahelia.tim_motors.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.domain.ResetPassword;
import fi.haagahelia.tim_motors.repository.AppUserRepository;
import fi.haagahelia.tim_motors.repository.ResetPasswordRepository;

@Service
public class PasswordResetService {

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public boolean validateToken(String token) {

        Optional<ResetPassword> resetPasswordToken = resetPasswordRepository.findByToken(token);

        if (resetPasswordToken.isPresent()) {
            ResetPassword tokenEntity = resetPasswordToken.get();

            LocalDateTime expirationTime = tokenEntity.getExpirationTime();

            // Get current time
            LocalDateTime currentTime = LocalDateTime.now();

            // Compare expiration time with current time
            if (currentTime.isBefore(expirationTime)) {
                return true;
            }

        }
        return false;
    }

    public boolean resetPassword(String token, String newPassword, String email) {

        // Check if the token is valid and fetch the associated ResetPassword record
        Optional<ResetPassword> resetPasswordOptional = resetPasswordRepository.findByToken(token);

        if (resetPasswordOptional.isEmpty()) {
            return false; // Token not found
        }

        ResetPassword resetPassword = resetPasswordOptional.get();

        // Ensure that the email in the token matches the one provided by the user
        if (!resetPassword.getEmail().equals(email)) {
            return false; // Email does not match
        }

        // Find the user by email
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

        if (appUserOptional.isEmpty()) {
            return false; // User not found
        }

        // Get the AppUser object
        AppUser appUser = appUserOptional.get();

        // Encode the new password using BCrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordHash = passwordEncoder.encode(newPassword);

        appUser.setPassword(passwordHash);

        appUserRepository.save(appUser);

        resetPasswordRepository.delete(resetPassword); // Delete token after successful reset

        System.out.println("Password reset successful for user: " + email);

        return true; // Password reset successfully
    }
}
