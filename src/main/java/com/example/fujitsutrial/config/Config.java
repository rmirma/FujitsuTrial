package com.example.fujitsutrial.config;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public enum VehicleType {
        CAR, SCOOTER, BIKE
    }

    public enum City {
        TALLINN, TARTU, PARNU
    }

    /**
     * Returns the regional base fee for the given city and vehicle type.
     * @param city        the city
     * @param vehicleType the vehicle type
     * @return the regional base fee
     */
    public double getRegionalBaseFee(City city, VehicleType vehicleType) {
        return switch (city) {
            case TALLINN -> switch (vehicleType) {
                case CAR -> 4.0;
                case SCOOTER -> 3.5;
                case BIKE -> 3.0;
            };
            case TARTU -> switch (vehicleType) {
                case CAR -> 3.5;
                case SCOOTER -> 3.0;
                case BIKE -> 2.5;
            };
            case PARNU -> switch (vehicleType) {
                case CAR -> 3.0;
                case SCOOTER -> 2.5;
                case BIKE -> 2.0;
            };
        };
    }

    /**
     * Returns the extra fee based on the air temperature for the given weather data and vehicle type.
     * @param weatherData the weather data
     * @param vehicleType the vehicle type
     * @return the extra fee
     */
    public double getAirTemperatureExtraFee(WeatherDataEntity weatherData, VehicleType vehicleType) {
        double airTemperature = weatherData.getAirtemperature();
        if (vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) {
            if (airTemperature < -10) {
                return 1.0;
            } else if (airTemperature >= -10 && airTemperature < 0) {
                return 0.5;
            }
        }
        return 0.0;
    }

    /**
     * Returns the extra fee based on the wind speed for the given weather data and vehicle type.
     * @param weatherData the weather data
     * @param vehicleType the vehicle type
     * @return the extra fee
     * @throws Exception if the wind speed is too high for the given vehicle type
     */
    public double getWindSpeedExtraFee(WeatherDataEntity weatherData, VehicleType vehicleType) throws Exception {
        double windSpeed = weatherData.getWindspeed();

        if (vehicleType == VehicleType.BIKE) {
            if (windSpeed >= 10 && windSpeed <= 20) {
                return 0.5;
            } else if (windSpeed > 20) {
                throw new Exception("Usage of selected vehicle type is forbidden");
            }
        }
        return 0.0;
    }

    /**
     * Returns the extra fee based on the weather phenomenon for the given weather data and vehicle type.
     * @param weatherData the weather data
     * @param vehicleType the vehicle type
     * @return the extra fee
     * @throws Exception if the weather phenomenon is too severe for the given vehicle type
     */
    public double getWeatherPhenomenonExtraFee(WeatherDataEntity weatherData, VehicleType vehicleType) throws Exception {
        String weatherPhenomenon = weatherData.getWeatherphenomenon();

        if (vehicleType == VehicleType.SCOOTER || vehicleType == VehicleType.BIKE) {
            if (weatherPhenomenon.contains("snow") || weatherPhenomenon.contains("sleet")) {
                return 1.0;
            } else if (weatherPhenomenon.contains("rain") || weatherPhenomenon.contains("shower")){
                return 0.5;
            } else if (weatherPhenomenon.contains("glaze") || weatherPhenomenon.contains("hail") || weatherPhenomenon.contains("thunder")) {
                throw new Exception("Usage of selected vehicle type is forbidden");
            }
        }
        return 0.0;
    }
}