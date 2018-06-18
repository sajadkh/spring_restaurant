package restaurant.classes;

import javax.persistence.*;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(unique=true, nullable=false)
    private String name;

    private String phoneNumber;

    private String password;

    @JoinColumn(name="table_id", unique=true)
    @OneToOne
    private Table table;

    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String md5Hex = DigestUtils.md5Hex(password).toUpperCase();
        this.password=md5Hex;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean equals(User user) {
        return this.name.equals(user.getName());
    }
}
