package com.akhil_kumar.travelagency.repository;

import com.akhil_kumar.travelagency.entity.Passenger;
import com.akhil_kumar.travelagency.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface TravelPackageRepo extends JpaRepository<TravelPackage, Long> {
    @Transactional
    @Modifying
    @Query("update TravelPackage t set t.passengerCapacity = ?1, t.passengerList = ?2 where t.id = ?3")
    void updatePassengerCapacityAndPassengerListById(Integer passengerCapacity, Passenger passengerList, Long travelPackageId);

    @Query("select t from TravelPackage t")
    List<TravelPackageInfo> getAll();

    interface TravelPackageInfo {
        Long getId();

        String getName();
    }
}

