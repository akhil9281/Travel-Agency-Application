package com.akhil_kumar.travelagency.service;

import com.akhil_kumar.travelagency.entity.Activity;
import com.akhil_kumar.travelagency.repository.ActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @Transactional
public class ActivityService {

    @Autowired
    ActivityRepo activityRepo;

    public Map<Activity, Integer> allAvailableActivity() {
        List<Activity> activityList =  activityRepo.findAll();
        Map<Activity, Integer> availableActivityList = new HashMap<>();

        for (Activity activity : activityList) {
            int spaceLeft = activity.getTotalCapacity() - activity.getNumberOfPassengers();
            if (spaceLeft > 0) {
                availableActivityList.put(activity, spaceLeft);
            }
        }
        return availableActivityList;
    }

    public Activity saveActivity(Activity activity) {
        return activityRepo.save(activity);
    }

    public boolean activityExists(Long activityId) {
        return activityRepo.existsById(activityId);
    }

    public Activity getActivity(Long activityId) {
        return activityRepo.findById(activityId).get();
    }

}
