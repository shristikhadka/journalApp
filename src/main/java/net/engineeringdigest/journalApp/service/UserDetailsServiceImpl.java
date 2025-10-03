package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 🔍 THIS METHOD IS CALLED BY SPRING SECURITY DURING AUTHENTICATION!
        // When client sends Authorization: Basic "alice:plaintext123"
        // Spring Security extracts "alice" and calls THIS method with username="alice"

        // 🔍 Step 1: Look up user in database by username
        User user = userRepository.findByUserName(username); // Find "alice" in database

        if(user != null){
            // 🔍 Step 2: Return user details to Spring Security for password comparison
            // The encrypted password from database will be compared with the plain password from client
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName()) // "alice"
                    .password(user.getPassword()) // "$2a$10$xyz..." (encrypted password from database)
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();

            // 🔍 Step 3: Spring Security will now compare:
            // - Client sent: "plaintext123"
            // - Database has: "$2a$10$xyz..."
            // - BCryptPasswordEncoder.matches("plaintext123", "$2a$10$xyz...") → true/false

            return userDetails; // Return to Spring Security for password verification
        }
        throw new UsernameNotFoundException("User not found with username:" + username);
    }
}
