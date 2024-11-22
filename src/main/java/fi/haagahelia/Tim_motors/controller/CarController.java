package fi.haagahelia.tim_motors.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import fi.haagahelia.tim_motors.repository.CarRepository;
import fi.haagahelia.tim_motors.service.CarService;

@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

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
            @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            if (imageFile.isEmpty()) {
                model.addAttribute("error", "Please select an image file");
                return "redirect:/add-vehicle";
            }

            // Save image file
            String imageUrl = carService.saveFile(imageFile);
            car.setUrl(imageUrl);

            // Get user id to be assossiated with post
            AppUser appUser = userDetails.getAppUser();
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
    public String listVehicles(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalStateException(""); ////////////////// Add error page here
        }

        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        model.addAttribute("appUser", userDetails.getAppUser());
        return "cars";
    }

    @GetMapping("/my-listing/{id}")
    public String viewListing(@PathVariable Long id, Model model) {
        List<Car> cars = carRepository.findCarByUserId(id);
        model.addAttribute("cars", cars);
        return "my-listing";
    }

    @GetMapping("/cars/vehicle/{id}")
    public String vehicleDetails(@PathVariable Long id, Model model,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with ID: " + id));
        model.addAttribute("car", car);
        model.addAttribute("appUser", userDetails.getAppUser());
        return "vehicle";
    }

    @GetMapping("/saved")
    public String viewSaved(Model model) {
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
        return "saved";
    }

}
