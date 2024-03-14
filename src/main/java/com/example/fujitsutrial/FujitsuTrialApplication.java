package com.example.fujitsutrial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FujitsuTrialApplication {

    public static void main(String[] args) {
        SpringApplication.run(FujitsuTrialApplication.class, args);
    }

}
