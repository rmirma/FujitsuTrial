package com.example.fujitsutrial.services;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import com.example.fujitsutrial.repositories.WeatherDataRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherDataImport {
    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate;
    private static final String WEATHER_DATA_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<String> STATION_NAMES = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataImport.class);

    public WeatherDataImport(WeatherDataRepository weatherDataRepository, RestTemplate restTemplate) {
        this.weatherDataRepository = weatherDataRepository;
        this.restTemplate = restTemplate;
    }

    public String fetchWeatherData() {
        return restTemplate.getForObject(WEATHER_DATA_URL, String.class);
    }

    /**
     * Check if the database is empty or if the latest weather data is older than 1 hour
     * Import up-to-date weather data from the external API and save it to the database on initial launch
     */
    @PostConstruct
    public void init() {
        WeatherDataEntity latestWeatherData = (weatherDataRepository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku"));
        if (latestWeatherData == null) {
            importWeatherData();
        } else {
            Instant lastImport = Instant.ofEpochSecond(Long.parseLong(latestWeatherData.getTimestamp()));
            Instant now = Instant.now();
            if (now.minusSeconds(3600).isAfter(lastImport)) {
                importWeatherData();
            }
        }
    }

    /**
     * Parse the XML data and return a list of WeatherDataEntity objects
     * @param xmlData the XML data to be parsed
     * @return a list of WeatherDataEntity objects
     * @throws IOException if the XML data cannot be parsed
     */
    public List<WeatherDataEntity> parseWeatherData(String xmlData) throws IOException {
        List<WeatherDataEntity> weatherDataEntities = new ArrayList<>();
        String timestamp = xmlData.split("<observations timestamp=\"")[1].split("\">")[0];
        String[] stations = xmlData.split("<station>");
        for (String station : stations) {
            for (String stationName : STATION_NAMES) {
                if (station.contains(stationName + "</name>")) {
                    WeatherDataEntity weatherDataEntity = getWeatherDataEntity(station, timestamp);
                    weatherDataEntities.add(weatherDataEntity);
                }
            }
        }
        logger.info("Parsed weather data for " + timestamp);
        return weatherDataEntities;
    }

    /**
     * Create a WeatherDataEntity object from the station data and timestamp
     * @param stationData the station data to be parsed
     * @param timestamp   the timestamp of the weather data
     * @return a WeatherDataEntity object
     */
    private static WeatherDataEntity getWeatherDataEntity(String stationData, String timestamp) {
        WeatherDataEntity weatherDataEntity = new WeatherDataEntity();

        weatherDataEntity.setTimestamp(timestamp);
        weatherDataEntity.setStationName(stationData.split("<name>")[1].split("</name>")[0]);
        weatherDataEntity.setWmocode(stationData.split("<wmocode>")[1].split("</wmocode>")[0]);
        weatherDataEntity.setAirtemperature(Double.parseDouble(stationData.split("<airtemperature>")[1].split("</airtemperature>")[0]));
        weatherDataEntity.setWindspeed(Double.parseDouble(stationData.split("<windspeed>")[1].split("</windspeed>")[0]));
        weatherDataEntity.setWeatherphenomenon(stationData.split("<phenomenon>")[1].split("</phenomenon>")[0]);

        return weatherDataEntity;
    }

    public List<WeatherDataEntity> getAllWeatherData() {
        return weatherDataRepository.findAll();
    }

    /**
     * Import up-to-date weather data from the external API and save it to the database
     * Uses a cron expression to schedule the import every hour at 15 minutes past the hour
     */
    @Scheduled(cron = "0 15 * * * *")
    public void importWeatherData() {
        String xmlData = fetchWeatherData();
        try {
            List<WeatherDataEntity> weatherDataEntities = parseWeatherData(xmlData);
            weatherDataRepository.saveAll(weatherDataEntities);
        } catch (IOException e) {
            logger.error("Failed to parse weather data: " + e.getMessage());
        }
    }
}
