package restaurant.repository;

import org.springframework.data.repository.CrudRepository;
import restaurant.classes.Table;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TableRepository extends CrudRepository<Table, Long> {
    List<Table> findById(int id);
    List<Table> findByCapacity(int capacity);
    List<Table> findByReserved(boolean reserved);
}