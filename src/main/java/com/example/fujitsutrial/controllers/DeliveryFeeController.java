package com.example.fujitsutrial.controllers;

import com.example.fujitsutrial.config.Config;
import com.example.fujitsutrial.services.DeliveryFeeCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
public class DeliveryFeeController {
    private final DeliveryFeeCalculator deliveryFeeCalculator;
    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeController.class);

    public DeliveryFeeController(DeliveryFeeCalculator deliveryFeeCalculator) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    @GetMapping("/delivery-fee")
    public ResponseEntity<Double> getDeliveryFee(
            @RequestParam("city") Config.City city,
            @RequestParam("vehicleType") Config.VehicleType vehicleType) {
        logger.info("Request received for delivery fee calculation for city: " + city + " and vehicle type: " + vehicleType);
        try {
            double deliveryFee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
            return ResponseEntity.ok(deliveryFee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}