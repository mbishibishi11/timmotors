package fi.haagahelia.tim_motors.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import jakarta.validation.Valid;

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
import fi.haagahelia.tim_motors.domain.ResetPassword;
import fi.haagahelia.tim_motors.repository.AppUserRepository;
import fi.haagahelia.tim_motors.repository.ResetPasswordRepository;
import fi.haagahelia.tim_motors.service.EmailService;
import fi.haagahelia.tim_motors.service.PasswordResetService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private PasswordResetService passwordResetService;

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

    @GetMapping("/forgot-password")
    public String passwordResetForm(Model model) {
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
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

        String verificationToken = UUID.randomUUID().toString(); // Generate a verification token

        ResetPassword resetPassword = new ResetPassword(
                email,
                verificationToken,
                LocalDateTime.now().plusMinutes(30) // Token validity
        );

        resetPasswordRepository.save(resetPassword); // Save email and token
        emailService.sendResetPasswordEmail(email, verificationToken); // Email user

        model.addAttribute("message", "Link to reset password sent to " + email);
        return "/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Validate the token and check if it's valid
        boolean isValid = passwordResetService.validateToken(token);

        if (isValid) {
            model.addAttribute("token", token);
            model.addAttribute("appUser", new AppUser());
            return "reset-password";
        } else {
            model.addAttribute("errorMessage", "Invalid link request new link.");
            return "/forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String ResetPassword(@RequestParam("token") String token, @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        // Validate email for password reset
        boolean isValidEmail = passwordResetService.resetPassword(token, password, email);

        if (isValidEmail) {

            model.addAttribute("message", "Password reset successful. Please log in with your new password.");
            return "redirect:/login";
        }

        model.addAttribute("errorMessage", "Invalid email address or password reset failed.");
        return "reset-password";
    }

}
