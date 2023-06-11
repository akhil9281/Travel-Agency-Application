package com.akhil_kumar.travelagency.service;

import com.akhil_kumar.travelagency.entity.Activity;
import com.akhil_kumar.travelagency.entity.TravelPackage;
import com.akhil_kumar.travelagency.entity.Destination;
import com.akhil_kumar.travelagency.repository.DestinationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    @Autowired
    ActivityService activityService;

    @Autowired
    private DestinationRepo destinationRepo;

    public boolean destinationExists(Long destinationId) {
        return destinationRepo.existsById(destinationId);
    }

    public Destination addDestinationActivity(Long destinationId, Activity activity) {
        Destination destination = destinationRepo.getReferenceById(destinationId);
        destination.getActivityList().add(activity);
        activity.setDestination(destination);
        activityService.saveActivity(activity);
        return destinationRepo.save(destination);
    }


    public Destination updateDestination(Destination destination) {
        return destinationRepo.save(destination);
    }
}
