package com.example.fujitsutrial;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import com.example.fujitsutrial.repositories.WeatherDataRepository;
import com.example.fujitsutrial.services.WeatherDataImport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherServiceTest {
    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private WeatherDataImport weatherDataImport;

    @Test
    void testDatabaseConnection() {
        assertDoesNotThrow(() -> weatherDataRepository.findAll());
    }

    @Test
    void testGetWeatherObservation() {
        List<WeatherDataEntity> weatherDataEntities = weatherDataImport.getAllWeatherData();
        assertNotNull(weatherDataEntities);
        assertFalse(weatherDataEntities.isEmpty());
    }

    @Test
    void testIfWeatherDataGetsParsed() throws IOException {
        String xmlData = weatherDataImport.fetchWeatherData();
        List<WeatherDataEntity> weatherDataEntities = weatherDataImport.parseWeatherData(xmlData);
        assertNotNull(weatherDataEntities);
        assertFalse(weatherDataEntities.isEmpty());
    }

    @Test
    void testIfWeatherDataGetsSavedToDatabase() {
        WeatherDataEntity weatherDataEntity = new WeatherDataEntity();
        weatherDataEntity.setStationName("TestStation");
        weatherDataEntity.setWmocode("12345");
        weatherDataEntity.setAirtemperature(10.0);
        weatherDataEntity.setWindspeed(5.0);
        weatherDataEntity.setWeatherphenomenon("Rain");
        weatherDataEntity.setTimestamp("1609502400");

        weatherDataRepository.save(weatherDataEntity);

        WeatherDataEntity savedWeatherData = weatherDataRepository.findTopByStationNameOrderByTimestampDesc("TestStation");

        assertNotNull(savedWeatherData);
        assertEquals("TestStation", savedWeatherData.getStationName());
        assertEquals("12345", savedWeatherData.getWmocode());
        assertEquals(10.0, savedWeatherData.getAirtemperature());
        assertEquals(5.0, savedWeatherData.getWindspeed());
        assertEquals("Rain", savedWeatherData.getWeatherphenomenon());
        assertEquals("1609502400", savedWeatherData.getTimestamp());
    }

    @Test
    void testIfWeatherDataGetsImported() {
        assertDoesNotThrow(() -> weatherDataImport.importWeatherData());
    }
}