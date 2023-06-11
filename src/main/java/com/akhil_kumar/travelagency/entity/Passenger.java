package com.akhil_kumar.travelagency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Passenger {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id; // passenger number

    private String name;

    private Integer passengerNumber;

    private Double balance;

    @Enumerated(EnumType.STRING)
    private PassengerType type;

    @ManyToMany
    @JoinTable(
            name = "passenger_activity",
            joinColumns = @JoinColumn(name = "passenger_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<Activity> activityList;

    @ManyToMany
    @JoinTable(name = "passenger_travel_package",
            joinColumns = @JoinColumn(name = "passenger_id"))
    private List<TravelPackage> travelPackages = new ArrayList<>();

    public Passenger(String name, Integer passengerNumber, Double balance, PassengerType type) {
        this.name = name;
        this.passengerNumber = passengerNumber;
        this.balance = balance;
        this.type = type;
    }
}
