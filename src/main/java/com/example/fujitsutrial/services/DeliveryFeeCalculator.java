package com.example.fujitsutrial.services;

import com.example.fujitsutrial.config.Config;
import com.example.fujitsutrial.entities.WeatherDataEntity;
import com.example.fujitsutrial.repositories.WeatherDataRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFeeCalculator {
    private final WeatherDataRepository weatherDataRepository;
    private final Config config;

    public DeliveryFeeCalculator(WeatherDataRepository weatherDataRepository, Config config) {
        this.weatherDataRepository = weatherDataRepository;
        this.config = config;
    }

    /**
     * It first finds the latest weather data for the given city. Then it calculates the delivery fee based on the weather data.
     * @param city        - city where the delivery is made
     * @param vehicleType - type of the vehicle used for delivery
     * @return delivery fee
     * @throws Exception if city is not found
     */
    public double calculateDeliveryFee(Config.City city, Config.VehicleType vehicleType) throws Exception {
        WeatherDataEntity latestWeatherData = switch (city.name()) {
            case "TALLINN" -> weatherDataRepository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku");
            case "TARTU" -> weatherDataRepository.findTopByStationNameOrderByTimestampDesc("Tartu-Tõravere");
            case "PÄRNU" -> weatherDataRepository.findTopByStationNameOrderByTimestampDesc("Pärnu");
            default -> throw new Exception("City not found");
        };

        double regionalBaseFee = config.getRegionalBaseFee(city, vehicleType);
        double airTemperatureExtraFee = config.getAirTemperatureExtraFee(latestWeatherData, vehicleType);
        double windSpeedExtraFee = config.getWindSpeedExtraFee(latestWeatherData, vehicleType);
        double weatherPhenomenonExtraFee = config.getWeatherPhenomenonExtraFee(latestWeatherData, vehicleType);

        return regionalBaseFee + airTemperatureExtraFee + windSpeedExtraFee + weatherPhenomenonExtraFee;
    }
}