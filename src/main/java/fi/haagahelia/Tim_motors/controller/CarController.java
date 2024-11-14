package fi.haagahelia.tim_motors.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fi.haagahelia.tim_motors.domain.Car;
import fi.haagahelia.tim_motors.repository.CarRepository;
import fi.haagahelia.tim_motors.service.CarService;

@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Show the new vehicle form
    @GetMapping("/add-vehicle")
    public String showAddVehicleForm(Model model) {
        model.addAttribute("car", new Car());
        return "add-vehicle";
    }

    // handle the new vehicle form
    @PostMapping("/add-vehicle")
    public String saveVehicleInfo(Car car, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        try {
            // Save image file
            String imageUrl = carService.saveFile(imageFile);
            car.setUrl(imageUrl);

            carRepository.save(car);

            model.addAttribute("message", "Vehicle saved successfully.");
            return "redirect:/";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload image.");
            return "error" + e.getMessage();
        }
    }

}
