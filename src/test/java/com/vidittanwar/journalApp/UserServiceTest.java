package com.vidittanwar.journalApp;

import com.vidittanwar.journalApp.Entity.User;
import com.vidittanwar.journalApp.Repository.UserRepository;
import com.vidittanwar.journalApp.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByUsernameTest() {

        User user = new User();
        user.setUsername("vidit");
        user.setPassword("12345");

        when(userRepository.findByUsername("vidit"))
                .thenReturn(user);

        User result = userService.findByUsername("vidit");

        assertEquals("vidit", result.getUsername());
    }
}