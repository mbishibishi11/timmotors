package fi.haagahelia.tim_motors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailServiceImpl;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .oauth2Login(oauth2 -> {
                    oauth2.loginPage("/login").permitAll();
                    oauth2.defaultSuccessUrl("/google-user", true);
                })

                .formLogin(httpForm -> {
                    httpForm
                            .loginPage("/login").permitAll();
                    httpForm.defaultSuccessUrl("/cars", true);

                })

                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/create-account", "/static/**", "/google-user", "/add-user",
                            "/resources/**",
                            "uploads/**", "/error", "forgot-password", "reset-password").permitAll();
                    registry.anyRequest().authenticated();
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout")
                            .invalidateHttpSession(true);
                })

                .build();
    }
}
