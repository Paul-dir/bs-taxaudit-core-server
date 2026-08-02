package com.mor.itas.planning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application entry point for Cluster-AP-Implementation.
 * Manages annual audit plan workflows, case assignments, and audit referrals.
 */
@SpringBootApplication
@EnableScheduling
public class ClusterAPImplementationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusterAPImplementationApplication.class, args);
    }
}
