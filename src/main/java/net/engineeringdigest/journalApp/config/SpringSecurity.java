package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class SpringSecurity {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(request -> request
                        .antMatchers("/public/**").permitAll() // 🟢 No auth needed
                        .antMatchers("/journal/**", "/user/**").authenticated() // 🔐 AUTHENTICATION REQUIRED HERE!
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()) //
                .httpBasic(Customizer.withDefaults()) // 🔑 ENABLES BASIC AUTH (Authorization: Basic header)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    // 🎯 THIS IS WHERE YOU TELL SPRING SECURITY HOW TO AUTHENTICATE USERS
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 🔍 "When someone tries to authenticate, use THIS service to check credentials"
        auth.userDetailsService(userDetailsService) // 👉 Use UserDetailsServiceImpl
            .passwordEncoder(new BCryptPasswordEncoder()); // 🔐 Use BCrypt to compare passwords
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}