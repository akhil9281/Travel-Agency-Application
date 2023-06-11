package com.akhil_kumar.travelagency.repository;

import com.akhil_kumar.travelagency.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepo extends JpaRepository<Destination, Long> {
}
