package com.akhil_kumar.travelagency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Destination {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String name;

    @OneToMany(mappedBy = "destination")
    private List<Activity> activityList;

    @ManyToOne
    @JoinColumn(name = "travelPackage_id")
    private TravelPackage travelPackage;

    public Destination(String name) {
        this.name = name;
    }

    public Destination(String name, List<Activity> activityList) {
        this.name = name;
        this.activityList = activityList;
    }

    public Destination(String name, List<Activity> activityList, TravelPackage travelPackage) {
        this.name = name;
        this.activityList = activityList;
        this.travelPackage = travelPackage;
    }
}
