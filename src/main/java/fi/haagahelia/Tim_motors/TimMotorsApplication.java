package fi.haagahelia.tim_motors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class TimMotorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimMotorsApplication.class, args);
    }
}