package restaurant.classes;

import javax.persistence.*;
import java.util.List;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String name;

    private int price;

    @ManyToMany(mappedBy = "items")
    private List<Order> orders;

    public MenuItem(){}

    public MenuItem(String name,int price){
        this.name=name;
        this.price=price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
