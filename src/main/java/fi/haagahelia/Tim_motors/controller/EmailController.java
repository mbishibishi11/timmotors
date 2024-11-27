package fi.haagahelia.tim_motors.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haagahelia.tim_motors.AppUserDetails;
import fi.haagahelia.tim_motors.domain.AppUser;
import fi.haagahelia.tim_motors.domain.Car;
import fi.haagahelia.tim_motors.repository.AppUserRepository;
import fi.haagahelia.tim_motors.repository.CarRepository;
import fi.haagahelia.tim_motors.service.EmailService;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/emailSeller")
    public String emailSeller(
            @RequestParam("inquiryMessage") String inquiryMessage,
            @RequestParam("BuyerEmail") String buyerEmail,
            @RequestParam("sellerId") Long sellerId,
            @RequestParam("carId") Long carId,
            Model model, @AuthenticationPrincipal AppUserDetails userDetails, Authentication authentication) {

        // Find car
        Optional<Car> car = carRepository.findById(carId);
        AppUser appUser = null;

        // Check if the user is authenticated
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            if (userDetails != null) {
                appUser = userDetails.getAppUser();
            }
        }
        // Check if the user is authenticated via OAuth2
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
        Optional<AppUser> appUserOpt = appUserRepository.findById(sellerId);
        if (appUserOpt == null) {
            return "vehicle";
        }

        AppUser seller = appUserOpt.get();

        String sellerEmail = seller.getEmail();

        String subject = "Inquiry about your car listing";
        String body = "Message from buyer (" + buyerEmail + "): \n\n" + inquiryMessage;

        // Send email
        emailService.sendEmail(sellerEmail, subject, body);

        model.addAttribute("appUser", appUser);
        model.addAttribute("car", car.get());
        model.addAttribute("success", "Message sent to seller.");
        return "/vehicle";

    }
}
