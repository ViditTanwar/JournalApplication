package com.vidittanwar.journalApp.Controller;

import com.vidittanwar.journalApp.Entity.User;
import com.vidittanwar.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.saveEntry(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @RequestBody User user,
            @PathVariable String username) {

        User userInDb = userService.findByUsername(username);

        if (userInDb != null) {

            userInDb.setUsername(user.getUsername());

            userInDb.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );

            userService.saveEntry(userInDb);

            return new ResponseEntity<>(userInDb, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}