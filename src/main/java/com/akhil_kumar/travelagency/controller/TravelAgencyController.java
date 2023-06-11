package com.akhil_kumar.travelagency.controller;

import com.akhil_kumar.travelagency.Exception.InvalidRequestParameterException;
import com.akhil_kumar.travelagency.Handler.ResponseHandler;
import com.akhil_kumar.travelagency.entity.*;
import com.akhil_kumar.travelagency.service.ActivityService;
import com.akhil_kumar.travelagency.service.DestinationService;
import com.akhil_kumar.travelagency.service.PassengerService;
import com.akhil_kumar.travelagency.service.TravelPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TravelAgencyController {

    @Autowired
    TravelPackageService travelPackageService;

    @Autowired
    DestinationService destinationService;

    @Autowired
    ActivityService activityService;

    @Autowired
    PassengerService passengerService;

    /**
     * method -> Print itinerary of the travel package
     * @param travelPackageId
     * @return package name, Destinations in the Package
     */
    @GetMapping("/travelPackage/{travelPackageId}/itinerary")
    public ResponseEntity<Object> getPackageItinerary(@PathVariable Long travelPackageId) {
        if (travelPackageId == null)
            throw new InvalidRequestParameterException("TravelPackageID is null");
        if (!travelPackageService.travelPackageExists(travelPackageId))
            throw new InvalidRequestParameterException("Invalid ID, TravelPackage doesn't exist of the ID");

        TravelPackage travelPackage = travelPackageService.getTravelPackage(travelPackageId);
        return ResponseHandler.generateResponse(HttpStatus.OK, travelPackage.getName(), travelPackage.getDestinationList());
    }
    /**
     * method -> Print passenger list of the travel package:
     * @param travelPackageId
     * @return package name, passenger capacity, number of passenger, details of each passenger
     */
    @GetMapping("/travelPackage/{travelPackageId}/passengerList")
    public ResponseEntity<Object> getPackagePassengers(@PathVariable Long travelPackageId) {
        if (travelPackageId == null)
            throw new InvalidRequestParameterException("TravelPackageID is null");
        if (!travelPackageService.travelPackageExists(travelPackageId))
            throw new InvalidRequestParameterException("Invalid ID, TravelPackage doesn't exist of the ID");

        TravelPackage travelPackage = travelPackageService.getTravelPackage(travelPackageId);
        return ResponseHandler.generateResponse(
                HttpStatus.OK,      travelPackage.getName(),
                travelPackage.getPassengerCapacity(), travelPackage.getPassengerList().size());
    }

    /**
     * method -> Print details of passenger:
     * @param passengerId
     * @return passenger name, passenger number, list of each activity, balance
     */
    @GetMapping("/passengers/{passengerId}")
    public ResponseEntity<Object> getPassengerDetails(@PathVariable Long passengerId) {
        Passenger passenger = passengerService.getPassenger(passengerId);
        double multiplyBy = 0;
        if (passenger.getType() == PassengerType.STANDARD)
            multiplyBy = 1d;
        if (passenger.getType() == PassengerType.GOLD)
            multiplyBy = 0.9d;
        if (passenger.getType() == PassengerType.PREMIUM)
            multiplyBy = 0d;
        List<Activity> activityList = passenger.getActivityList();
        for (Activity activity : activityList)
            activity.setCost(activity.getCost()*multiplyBy);

        return ResponseHandler.generateResponse(HttpStatus.OK,
                passenger.getName(), passenger.getPassengerNumber(),
                passenger.getActivityList());
    }

    /**
     * Print the details of all the activities that still have spaces available,
     * including how many spaces are available
     */
    @GetMapping("/activity/available")
    public ResponseEntity<Object> getAvailableActivity() {
        return ResponseHandler.generateResponse(HttpStatus.OK, activityService.allAvailableActivity());
    }

    @PostMapping(value = "/travelPackage/create")
    public ResponseEntity<Object> createTravelPackage(@RequestBody TravelPackage travelPackage) {
        if (travelPackage == null || travelPackage.getName() == null ||
                travelPackage.getPassengerCapacity() == null || travelPackage.getPassengerCapacity() == 0)
            throw new InvalidRequestParameterException("Invalid Travel Package");

        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                travelPackageService.addTravelPackage(travelPackage));
    }

    @PostMapping("/travelPackage/{travelPackageId}/destination")
    public ResponseEntity<Object> addDestinationToTravelPackage(
            @PathVariable Long travelPackageId,
            @RequestBody Destination destination) {
        if (travelPackageId == null)
            throw new InvalidRequestParameterException("TravelPackageId is null");

        if (!travelPackageService.travelPackageExists(travelPackageId))
            throw new InvalidRequestParameterException("Invalid Travel Package ID provided");


        if (destination == null || destination.getName() == null)
            throw new InvalidRequestParameterException("Invalid Destination provided");


        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                travelPackageService.addDestination(travelPackageId, destination));
    }

    /**
     * adding Activity to Destination
     *
     * @param destinationId
     * @param activity
     * @return
     */
    @PostMapping("/destinations/{destinationId}/activities")
    public ResponseEntity<Object> addActivityToDestination(
            @PathVariable Long destinationId,
            @RequestBody Activity activity) {
        if (destinationId == null)
            throw new InvalidRequestParameterException("DestinationID is null");

        if (activity == null || activity.getName() == null)
            throw new InvalidRequestParameterException("Activity is null");

        if (!destinationService.destinationExists(destinationId))
            throw new InvalidRequestParameterException("Invalid DestinationID is null");

        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                destinationService.addDestinationActivity(destinationId, activity));
    }

    /**
     * creating a new Passenger
     * @param passenger
     * @return
     */
    @PostMapping("/passenger/create")
    public ResponseEntity<Object> createPassenger(@RequestBody Passenger passenger) {
        if (passenger == null)
            throw new InvalidRequestParameterException("Null Passenger");

        if (passenger.getPassengerNumber() == null || passenger.getName() == null)
            throw new InvalidRequestParameterException("Invalid Passenger details");

        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                passengerService.savePassenger(passenger));
    }

    /**
     * creating a new Passenger and adding it to a TravelPackage
     * @param travelPackageId
     * @param passenger
     * @return
     */
    @PostMapping("/travelPackage/{travelPackageId}/passengers")
    public ResponseEntity<Object> addPassengerToTravelPackage(
            @PathVariable Long travelPackageId,
            @RequestBody Passenger passenger) {

        if (travelPackageId == null)
            throw new InvalidRequestParameterException("TravelPackageID is null");

        if (passenger == null || passenger.getName() == null || passenger.getPassengerNumber() == null)
            throw new InvalidRequestParameterException("Invalid Passenger provided");

        if (!travelPackageService.travelPackageExists(travelPackageId)) {
            throw new InvalidRequestParameterException("TravelPackage doesn't exist for this ID");
        }
        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                travelPackageService.addPassenger(travelPackageId, passenger));
    }

    /**
     * Adding existing Passenger to a TravelPackage
     * @param travelPackageId
     * @param passengerId
     * @return
     */
    @PostMapping("/travelPackage/{travelPackageId}/passengers/{passengerId}")
    public ResponseEntity<Object> addPassengerToTravelPackage(
            @PathVariable Long travelPackageId,
            @PathVariable Long passengerId) {

        if (travelPackageId == null)
            throw new InvalidRequestParameterException("TravelPackageID is null");

        if (passengerId == null)
            throw new InvalidRequestParameterException("PassengerID is null");

        if (!travelPackageService.travelPackageExists(travelPackageId))
            throw new InvalidRequestParameterException("TravelPackage doesn't exist for this ID");

        if (!passengerService.passengerExists(passengerId))
            throw new InvalidRequestParameterException("Passenger doesn't exist for this ID");

        Passenger passenger = passengerService.getPassenger(passengerId);
        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                travelPackageService.addPassenger(travelPackageId, passenger));
    }

    /**
     * creating a new Activity, but not assigning to any Destination
     * @param activity
     * @return
     */
    @PostMapping("/activity/create")
    public ResponseEntity<Object> createActivity( @RequestBody Activity activity ) {
        if (activity == null )
            throw new InvalidRequestParameterException("activity provided is null");
        if (activity.getName() == null || activity.getCost() == null || activity.getTotalCapacity() == null)
            throw new InvalidRequestParameterException("Invalid activity properties");

        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                activityService.saveActivity(activity));
    }

    /**
     * Signing up Passenger to an existing Activity
     * @param passengerId
     * @param activityId
     * @return
     */
    @PostMapping("/passengers/{passengerId}/activities")
    public ResponseEntity<Object> signUpPassengerForActivity(
            @PathVariable Long passengerId,
            @RequestParam Long activityId) {
        if(passengerId == null)
            throw new InvalidRequestParameterException("PassengerId is null");
        if(activityId == null)
            throw new InvalidRequestParameterException("ActivityId is null");

        if(!passengerService.passengerExists(passengerId))
            throw new InvalidRequestParameterException("Passenger doesn't exist for this ID");
        if(!activityService.activityExists(activityId))
            throw new InvalidRequestParameterException("Activity doesn't exist for this ID");

        Activity activity = activityService.getActivity(activityId);
        return ResponseHandler.generateResponse(HttpStatus.CREATED,
                passengerService.signUpToActivity(passengerId, activity));
    }

}