package restaurant.classes;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name="resTable")
public class Table {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private int capacity;

    private boolean reserved;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "table")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "table")
    private User user;

    public Table(){}

    public Table(int capacity){
        this.capacity=capacity;
        this.reserved=false;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReserved() {
        return reserved;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public boolean reserveTable(User user){
        if(isReserved())
            return false;
        else{
            this.user=user;
            this.reserved=true;
            return true;
        }
    }

    public boolean deReserve(){
        if(!isReserved())
            return false;
        else{
            this.user=null;
            this.reserved=false;
            return true;
        }
    }


}
