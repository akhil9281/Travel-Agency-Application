package com.akhil_kumar.travelagency.repository;

import com.akhil_kumar.travelagency.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepo extends JpaRepository<Activity, Long> {
}
