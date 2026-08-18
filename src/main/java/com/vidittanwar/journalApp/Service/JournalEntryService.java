
package com.vidittanwar.journalApp.Service;

import com.vidittanwar.journalApp.Entity.JournalEntry;
import com.vidittanwar.journalApp.Entity.User;
import com.vidittanwar.journalApp.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public boolean isJournalOwner(ObjectId journalId, String username) {

        User user = userService.findByUsername(username);

        if (user == null || user.getJournalEntries() == null) {
            return false;
        }

        return user.getJournalEntries()
                .stream()
                .anyMatch(entry -> entry.getId().equals(journalId));
    }

    public void saveEntry(JournalEntry journalEntry, String username){
        try{
            User user =userService.findByUsername(username);

            JournalEntry saved =journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);}catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while saving the entry",e);        }
    }
    public void saveEntry(JournalEntry journalEntry){

        journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id, String username) {

        User user = userService.findByUsername(username);

        if (user == null || user.getJournalEntries() == null) {
            return false;
        }

        boolean exists = user.getJournalEntries()
                .stream()
                .anyMatch(entry ->
                        entry.getId() != null &&
                                entry.getId().equals(id.toHexString())
                );

        if (!exists) {
            return false;
        }

        user.getJournalEntries()
                .removeIf(entry ->
                        entry.getId() != null &&
                                entry.getId().equals(id.toHexString())
                );

        userService.saveEntry(user);

        journalEntryRepository.deleteById(id);

        return true;
    }

}