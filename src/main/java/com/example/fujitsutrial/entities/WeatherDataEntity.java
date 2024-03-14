package com.example.fujitsutrial.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "WEATHER_DATA")
public class WeatherDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String wmocode;

    @Column(nullable = false)
    private Double airtemperature;

    @Column(nullable = false)
    private Double windspeed;

    @Column(nullable = false)
    private String weatherphenomenon;

    @Column(nullable = false)
    private String timestamp;


    public Double getAirtemperature() {
        return airtemperature;
    }

    public Double getWindspeed() {
        return windspeed;
    }

    public String getWeatherphenomenon() {
        return weatherphenomenon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setWmocode(String wmocode) {
        this.wmocode = wmocode;
    }

    public void setAirtemperature(Double airtemperature) {
        this.airtemperature = airtemperature;
    }

    public void setWindspeed(Double windspeed) {
        this.windspeed = windspeed;
    }

    public void setWeatherphenomenon(String weatherphenomenon) {
        this.weatherphenomenon = weatherphenomenon;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}