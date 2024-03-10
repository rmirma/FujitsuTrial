package com.example.fujitsutrial;

import com.example.fujitsutrial.services.WeatherDataImport;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private final WeatherDataImport weatherDataImport;

    public ScheduledTasks(WeatherDataImport weatherDataImport) {
        this.weatherDataImport = weatherDataImport;
    }
}