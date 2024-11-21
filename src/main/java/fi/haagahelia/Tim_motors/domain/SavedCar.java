package fi.haagahelia.tim_motors.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "saved_car") // table naming
public class SavedCar {
    @Id // id annotation to assign id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to AppUser
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false) // Foreign key to Car
    private Car car;

    public SavedCar() {
    }

    public SavedCar(AppUser user, Car car) {
        this.user = user;
        this.car = car;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
