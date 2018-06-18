package restaurant.repository;

import org.springframework.data.repository.CrudRepository;
import restaurant.classes.Order;
import restaurant.classes.OrderState;
import restaurant.classes.Table;


import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findById(int id);
    Order findFirstByTable(Table table);
    List<Order> findAllByOrderState(OrderState orderState);
    List<Order> findAllByOrderStateAndDelivered(OrderState orderState,boolean delivered);
}