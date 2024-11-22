package fi.haagahelia.Tim_motors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fi.haagahelia.tim_motors.UserDetailServiceImpl;

@SpringBootTest
public class UserDetailServiceTest {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Test
    public void testLoadUserByUsername() {

        String email = "kiranlogan11";

        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(email);

            // Print the result
            System.out.println("User found: " + userDetails.getUsername());
            System.out.println("Roles: " + userDetails.getAuthorities());
        } catch (UsernameNotFoundException e) {
            // If the user is not found, print an error
            System.err.println("Error: " + e.getMessage());
        }
    }
}
