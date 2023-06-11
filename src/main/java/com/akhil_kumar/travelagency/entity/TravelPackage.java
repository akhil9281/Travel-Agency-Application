package com.akhil_kumar.travelagency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TravelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer passengerCapacity;

    @OneToMany(mappedBy = "travelPackage")
    private List<Destination> destinationList;

    @ManyToMany(mappedBy = "travelPackages")
    private List<Passenger> passengerList;

    public TravelPackage(String name, Integer passengerCapacity, List<Destination> destinationList, List<Passenger> passengerList) {
        this.name = name;
        this.passengerCapacity = passengerCapacity;
        this.destinationList = destinationList;
        this.passengerList = passengerList;
    }

    public TravelPackage(String name, Integer passengerCapacity) {
        this.name = name;
        this.passengerCapacity = passengerCapacity;
    }
}
