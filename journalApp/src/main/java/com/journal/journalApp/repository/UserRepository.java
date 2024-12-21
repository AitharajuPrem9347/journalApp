package com.journal.journalApp.repository;

import com.journal.journalApp.Entity.JournalEntry;
import com.journal.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByuserName(String userName);

}
