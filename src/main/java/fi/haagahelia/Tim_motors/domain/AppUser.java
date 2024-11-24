package fi.haagahelia.tim_motors.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user") // Name the user table
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Set id to be primary key
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SavedCar> savedCars = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    private List<Car> cars = new ArrayList<>();

    // Getters and setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<SavedCar> getSavedCars() {
        return savedCars;
    }

    public void setSavedCars(List<SavedCar> savedCars) {
        this.savedCars = savedCars;
    }

    public AppUser findByEmail(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

}
