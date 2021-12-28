package com.acabra.calculator.job;

import com.acabra.calculator.WebCalculatorManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.LocalDateTime;

/**
 * Created by Agustin on 10/6/2016.
 */
@Slf4j
public class WebCalculatorCalculationHistoryCleanerJob implements Job {

    private LocalDateTime lastRun = null;
    private WebCalculatorHistoryCleanerPolicy policy;
    private WebCalculatorManager webCalculatorManager;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (lastRun == null) {
            lastRun = LocalDateTime.now();
        }
        try {
            logger.info("last run " + lastRun);
            provideJobInputs(jobExecutionContext.getScheduler().getContext());
            webCalculatorManager.cleanExpiredEntries(this.lastRun, this.policy.getExpirationInterval(), this.policy.getUnit())
                    .thenAccept(cleanedEntries -> lastRun = LocalDateTime.now());
        } catch (SchedulerException | NullPointerException e) {
            logger.error("error", e);
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

    public LocalDateTime getLastRun() {
        return lastRun;
    }
}
