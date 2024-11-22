package fi.haagahelia.tim_motors.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.repository.AppUserRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping({ "/", "/login" })
    public String login() {
        return "login";
    }

    @GetMapping("/create-account")
    public String createAccountForm(Model model) {
        AppUser appUser = new AppUser();
        appUser.setRole("standard");
        model.addAttribute("appUser", appUser);
        return "create-account";
    }

    @GetMapping("/google-user")
    public String saveGoogleUser(OAuth2AuthenticationToken token, Model model) {
        String userEmail = token.getPrincipal().getAttribute("email");

        // Check if the user already exists in the database
        AppUser appUser = appUserRepository.findByEmail(userEmail).orElseGet(() -> {
            // Create a new user if not found
            AppUser newUser = new AppUser();
            newUser.setEmail(userEmail);
            newUser.setRole("standard"); // Set default role
            newUser.setPassword("defaultPassword"); // Set default password for Google login
            newUser.setFirstName(token.getPrincipal().getAttribute("given_name"));
            newUser.setlastName(token.getPrincipal().getAttribute("family_name"));
            return appUserRepository.save(newUser); // Save and return the new user
        });
        // Add the user to the model
        model.addAttribute("appUser", appUser);

        // Return the cars view
        return "redirect:/cars";

    }

    @PostMapping("/add-user")
    public String saveUser(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult result, Model model) {
        // If email already exists
        if (appUserRepository.existsByEmail(appUser.getEmail())) {
            // Error message
            result.rejectValue("email", "email.exists", "This email is already in use, try sign in.");
            return "/create-account"; // Return to the form
        }

        if (result.hasErrors()) {
            // Return to the form if there are validation errors
            return "/create-account";
        }

        // Encrypt password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordHash = passwordEncoder.encode(appUser.getPassword());

        // Set password
        appUser.setPassword(passwordHash);

        // Set default role
        appUser.setRole("standard");

        // Save user
        appUserRepository.save(appUser);
        return "redirect:/login";
    }

    @GetMapping("/reset-password")
    public String passwordResetForm(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {

        // Check if email exists
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Email not found");
            return "forgot-password"; // Return to the forgot page
        }

        AppUser appUser = userOptional.get();
        String verificationToken = UUID.randomUUID().toString(); // Generate
        return "/";
    }

    @GetMapping("/verification")
    public String verification(Model model) {
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
        return "forgot-password";
    }

}
