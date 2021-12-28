package com.acabra.calculator.job;

import com.acabra.calculator.WebCalculatorManager;


import org.mockito.Mockito;
api.mockito.PowerMockito;
core.classloader.annotations.PowerMockIgnore;
core.classloader.annotations.PrepareForTest;
modules.junit4.PowerMockRunner;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Agustin on 10/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SchedulerContext.class, Scheduler.class, JobExecutionContext.class,
        WebCalculatorManager.class, WebCalculatorHistoryCleanerPolicy.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorCalculationHistoryCleanerJobTest {

    @Test
    public void creationTest() throws SchedulerException, InterruptedException {
        int policy = 10;
        ChronoUnit chronoUnit = ChronoUnit.MINUTES;
        Integer cleanedEntries = 0;
        WebCalculatorHistoryCleanerPolicy cleanerStub = new WebCalculatorHistoryCleanerPolicy(chronoUnit, policy);

        WebCalculatorManager calculatorManagerMock = PowerMockito.mock(WebCalculatorManager.class);
        PowerMockito.when(calculatorManagerMock.cleanExpiredEntries(any(LocalDateTime.class), eq(policy), eq(chronoUnit))).thenReturn(CompletableFuture.completedFuture(cleanedEntries));

        SchedulerContext contextMock = PowerMockito.mock(SchedulerContext.class);
        PowerMockito.when(contextMock.get(eq(WebCalculatorJobManager.WEB_CALC_MANAGER_KEY))).thenReturn(calculatorManagerMock);
        PowerMockito.when(contextMock.get(eq(WebCalculatorJobManager.WEB_CALC_POLICY_KEY))).thenReturn(cleanerStub);

        Scheduler schedulerMock = PowerMockito.mock(Scheduler.class);
        PowerMockito.when(schedulerMock.getContext()).thenReturn(contextMock);

        JobExecutionContext jobExecutionContextMock = PowerMockito.mock(JobExecutionContext.class);
        PowerMockito.when(jobExecutionContextMock.getScheduler()).thenReturn(schedulerMock);

        WebCalculatorCalculationHistoryCleanerJob cleanerJob = new WebCalculatorCalculationHistoryCleanerJob();
        LocalDateTime beforeRunning = cleanerJob.getLastRun();
        cleanerJob.execute(jobExecutionContextMock);

        Mockito.verify(schedulerMock, times(1)).getContext();
        Mockito.verify(calculatorManagerMock, times(1)).cleanExpiredEntries(any(LocalDateTime.class), eq(policy), eq(chronoUnit));
        Mockito.verify(contextMock, times(1)).get(eq(WebCalculatorJobManager.WEB_CALC_POLICY_KEY));
        Mockito.verify(contextMock, times(1)).get(eq(WebCalculatorJobManager.WEB_CALC_POLICY_KEY));
        Mockito.verify(calculatorManagerMock, times(1)).cleanExpiredEntries(any(LocalDateTime.class), eq(policy), eq(chronoUnit));

        assertFalse(cleanerJob.getLastRun().equals(beforeRunning));

    }

    @Test
    public void executionFailNoCalculationManagerFoundTest() throws SchedulerException, InterruptedException {
        int policy = 10;
        ChronoUnit chronoUnit = ChronoUnit.MINUTES;

        WebCalculatorHistoryCleanerPolicy cleanerStub = new WebCalculatorHistoryCleanerPolicy(chronoUnit, policy);

        SchedulerContext contextMock = PowerMockito.mock(SchedulerContext.class);
        PowerMockito.when(contextMock.get(eq(WebCalculatorJobManager.WEB_CALC_MANAGER_KEY))).thenReturn(null);
        PowerMockito.when(contextMock.get(eq(WebCalculatorJobManager.WEB_CALC_POLICY_KEY))).thenReturn(cleanerStub);

        Scheduler schedulerMock = PowerMockito.mock(Scheduler.class);
        PowerMockito.when(schedulerMock.getContext()).thenReturn(contextMock);

        JobExecutionContext jobExecutionContextMock = PowerMockito.mock(JobExecutionContext.class);
        PowerMockito.when(jobExecutionContextMock.getScheduler()).thenReturn(schedulerMock);

        WebCalculatorCalculationHistoryCleanerJob cleanerJob = new WebCalculatorCalculationHistoryCleanerJob();
        cleanerJob.execute(jobExecutionContextMock);
    }

    @Test
    public void executionFailNoCleanerPolicyFoundTest() throws SchedulerException, InterruptedException {
        int policy = 10;
        Integer cleanedEntries = 0;
        ChronoUnit chronoUnit = ChronoUnit.MINUTES;

        WebCalculatorManager calculatorManagerMock = PowerMockito.mock(WebCalculatorManager.class);
        PowerMockito.when(calculatorManagerMock.cleanExpiredEntries(any(LocalDateTime.class), eq(policy), eq(chronoUnit)))
                .thenReturn(CompletableFuture.completedFuture(cleanedEntries));

        SchedulerContext contextMock = PowerMockito.mock(SchedulerContext.class);
        PowerMockito.when(contextMock.get(eq(WebCalculatorJobManager.WEB_CALC_MANAGER_KEY))).thenReturn(calculatorManagerMock);
        PowerMockito.when(contextMock.get(eq(WebCalculatorJobManager.WEB_CALC_POLICY_KEY))).thenReturn(null);

        Scheduler schedulerMock = PowerMockito.mock(Scheduler.class);
        PowerMockito.when(schedulerMock.getContext()).thenReturn(contextMock);

        JobExecutionContext jobExecutionContextMock = PowerMockito.mock(JobExecutionContext.class);
        PowerMockito.when(jobExecutionContextMock.getScheduler()).thenReturn(schedulerMock);

        WebCalculatorCalculationHistoryCleanerJob cleanerJob = new WebCalculatorCalculationHistoryCleanerJob();
        cleanerJob.execute(jobExecutionContextMock);
    }
}
