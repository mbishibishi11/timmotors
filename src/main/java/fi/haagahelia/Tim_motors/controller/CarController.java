package fi.haagahelia.tim_motors.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fi.haagahelia.tim_motors.AppUserDetails;
import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.domain.Car;
import fi.haagahelia.tim_motors.repository.AppUserRepository;
import fi.haagahelia.tim_motors.repository.CarRepository;
import fi.haagahelia.tim_motors.service.CarService;

@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    // Show the new vehicle form
    @GetMapping("/add-vehicle")
    public String showAddVehicleForm(Model model) {
        model.addAttribute("car", new Car());
        return "add-vehicle";
    }

    // Handle the new vehicle form
    @PostMapping("/save-vehicle")
    public String saveVehicleInfo(Car car,
            @RequestParam("imageFile") MultipartFile imageFile, Model model,
            @AuthenticationPrincipal AppUserDetails userDetails,
            Authentication authentication) {
        try {
            if (imageFile.isEmpty()) {
                model.addAttribute("error", "Please select an image file");
                return "redirect:/add-vehicle";
            }

            // Save image file
            String imageUrl = carService.saveFile(imageFile);
            car.setUrl(imageUrl);

            // Get user id to be assossiated with post
            AppUser appUser = null;

            // Check if the user is authenticated via standard login
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                if (userDetails != null) {
                    appUser = userDetails.getAppUser();
                }
            }
            // Check if the user is authenticated via OAuth2 (Google login)
            else if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
                String email = oAuth2Token.getPrincipal().getAttribute("email");
                appUser = appUserRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalStateException("User not found: " + email));
            }
            // Else, redirect to login page if no valid authentication is found
            else {
                return "redirect:/login";
            }
            car.setAppUser(appUser);
            carRepository.save(car);

            model.addAttribute("message", "Vehicle saved successfully.");
            return "redirect:/cars";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to save post." + e.getMessage());
            return "redirect:/add-vehicle";
        }
    }

    @GetMapping("/cars")
    public String listVehicles(Model model, @AuthenticationPrincipal AppUserDetails userDetails,
            Authentication authentication) {

        AppUser appUser = null;

        // Check if the user is authenticated via standard login
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (userDetails != null) {
                appUser = userDetails.getAppUser();
            }
        }
        // Check if the user is authenticated via OAuth2 (Google login)
        else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            String email = oAuth2Token.getPrincipal().getAttribute("email");
            appUser = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + email));
        }
        // Else, redirect to login page if no valid authentication is found
        else {
            return "redirect:/login";
        }

        // Add user and cars to the model
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        model.addAttribute("appUser", appUser);

        return "cars";
    }

    @GetMapping("/my-listing/{id}")
    public String viewListing(@PathVariable Long id, Model model, @AuthenticationPrincipal AppUserDetails userDetails,
            Authentication authentication) {
        List<Car> cars = carRepository.findCarByUserId(id);

        AppUser appUser = null;

        // Check if the user is authenticated via standard login
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (userDetails != null) {
                appUser = userDetails.getAppUser();
            }
        }
        // Check if the user is authenticated via OAuth2 (Google login)
        else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            String email = oAuth2Token.getPrincipal().getAttribute("email");
            appUser = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + email));
        }
        // Else, redirect to login page if no valid authentication is found
        else {
            return "redirect:/login";
        }

        model.addAttribute("appUser", appUser);
        model.addAttribute("cars", cars);
        return "my-listing";
    }

    @GetMapping("/cars/vehicle/{id}")
    public String vehicleDetails(@PathVariable Long id, Model model,
            @AuthenticationPrincipal AppUserDetails userDetails, Authentication authentication) {

        AppUser appUser = null;

        // Check if the user is authenticated via standard login
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (userDetails != null) {
                appUser = userDetails.getAppUser();
            }
        }
        // Check if the user is authenticated via OAuth2 (Google login)
        else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            String email = oAuth2Token.getPrincipal().getAttribute("email");
            appUser = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + email));
        }
        // Else, redirect to login page if no valid authentication is found
        else {
            return "redirect:/login";
        }
        // Retrieve car info
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + id));
        model.addAttribute("car", car);
        model.addAttribute("appUser", appUser);
        return "vehicle";
    }

    @GetMapping("/saved")
    public String viewSaved(Model model) {
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
        return "saved";
    }

}
