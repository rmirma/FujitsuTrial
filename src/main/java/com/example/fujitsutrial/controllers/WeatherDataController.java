package com.example.fujitsutrial.controllers;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import com.example.fujitsutrial.services.WeatherDataImport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WeatherDataController {
    private final WeatherDataImport weatherDataImport;

    public WeatherDataController(WeatherDataImport weatherDataImport) {
        this.weatherDataImport = weatherDataImport;
    }

    @GetMapping("/weather-data")
    public List<WeatherDataEntity> getAllWeatherData() {
        return weatherDataImport.getAllWeatherData();
    }
}