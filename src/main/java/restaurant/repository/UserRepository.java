package restaurant.repository;

import org.springframework.data.repository.CrudRepository;
import restaurant.classes.User;


import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
    User findFirstByToken(String token);
}