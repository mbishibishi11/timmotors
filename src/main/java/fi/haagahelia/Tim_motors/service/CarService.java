package fi.haagahelia.tim_motors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fi.haagahelia.tim_motors.domain.Car;
import fi.haagahelia.tim_motors.repository.CarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    private String uploadDirectory = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);

        // Check if directory exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Assign a unique name
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());

        return uploadPath + fileName;
    }

    /**
     * public Car saveCar(Car car, MultipartFile imageFile) throws IOException {
     * if (imageFile != null && !imageFile.isEmpty()) {
     * String imageUrl = handleImageUpload(imageFile);
     * car.setUrl(imageUrl);
     * }
     * 
     * cleanCarData(car);
     * car.setAppUser(null);
     * return carRepository.save(car);
     * }
     * 
     * private String handleImageUpload(MultipartFile imageFile) throws IOException
     * {
     * // Create uploads directory
     * Path uploads = Paths.get(uploadPath);
     * if (!Files.exists(uploads)) {
     * Files.createDirectories(uploads);
     * }
     * 
     * // Generate unique name for the upload
     * String originalFilename = imageFile.getOriginalFilename();
     * String fileExtension =
     * originalFilename.substring(originalFilename.lastIndexOf("."));
     * String newFilename = UUID.randomUUID().toString() + fileExtension;
     * 
     * // Save file
     * Path filePath = uploads.resolve(newFilename);
     * Files.copy(imageFile.getInputStream(), filePath);
     * 
     * return "/uploads/" + newFilename;
     * }
     * private void cleanCarData(Car car) {
     * // Remove any symbol or comma from price
     * if (car.getPrice() != null) {
     * String priceStr = car.getPrice().toString()
     * .replaceAll("[^0-9.]", "");
     * car.setPrice(Double.parseDouble(priceStr));
     * }
     * 
     * // Clean mileage
     * if (car.getMileage() != null) {
     * car.setMileage(car.getMileage().replaceAll("[^0-9]", ""));
     * }
     * 
     * // Validate manufacture year
     * if (car.getManufactureYear() != null) {
     * car.setManufactureYear(car.getManufactureYear().trim());
     * }
     * }
     * // Get car by Id
     * public Car getCarById(Long id) {
     * return carRepository.findById(id)
     * .orElseThrow(() -> new RuntimeException("Car not found"));
     * }
     * // Delete car by id
     * public void deleteCar(Long id) {
     * carRepository.deleteById(id);
     * }
     * // Get all cards
     * public List<Car> getAllCars() {
     * return carRepository.findAll();
     * }
     */

}
