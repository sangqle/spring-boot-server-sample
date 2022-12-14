package com.example.springboot;


import com.example.springboot.storage.StorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Disable autoconfiguration datasource, and we will be config in configuration package
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBootServerApplication {
    public static Logger _Logger = LogManager.getLogger(SpringBootServerApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringBootServerApplication.class, args);
            _Logger.info("Server is running");
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        }
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}