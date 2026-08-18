
package com.vidittanwar.journalApp.Repository;

import com.vidittanwar.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);
}