package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.mockito.Mockito.*;
import java.util.Arrays;


public class UserDetailsServiceImplTests {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.jupiter.api.Disabled
    @Test
    void loadUserByUsernameTest(){
        User mockUser = new User("ram", "inigin");
        mockUser.setRoles(Arrays.asList("USER"));
        when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(mockUser);
        UserDetails user=userDetailsService.loadUserByUsername("ram");
        Assertions.assertNotNull(user);
    }
}

