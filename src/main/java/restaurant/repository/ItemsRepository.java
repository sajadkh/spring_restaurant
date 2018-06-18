package restaurant.repository;

import org.springframework.data.repository.CrudRepository;
import restaurant.classes.MenuItem;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ItemsRepository extends CrudRepository<MenuItem, Long> {
    MenuItem findFirstByName(String name);
}