package restaurant.classes;

import javax.persistence.*;
import java.util.List;

@Entity
@javax.persistence.Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToMany
    @JoinTable(name = "orders_items", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    private List<MenuItem> items;

//    private int[] orders;

    private boolean delivered;

    private int price;

    private int turn;

    private OrderState orderState;

    @JoinColumn(name="table_id", unique=true)
    @OneToOne
    private Table table;

    public Order(){ }

    public Order(List<MenuItem> items,Table table){
        this.orderState=OrderState.NOTCHECKED;
        this.items=items;
        this.delivered=false;
        int price=0;
        for (MenuItem item:items) {
            price+=item.getPrice();
        }
        this.price=price;
        this.table=table;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}

