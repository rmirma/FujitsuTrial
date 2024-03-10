package com.example.fujitsutrial.controllers;

import com.example.fujitsutrial.config.Config;
import com.example.fujitsutrial.services.DeliveryFeeCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryFeeController {
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public DeliveryFeeController(DeliveryFeeCalculator deliveryFeeCalculator) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    @GetMapping("/delivery-fee")
    public ResponseEntity<Double> getDeliveryFee(
            @RequestParam("city") Config.City city,
            @RequestParam("vehicleType") Config.VehicleType vehicleType) {
        try {
            double deliveryFee = deliveryFeeCalculator.calculateDeliveryFee(city, vehicleType);
            return ResponseEntity.ok(deliveryFee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}