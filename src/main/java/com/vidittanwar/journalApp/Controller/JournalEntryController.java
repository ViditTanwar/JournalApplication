package com.vidittanwar.journalApp.Controller;


import com.vidittanwar.journalApp.Entity.JournalEntry;
import com.vidittanwar.journalApp.Entity.User;
import com.vidittanwar.journalApp.Service.JournalEntryService;
import com.vidittanwar.journalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @PostMapping("/journalpost/{username}")
    public ResponseEntity<?> createEntry(
            @RequestBody JournalEntry myEntry,
            @PathVariable String username,
            org.springframework.security.core.Authentication authentication) {

        String loggedInUsername = authentication.getName();

        if (!loggedInUsername.equals(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/journalget/{username}")
    public ResponseEntity<?> getAllJournalEntriesUser(
            @PathVariable String username,
            Authentication authentication) {

        String loggedInUsername = authentication.getName();

        if (!loggedInUsername.equals(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User user = userService.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> all = user.getJournalEntries();

        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId id){

        Optional<JournalEntry> journalEntry=journalEntryService.findById(id);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

    }



    @DeleteMapping("/id/{username}/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable ObjectId id,
            @PathVariable String username,
            org.springframework.security.core.Authentication authentication) {

        String loggedInUsername = authentication.getName();

        if (!loggedInUsername.equals(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        boolean deleted = journalEntryService.deleteById(id, username);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{username}/{id}")
    public ResponseEntity<?> updateEntry(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry journalEntry,
            @PathVariable String username,
            org.springframework.security.core.Authentication authentication) {

        String loggedInUsername = authentication.getName();

        if (!loggedInUsername.equals(username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        JournalEntry old = journalEntryService.findById(id).orElse(null);

        if (old != null) {

            old.setTitle(
                    journalEntry.getTitle() != null && !journalEntry.getTitle().equals("")
                            ? journalEntry.getTitle()
                            : old.getTitle()
            );

            old.setContent(
                    journalEntry.getContent() != null && !journalEntry.getContent().equals("")
                            ? journalEntry.getContent()
                            : old.getContent()
            );

            journalEntryService.saveEntry(old);

            return new ResponseEntity<>(old, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}