package com.journal.journalApp.Controller;

import com.journal.journalApp.Entity.JournalEntry;
import com.journal.journalApp.Entity.User;
import com.journal.journalApp.service.JournalEntryService;
import com.journal.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> entries = user.getJournalEntries();
        if (entries != null && !entries.isEmpty()) {
            return new ResponseEntity<>(entries, HttpStatus.OK);
        }

        return new ResponseEntity<>("No entries found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        try {
            User user = userService.findByUserName(userName);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating journal entry", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>( HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String userName) {
        Optional<JournalEntry> entry = journalEntryService.findById(myId);
        if (entry.isEmpty()) {
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }

        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>("Journal entry deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable String userName,
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry) {
        Optional<JournalEntry> optionalOldEntry = journalEntryService.findById(myId);
        if (optionalOldEntry.isEmpty()) {
            return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
        }

        JournalEntry oldEntry = optionalOldEntry.get();
        oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
        oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());

        journalEntryService.saveEntry(oldEntry);
        return new ResponseEntity<>(oldEntry, HttpStatus.OK);
    }
}
