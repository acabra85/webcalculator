package com.acabra.calculator.job;

import com.acabra.calculator.WebCalculatorManager;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Agustin on 10/6/2016.
 */
public class WebCalculatorJobManager {

    private static final Logger logger = Logger.getLogger(WebCalculatorJobManager.class);

    private static final String JOB_GROUP_1 = "GROUP_1";
    private final SimpleScheduleBuilder EVERY_10_MINUTES = simpleSchedule().withIntervalInSeconds(15).repeatForever();
    static final String WEB_CALC_MANAGER_KEY = "webCalculatorManager";
    static final String WEB_CALC_POLICY_KEY = "webCalculatorPolicy";

    private final WebCalculatorManager webCalculatorManager;
    private final WebCalculatorHistoryCleanerPolicy policyCleaner;
    private Scheduler scheduler;
    private volatile boolean started = false;

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
            if (webCalculatorManager!= null && !this.started && this.scheduler != null && !this.scheduler.isStarted()) {
                // and start it off
                scheduler.start();
                logger.info("web calculation history scheduler started");

                scheduler.getContext().put(WEB_CALC_MANAGER_KEY, this.webCalculatorManager);
                scheduler.getContext().put(WEB_CALC_POLICY_KEY, this.policyCleaner);

                scheduleWebCalculatorHistoryCleanerJob();

                this.started = true;
            }
        } catch (SchedulerException se) {
            logger.info("failed_ " + se.getMessage());
            logger.error(se);
        }
    }

    private void scheduleWebCalculatorHistoryCleanerJob() throws SchedulerException {
        JobDetail syncJobDetail = newJob(WebCalculationHistoryCleanerJob.class)
                .withIdentity("historyCleanJob", JOB_GROUP_1)
                .build();

        Trigger syncTrigger = newTrigger()
                .withIdentity("syncTrigger", JOB_GROUP_1)
                .startNow()
                .withSchedule(EVERY_10_MINUTES)
                .build();

        scheduler.scheduleJob(syncJobDetail, syncTrigger);
    }


    public void shutDown() {
        try {
            if (scheduler!= null && scheduler.isStarted()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException se) {
            logger.error(se);
        }
    }
}
