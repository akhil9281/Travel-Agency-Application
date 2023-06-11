package com.akhil_kumar.travelagency.repository;

import com.akhil_kumar.travelagency.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {
}
