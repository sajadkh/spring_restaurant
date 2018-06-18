package restaurant.classes;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import restaurant.repository.ItemsRepository;
import restaurant.repository.OrderRepository;
import restaurant.repository.TableRepository;
import restaurant.repository.UserRepository;


import java.util.Random;

public class Restaurant {
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";


    public boolean init(int table2, int table3, int table4, int table6, String menuItems, String menuItemsPrice, ItemsRepository itemsRepository, TableRepository tableRepository){
        int i;
        String[] menuItemNames = menuItems.split(",");
        String[] menuItemPr = menuItemsPrice.split(",");
        for (int j=0;j<menuItemNames.length;j++) {
            MenuItem mi = new MenuItem(menuItemNames[j],Integer.parseInt(menuItemPr[j]));
            itemsRepository.save(mi);
        }
        for (i = 1; i <= table2; i++) {
            Table t = new Table(2);
            tableRepository.save(t);
        }
        for (i = 1; i <= table3; i++) {
            Table t = new Table(3);
            tableRepository.save(t);
        }
        for (i = 1; i <= table4; i++) {
            Table t = new Table(4);
            tableRepository.save(t);
        }
        for (i = 1; i <= table6; i++) {
            Table t = new Table(6);
            tableRepository.save(t);
        }
        return true;
    }

    //add user to db
    public boolean signUp(String name, String phoneNumber, String password , UserRepository userRepository) {
        User user = new User();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        userRepository.save(user);
        return true;
    }

    //sign in
    public String signIn(String name, String password , UserRepository userRepository) {
        User user = userRepository.findByName(name).get(0);
        String md5Hex = DigestUtils.md5Hex(password).toUpperCase();
        if (user.getPassword().equals(md5Hex)) {
            StringBuilder token = new StringBuilder(100);
            for (int i = 0; i < 100; i++) {
                token.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            user.setToken(token.toString());
            userRepository.save(user);
            return token.toString();
        } else {
            return "Wrong Password!";
        }
    }

    public boolean reserve(int number, String token,UserRepository userRepository,TableRepository tableRepository) {
        User user = userRepository.findFirstByToken(token);
        Table table = tableRepository.findById(number).get(0);
        if (user == null)
            return false;
        if(table.reserveTable(user)){
            user.setTable(table);
            tableRepository.save(table);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deReserve(int number, String token, UserRepository userRepository, TableRepository tableRepository) {
        try {
            Table table = tableRepository.findById(number).get(0);
            User user = userRepository.findFirstByToken(token);
            if (user != null && user.equals(table.getUser())) {
                if(table.deReserve()) {
                    tableRepository.save(table);
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public List<Table> showEmptyTable(int guestNumber ,TableRepository tableRepository){
        List<Table> tables = tableRepository.findByReserved(false);
        ArrayList<Table> pTable = new ArrayList<>();
        if(guestNumber==0)
            return pTable;
        boolean[] sw={false,false,false,false};
        for(int i=0;i<tables.size();i++){
            Table t=tables.get(i);
            if(guestNumber<=2){
                if (t.getCapacity()==2) {
                    pTable.add(t);
                    sw[0] = true;
                }
                if(!sw[0] && t.getCapacity()==3){
                    pTable.add(t);
                    sw[1]=true;
                }
                if(!sw[0] && !sw[1] && t.getCapacity()==4){
                    pTable.add(t);
                    sw[2]=true;
                }
                if(!sw[0] && !sw[1] && !sw[2] && t.getCapacity()==6){
                    pTable.add(t);
                    sw[3]=true;
                }
            }
            else if(guestNumber==3){
                if(t.getCapacity()==3){
                    pTable.add(t);
                    sw[1]=true;
                }
                if(!sw[1] && t.getCapacity()==4){
                    pTable.add(t);
                    sw[2]=true;
                }
                if(!sw[1] && !sw[2] && t.getCapacity()==6){
                    pTable.add(t);
                    sw[3]=true;
                }
            }
            else if(guestNumber==4){
                if(t.getCapacity()==4){
                    pTable.add(t);
                    sw[2]=true;
                }
                if(!sw[1] && !sw[2] && t.getCapacity()==6){
                    pTable.add(t);
                    sw[3]=true;
                }
            }
            else if(guestNumber<=6){
                if(t.getCapacity()==6){
                    pTable.add(t);
                    sw[1]=true;
                }
            }
        }
        return pTable;
    }

    public Order order(List<MenuItem> menuItems,String token,UserRepository userRepository,OrderRepository orderRepository){
        User user = userRepository.findFirstByToken(token);
        if(user!=null) {
            if(orderRepository.findFirstByTable(user.getTable())==null) {
                Order o = new Order(menuItems, user.getTable());
                user.getTable().setOrder(o);
                try {
                    orderRepository.save(o);
                } catch (Exception e) {
                    System.out.println(e);
                }
                return o;
            }
        }
        return null;
    }

    public List<Order> showAllOrder(OrderRepository orderRepository){
        return orderRepository.findAllByOrderState(OrderState.NOTCHECKED);
    }

    public boolean changeOrderStateToPending(int id,OrderRepository orderRepository){
        Order o = orderRepository.findById(id);
        if(o==null)
            return false;
        o.setOrderState(OrderState.PENDING);
        orderRepository.save(o);
        return true;
    }

    public boolean changeOrderStateToChecked(int id,OrderRepository orderRepository){
        Order o = orderRepository.findById(id);
        if(o==null || o.getOrderState()!=OrderState.PENDING)
            return false;
        o.setOrderState(OrderState.CHECKED);
        orderRepository.save(o);
        return true;
    }

    public List<Order> showAllOrderCheckedAndNotDelivered(OrderRepository orderRepository){
        return orderRepository.findAllByOrderStateAndDelivered(OrderState.CHECKED,false);
    }

    public boolean deliverOrder(int id,OrderRepository orderRepository){
        Order o = orderRepository.findById(id);
        if(o==null)
            return false;
        o.setDelivered(true);
        orderRepository.save(o);
        return true;
    }
}

