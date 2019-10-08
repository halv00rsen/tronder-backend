package org.tronder.words.repository;

import org.springframework.data.repository.CrudRepository;
import org.tronder.words.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
