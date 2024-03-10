package com.example.fujitsutrial.repositories;

import com.example.fujitsutrial.entities.WeatherDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDataRepository extends JpaRepository<WeatherDataEntity, Long> {
    WeatherDataEntity findTopByStationNameOrderByTimestampDesc(String name);
}
