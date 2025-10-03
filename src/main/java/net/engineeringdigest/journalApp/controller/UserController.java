package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController//special type of component that handles http request
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName(); // Gets "alice" (the current authenticated user)

        User userInDb = userService.findByUserName(userName); // Finds alice's complete record

        if(userInDb != null){

            userInDb.setUserName(user.getUserName()); // Changes alice → alice2
            userInDb.setPassword(user.getPassword()); // Changes password to "newsecret"

            userService.saveNewUser(userInDb); // Saves alice2 with encrypted "newsecret"
        }

        // 📤 Return success response (no content body, just status code)
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content = Success
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName()); // Deletes alice

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
