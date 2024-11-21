package fi.haagahelia.tim_motors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.repository.AppUserRepository;

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
}
