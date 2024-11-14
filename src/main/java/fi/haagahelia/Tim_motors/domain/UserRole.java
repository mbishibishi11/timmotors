package fi.haagahelia.tim_motors.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")   //table naming
public class UserRole {
    @Id   //id annotation to assign id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userRole")
    private List<AppUser> appUsers;
    
    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
}
