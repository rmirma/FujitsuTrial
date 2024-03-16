package com.example.fujitsutrial;

import com.example.fujitsutrial.config.Config;
import com.example.fujitsutrial.entities.WeatherDataEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FujitsuTrialApplicationTests {
    @Autowired
    private Config config;

    @Test
    void testRegionalBaseFee() {
        assertEquals(4.0, config.getRegionalBaseFee(Config.City.TALLINN, Config.VehicleType.CAR));
        assertEquals(3.0, config.getRegionalBaseFee(Config.City.TARTU, Config.VehicleType.SCOOTER));
        assertEquals(2.0, config.getRegionalBaseFee(Config.City.PARNU, Config.VehicleType.BIKE));
    }

    @Test
    void testTempExtraFee() {
        WeatherDataEntity weatherData = new WeatherDataEntity();
        weatherData.setAirtemperature(-15.0);
        assertEquals(1.0, config.getAirTemperatureExtraFee(weatherData, Config.VehicleType.SCOOTER));

        weatherData.setAirtemperature(-5.0);
        assertEquals(0.5, config.getAirTemperatureExtraFee(weatherData, Config.VehicleType.SCOOTER));

        weatherData.setAirtemperature(5.0);
        assertEquals(0.0, config.getAirTemperatureExtraFee(weatherData, Config.VehicleType.SCOOTER));
    }

    @Test
    void testWindExtraFee() throws Exception {
        WeatherDataEntity weatherData = new WeatherDataEntity();
        weatherData.setWindspeed(15.0);
        assertEquals(0.5, config.getWindSpeedExtraFee(weatherData, Config.VehicleType.BIKE));

        weatherData.setWindspeed(5.0);
        assertEquals(0.0, config.getWindSpeedExtraFee(weatherData, Config.VehicleType.BIKE));
    }

    @Test
    void testWindExtraFeeException(){
        WeatherDataEntity weatherData = new WeatherDataEntity();
        String exceptionMessage = "Usage of selected vehicle type is forbidden";
        weatherData.setWindspeed(22.0);
        Exception exception = assertThrows(Exception.class, () -> config.getWindSpeedExtraFee(weatherData, Config.VehicleType.BIKE));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    void testPhenomenonExtraFee() throws Exception {
        WeatherDataEntity weatherData = new WeatherDataEntity();
        weatherData.setWeatherphenomenon("snow");
        assertEquals(1.0, config.getWeatherPhenomenonExtraFee(weatherData, Config.VehicleType.SCOOTER));

        weatherData.setWeatherphenomenon("rain");
        assertEquals(0.5, config.getWeatherPhenomenonExtraFee(weatherData, Config.VehicleType.SCOOTER));

        weatherData.setWeatherphenomenon("clear");
        assertEquals(0.0, config.getWeatherPhenomenonExtraFee(weatherData, Config.VehicleType.SCOOTER));
    }

    @Test
    void testPhenomenonExtraFeeException(){
        WeatherDataEntity weatherData = new WeatherDataEntity();
        String exceptionMessage = "Usage of selected vehicle type is forbidden";
        weatherData.setWeatherphenomenon("glaze");
        Exception exception = assertThrows(Exception.class, () -> config.getWeatherPhenomenonExtraFee(weatherData, Config.VehicleType.BIKE));
        assertEquals(exceptionMessage, exception.getMessage());
    }
}