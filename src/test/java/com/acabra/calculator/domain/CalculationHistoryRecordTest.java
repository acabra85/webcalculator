package com.acabra.calculator.domain;

import com.acabra.calculator.response.CalculationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 10/8/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CalculationResponse.class, CalculationHistoryRecord.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class CalculationHistoryRecordTest {

    private static long WAIT_LENGTH = 1000L;

    private CalculationResponse calculationResponseMock;

    private void waitExecution() {
        try {
            Thread.sleep(WAIT_LENGTH);
        } catch (Exception e) {
            System.out.println("unable to sleep the thread");
        }
    }

    @Before
    public void prepare() {
        this.calculationResponseMock = PowerMockito.mock(CalculationResponse.class);
    }

    @Test
    public void validateObjectCreationProvidesLastUsedDatesAfterTest() {
        CalculationHistoryRecord calculationHistoryRecord = new CalculationHistoryRecord(calculationResponseMock);
        waitExecution();
        assertTrue(LocalDateTime.now().isAfter(calculationHistoryRecord.getLastUsed()));
    }

    @Test
    public void validateListQueryUpdatesLastUsedTest() {
        CalculationHistoryRecord calculationHistoryRecord = new CalculationHistoryRecord(calculationResponseMock);
        LocalDateTime creationTime = calculationHistoryRecord.getLastUsed();

        waitExecution();
        assertEquals(1, calculationHistoryRecord.getCalculationHistory().size());
        waitExecution();

        LocalDateTime latestUsed = calculationHistoryRecord.getLastUsed();
        assertTrue(creationTime.isBefore(latestUsed));
        assertTrue(LocalDateTime.now().isAfter(latestUsed));
    }

    @Test
    public void validateListAppendUpdatesLastUsedTest() throws InterruptedException {

        CalculationHistoryRecord calculationHistoryRecord = new CalculationHistoryRecord(calculationResponseMock);
        LocalDateTime creationTime = calculationHistoryRecord.getLastUsed();

        waitExecution();
        calculationHistoryRecord.append(calculationResponseMock);
        waitExecution();

        LocalDateTime latestUsed = calculationHistoryRecord.getLastUsed();
        assertTrue(creationTime.isBefore(latestUsed));
        assertTrue(LocalDateTime.now().isAfter(latestUsed));
    }

}
