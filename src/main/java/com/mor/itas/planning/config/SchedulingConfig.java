package com.mor.itas.planning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    // Enables @Scheduled annotations across the application (e.g. OutboxDrainer)
}
