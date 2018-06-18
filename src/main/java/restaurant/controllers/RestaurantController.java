package restaurant.controllers;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.*;
import restaurant.classes.MenuItem;
import restaurant.classes.Order;
import restaurant.classes.Restaurant;
import restaurant.repository.ItemsRepository;
import restaurant.repository.OrderRepository;
import restaurant.repository.TableRepository;
import restaurant.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class RestaurantController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    private Restaurant restaurant;

    public RestaurantController(){
        this.restaurant=new Restaurant();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/save") // Map ONLY GET Requests
    public @ResponseBody String init (@RequestParam int table2, @RequestParam int table3 , @RequestParam int table4 ,
                                      @RequestParam int table6, @RequestParam String menuItems, @RequestParam String menuItemPrice) {
        boolean result = this.restaurant.init(table2,table3,table4,table6,menuItems,menuItemPrice,itemsRepository,tableRepository);

        return "{\"result\":\""+result+"\"}";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/signup") // Map ONLY GET Requests
    public @ResponseBody String signUp (@RequestParam String name
            , @RequestParam String phoneNumber , @RequestParam String password) {
        boolean result = restaurant.signUp(name,phoneNumber,password,userRepository);

        return "{\"result\":\""+result+"\"}";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/signin")
    public @ResponseBody String signIn(@RequestParam String name , @RequestParam String password) {
        String token = restaurant.signIn(name,password,userRepository);
        String r;
        if(token.length()!= 100){
            return  "{\"result\":no,\"token\":\""+ token +"\"}";
        }
        return "{\"result\":ok,\"token\":\""+ token +"\"}";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/reserve")
    public @ResponseBody String reserve(@RequestParam int tableNumber,@RequestParam String token){
        boolean result = restaurant.reserve(tableNumber,token,userRepository,tableRepository);
        return "{\"result\":\""+result+"\"}";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/deReserve")
    public @ResponseBody String deReserve(@RequestParam int tableNumber,@RequestParam String token){
        boolean result = restaurant.deReserve(tableNumber,token,userRepository,tableRepository);
        return "{\"result\":\""+result+"\"}";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path="/emptyTable")
    public @ResponseBody String showEmptyTable(@RequestParam int guestNumber){
        Gson gson = new Gson();
        return gson.toJson(restaurant.showEmptyTable(guestNumber,tableRepository));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/order")
    public @ResponseBody String order(@RequestParam String items, @RequestParam String token){
        String[] menuItemNames = items.split(",");
        List<MenuItem> menuItems=new ArrayList<>();
        for (String s:menuItemNames) {
            MenuItem mi = itemsRepository.findFirstByName(s);
            menuItems.add(mi);
        }
        Order o = restaurant.order(menuItems,token,userRepository,orderRepository);
        if(o!=null)
            return "{\"result\":\"ok\",\"orderId\":\""+o.getId()+"\",\"turn\":\""+o.getTurn()+"\"}";
        else
            return "{\"result\":\"false\"}";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/allOrders")
    public @ResponseBody List<Order> showAllOrders(){
        return restaurant.showAllOrder(orderRepository);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/changeOrderStateToPending")
    public @ResponseBody String changeOrderStateToPending(@RequestParam int orderId){
        boolean result = restaurant.changeOrderStateToPending(orderId,orderRepository);
        return "{\"result\":\""+result+"\"}";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/changeOrderStateToChecked")
    public @ResponseBody String changeOrderStateToChecked(@RequestParam int orderId){
        boolean result = restaurant.changeOrderStateToChecked(orderId,orderRepository);
        return "{\"result\":\""+result+"\"}";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/allOrdersCheckedAndNotDelivered")
    public @ResponseBody List<Order> showAllOrdersCheckedAndNotDelivered(){
        return restaurant.showAllOrderCheckedAndNotDelivered(orderRepository);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/deliverOrder")
    public @ResponseBody String deliverOrder(@RequestParam int orderId){
        boolean result = restaurant.deliverOrder(orderId,orderRepository);
        return "{\"result\":\""+result+"\"}";
    }
}