package com.shahbour.asteriskdemo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Slf4j
public class RootConfiguration {

    @Bean()
    public ScheduledExecutorService taskScheduler() {
        return  Executors.newScheduledThreadPool(2);
    }
}
