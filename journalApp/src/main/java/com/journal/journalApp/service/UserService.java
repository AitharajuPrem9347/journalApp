package com.journal.journalApp.service;

import com.journal.journalApp.Entity.JournalEntry;
import com.journal.journalApp.Entity.User;
import com.journal.journalApp.repository.JournalEntryRepository;
import com.journal.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user){
        userRepository.save(user);
    }

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }

    public User findByUserName(String userName)
    {
        return userRepository.findByuserName(userName);
    }


    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }
}


//controller  calls service , calls repository
//controller --> service --> repository