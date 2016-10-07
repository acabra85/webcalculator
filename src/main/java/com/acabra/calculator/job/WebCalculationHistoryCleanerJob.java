package com.acabra.calculator.job;

import com.acabra.calculator.WebCalculatorManager;
import org.apache.log4j.Logger;
import org.quartz.*;

import java.time.LocalDateTime;

/**
 * Created by Agustin on 10/6/2016.
 */
public class WebCalculationHistoryCleanerJob implements Job {

    private static final Logger logger = Logger.getLogger(WebCalculationHistoryCleanerJob.class);

    private LocalDateTime lastRun = null;
    private WebCalculatorHistoryCleanerPolicy policy;
    private WebCalculatorManager webCalculatorManager;

    public static void main(String ...args) {
        logger.info("heroku worker triggered");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (lastRun == null) {
            lastRun = LocalDateTime.now();
        }
        try {
            logger.info("last run " + lastRun);
            provideJobInputs(jobExecutionContext.getScheduler().getContext());
            webCalculatorManager.cleanExpiredEntries(this.lastRun, this.policy.getExpirationInterval(), this.policy.getUnit()).thenAccept(
                cleanedEntries -> {
                    logger.info("total expired entries removed :" + cleanedEntries);
                    lastRun = LocalDateTime.now();
                }
            );
        } catch (SchedulerException | NullPointerException e) {
            logger.error(e);
        }
    }

    private void provideJobInputs(SchedulerContext schedulerContext) {
        if (null == this.webCalculatorManager) {
            this.webCalculatorManager = (WebCalculatorManager) schedulerContext.get(WebCalculatorJobManager.WEB_CALC_MANAGER_KEY);
        }
        if (null == this.policy) {
            this.policy = (WebCalculatorHistoryCleanerPolicy) schedulerContext.get(WebCalculatorJobManager.WEB_CALC_POLICY_KEY);
        }
        if (this.policy == null || this.webCalculatorManager == null){
            throw new NullPointerException("unable to retrieve cleaner job inputs");
        }
    }
}
