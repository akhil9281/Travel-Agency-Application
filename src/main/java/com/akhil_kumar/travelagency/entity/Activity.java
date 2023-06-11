package com.akhil_kumar.travelagency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Double cost;

    private Integer totalCapacity;

    private Integer numberOfPassengers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id")
    private Destination destination;

    public Activity(String name, String description, Double cost, Integer totalCapacity, Destination destination) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.totalCapacity = totalCapacity;
        this.destination = destination;
        this.numberOfPassengers = 0;
    }
}
