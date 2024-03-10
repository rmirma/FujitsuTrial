package com.example.fujitsutrial.services;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import com.example.fujitsutrial.repositories.WeatherDataRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherDataImport {
    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate;
    private static final String WEATHER_DATA_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<String> STATION_NAMES = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
    public WeatherDataImport(WeatherDataRepository weatherDataRepository, RestTemplate restTemplate) {
        this.weatherDataRepository = weatherDataRepository;
        this.restTemplate = restTemplate;
    }

    public String fetchWeatherData() {
        return restTemplate.getForObject(WEATHER_DATA_URL, String.class);
    }

    public List<WeatherDataEntity> parseWeatherData(String xmlData) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();


        List<WeatherDataEntity> weatherDataEntities = new ArrayList<>();

        for (String stationName : STATION_NAMES) {
            WeatherDataEntity weatherDataEntity = xmlMapper.readValue(xmlData, WeatherDataEntity.class);
            weatherDataEntity.setStationName(stationName);
            weatherDataEntities.add(weatherDataEntity);
        }

        return weatherDataEntities;
    }
    @Scheduled(cron = "0 15 * * * *")
    public void importWeatherData() {
        try {
            String weatherDataXml = fetchWeatherData();
            List<WeatherDataEntity> weatherDataEntities = parseWeatherData(weatherDataXml);
            weatherDataRepository.saveAll(weatherDataEntities);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
