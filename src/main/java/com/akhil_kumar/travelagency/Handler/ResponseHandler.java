package com.akhil_kumar.travelagency.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.*;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(@Nullable String message,
                                                          HttpStatus status, Object ...responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("message", message);
            map.put("status", status.value());
            map.put("data", responseObj);

            return new ResponseEntity<>(map, status);
        }
        catch (Exception e) {
            map.clear();
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", e.getMessage());
            map.put("data", null);
            return new ResponseEntity<Object>(map,status);
        }
    }

    public static ResponseEntity<Object> generateResponse(HttpStatus status, Object ...responseObj) {
        return ResponseHandler.generateResponse(null, status, responseObj);
    }
}
