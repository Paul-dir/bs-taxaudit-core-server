package com.mor.itas.application.scheduling;

import com.mor.itas.application.usecase.qa.sampling.RunSamplingJobUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QaSamplingJob {

    private static final Logger logger = LoggerFactory.getLogger(QaSamplingJob.class);
    private final RunSamplingJobUseCase runSamplingJobUseCase;

    public QaSamplingJob(RunSamplingJobUseCase runSamplingJobUseCase) {
        this.runSamplingJobUseCase = runSamplingJobUseCase;
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Run at midnight on the 1st of every month
    public void runPeriodicSampling() {
        logger.info("Starting periodic QA sampling job");
        try {
            int selectedCount = runSamplingJobUseCase.executeSamplingJob();
            logger.info("QA sampling job completed. Selected {} cases for review", selectedCount);
        } catch (Exception e) {
            logger.error("Error during QA sampling job", e);
        }
    }
}