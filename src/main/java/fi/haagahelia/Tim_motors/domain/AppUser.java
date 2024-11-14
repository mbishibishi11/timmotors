package fi.haagahelia.tim_motors.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user") //Name the user table
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Set id to be primary key
    private long id;
    private String firstName, lastName, email;

    @ManyToOne  //create relationship: one role can be held by many users
    @JoinColumn(name = "role_id") //create the role_id attribute
    private UserRole userRole;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    private List<Car> cars;


    //Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    


}
