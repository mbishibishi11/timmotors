package fi.haagahelia.Tim_motors.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "car") //table name in the database
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Assign id as primary key
    private long id;

    private String brand;
    private String model;
    private String ManufactureYear;
    private String mileage;
    private String fuel;
    private String transmission;
    private Double price;
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getManufactureYear() {
        return ManufactureYear;
    }
    public void setManufactureYear(String manufactureYear) {
        ManufactureYear = manufactureYear;
    }
    public String getMileage() {
        return mileage;
    }
    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
    public String getFuel() {
        return fuel;
    }
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
    public String getTransmission() {
        return transmission;
    }
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    /*This is a comment added from github*/
    

}
