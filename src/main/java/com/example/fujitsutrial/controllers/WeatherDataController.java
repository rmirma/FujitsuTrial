package com.example.fujitsutrial.controllers;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import com.example.fujitsutrial.services.WeatherDataImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WeatherDataController {
    private final WeatherDataImport weatherDataImport;
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataController.class);

    public WeatherDataController(WeatherDataImport weatherDataImport) {
        this.weatherDataImport = weatherDataImport;
    }

    @GetMapping("/weather-data")
    public List<WeatherDataEntity> getAllWeatherData() {
        logger.info("Request received for all weather data");
        return weatherDataImport.getAllWeatherData();
    }
}