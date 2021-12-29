package com.acabra.calculator.job;

import com.acabra.calculator.WebCalculatorManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Agustin on 10/6/2016.
 */
@Slf4j
public class WebCalculatorJobManager {

    private static final String JOB_GROUP_1 = "GROUP_1";
    private final SimpleScheduleBuilder EVERY_15_MINUTES = simpleSchedule().withIntervalInMinutes(15).repeatForever();
    static final String WEB_CALC_MANAGER_KEY = "webCalculatorManager";
    static final String WEB_CALC_POLICY_KEY = "webCalculatorPolicy";

    private final WebCalculatorManager webCalculatorManager;
    private final WebCalculatorHistoryCleanerPolicy policyCleaner;
    private Scheduler scheduler;
    private volatile boolean started = false;

    public static void main(String ...args) {
        logger.info("heroku worker triggered");
    }

    public WebCalculatorJobManager(WebCalculatorManager webCalculatorManager, WebCalculatorHistoryCleanerPolicy policyCleaner) {
        this.webCalculatorManager = webCalculatorManager;
        this.policyCleaner = policyCleaner;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            scheduler = null;
        }
    }

    public void start() {
        try {
            logger.info("trying to start");
            if (this.scheduler == null ) {
                // Grab the Scheduler instance from the Factory
                this.scheduler = StdSchedulerFactory.getDefaultScheduler();
            }
            if (webCalculatorManager!= null && this.scheduler != null && !this.scheduler.isStarted() && !this.started) {
                // and start it off
                scheduler.start();
                logger.info("web calculation history scheduler started");

                scheduler.getContext().put(WEB_CALC_MANAGER_KEY, this.webCalculatorManager);
                scheduler.getContext().put(WEB_CALC_POLICY_KEY, this.policyCleaner);

                scheduleWebCalculatorHistoryCleanerJob();
                this.started = !(scheduler.isShutdown() && scheduler.isInStandbyMode());
            }
        } catch (SchedulerException se) {
            logger.info("failed_ " + se.getMessage());
            logger.error("error", se);
        }
    }

    private void scheduleWebCalculatorHistoryCleanerJob() throws SchedulerException {
        JobDetail syncJobDetail = newJob(WebCalculatorCalculationHistoryCleanerJob.class)
                .withIdentity("historyCleanJob", JOB_GROUP_1)
                .build();

        Trigger syncTrigger = newTrigger()
                .withIdentity("syncTrigger", JOB_GROUP_1)
                .startNow()
                .withSchedule(EVERY_15_MINUTES)
                .build();

        scheduler.scheduleJob(syncJobDetail, syncTrigger);
    }


    public void shutDown() {
        try {
            if (!this.started) {
                throw new IllegalStateException("The system has not been yet turned on");
            }
            if (scheduler!= null && scheduler.isStarted()) {
                scheduler.shutdown();
                this.started = !scheduler.isShutdown();
            }
        } catch (SchedulerException se) {
            logger.error("scheduler exception", se);
        }
    }

    public boolean isStarted() {
        return started;
    }
}
