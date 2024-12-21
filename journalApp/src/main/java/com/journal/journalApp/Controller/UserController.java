package com.journal.journalApp.Controller;

import com.journal.journalApp.Entity.JournalEntry;
import com.journal.journalApp.Entity.User;
import com.journal.journalApp.service.JournalEntryService;
import com.journal.journalApp.service.UserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController
{

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers()
    {
       return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user)
    {
        userService.saveEntry(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable String userName)
    {
       User userInDb = userService.findByUserName(userName);
       if(userInDb != null)
       {
           userInDb.setUserName(user.getUserName());
           userInDb.setPassword(user.getPassword());
           userService.saveEntry(userInDb);
           return new ResponseEntity<>(HttpStatus.OK);
       }

       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}