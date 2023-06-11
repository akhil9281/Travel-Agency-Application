package com.akhil_kumar.travelagency.service;

import com.akhil_kumar.travelagency.entity.Destination;
import com.akhil_kumar.travelagency.entity.Passenger;
import com.akhil_kumar.travelagency.entity.TravelPackage;
import com.akhil_kumar.travelagency.Exception.InvalidRequestParameterException;
import com.akhil_kumar.travelagency.repository.TravelPackageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TravelPackageService {

    @Autowired
    TravelPackageRepo travelRepo;

    @Autowired
    DestinationService destinationService;

    public List<TravelPackageRepo.TravelPackageInfo> getAllTravelPackages() {
        return travelRepo.getAll();
    }

    public TravelPackage getTravelPackage(Long TravelPackageId) {
        Optional<TravelPackage> travelPackage = travelRepo.findById(TravelPackageId);
        List<Destination> destinationList = travelPackage.get().getDestinationList();
        return travelRepo.findById(TravelPackageId).get();
    }


    public List<TravelPackageRepo.TravelPackageInfo> addTravelPackage(TravelPackage travelPackage) {
        travelRepo.save(travelPackage);
        return this.getAllTravelPackages();
    }

    public boolean travelPackageExists(Long travelPackageId) {
        return travelRepo.existsById(travelPackageId);
    }

    public TravelPackage addDestination(Long travelPackageId, Destination destination) {
        TravelPackage travelPackage = travelRepo.getReferenceById(travelPackageId);
        travelPackage.getDestinationList().add(destination);
        destination.setTravelPackage(travelPackage);
        destinationService.updateDestination(destination);
        return travelRepo.save(travelPackage);
    }

    public TravelPackage addPassenger(Long travelPackageId, Passenger passenger) {
        TravelPackage travelPackage = travelRepo.getReferenceById(travelPackageId);
        if (travelPackage.getPassengerCapacity() == 0)
            throw new InvalidRequestParameterException("Capacity full the given Travel Package");
        travelPackage.getPassengerList().add(passenger);
        travelPackage.setPassengerCapacity(travelPackage.getPassengerCapacity() - 1);

        return travelRepo.save(travelPackage);
    }
}
