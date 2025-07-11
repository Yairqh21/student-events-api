package com.devsz.studenteventsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudentEventsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentEventsApiApplication.class, args);
    }

}


