package com.akhil_kumar.travelagency.service;

import com.akhil_kumar.travelagency.entity.*;
import com.akhil_kumar.travelagency.Exception.InvalidRequestParameterException;
import com.akhil_kumar.travelagency.repository.PassengerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    PassengerRepo passengerRepo;

    public Passenger savePassenger(Passenger passenger) {
        return passengerRepo.save(passenger);
    }

    public boolean passengerExists(Long passengerId) {
        return passengerRepo.existsById(passengerId);
    }

    public Passenger getPassenger(Long passengerId) {
        return passengerRepo.findById(passengerId).get();
    }

    public Passenger signUpToActivity(Long passengerId, Activity activity) {
        Passenger passenger = passengerRepo.getReferenceById(passengerId);
        Destination destination = activity.getDestination();
        TravelPackage travelPackage = destination.getTravelPackage();

        boolean checkForPassengerInPackage = false;
        for (Passenger passengerInTravelPackage : travelPackage.getPassengerList())
            if (passengerInTravelPackage.getId().equals(passengerId)) {
                checkForPassengerInPackage = true;
                break;
            }

        if (!checkForPassengerInPackage)
            throw new InvalidRequestParameterException("Passenger is not part of corresponding TravelPackage for the activity");

        if (activity.getNumberOfPassengers().equals(activity.getTotalCapacity()))
            throw new InvalidRequestParameterException("capacity of Activity is full");

        if (this.hasEnoughMoney(passenger, activity))
            return this.addActivity(passenger, activity);
        else
            throw new InvalidRequestParameterException("Passenger doesn't have enough balance");

    }

    private Passenger addActivity(Passenger passenger, Activity activity) {
        activity.setNumberOfPassengers(activity.getNumberOfPassengers() + 1);
        Double activityCost = activity.getCost();
        Double balance = passenger.getBalance();
        if (passenger.getType() == PassengerType.STANDARD)
            passenger.setBalance(balance - activityCost);

        else if (passenger.getType() == PassengerType.GOLD)
            passenger.setBalance(balance - activityCost*0.9);

        else if (passenger.getType() == PassengerType.PREMIUM)
            passenger.setBalance(1d);

        passenger.getActivityList().add(activity);
        return passenger;
    }

    public boolean hasEnoughMoney(Passenger passenger, Activity activity) {
        Double activityCost = activity.getCost();
        if (passenger.getType() == PassengerType.STANDARD)
            return passenger.getBalance() >= activityCost;

        if (passenger.getType() == PassengerType.GOLD)
            return passenger.getBalance() >= activityCost * 0.9;

        return true;
    }
}
