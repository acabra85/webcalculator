package com.acabra.calculator.domain;

import com.acabra.calculator.response.CalculationResponse;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.LocalDateTime;

/**
 * Created by Agustin on 10/8/2016.
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class CalculationHistoryRecordTest {

    private static final long WAIT_LENGTH = 100L;

    private CalculationResponse calculationResponseMock;

    private void waitExecution() {
        try {
            Thread.sleep(WAIT_LENGTH);
        } catch (Exception e) {
            System.out.println("unable to sleep the thread");
        }
    }

    @BeforeEach
    public void prepare() {
        this.calculationResponseMock = Mockito.mock(CalculationResponse.class);
    }

    @Test
    public void validateObjectCreationProvidesLastUsedDatesAfterTest() {
        CalculationHistoryRecord calculationHistoryRecord = new CalculationHistoryRecord(calculationResponseMock);
        waitExecution();
        Assertions.assertThat(LocalDateTime.now().isAfter(calculationHistoryRecord.getLastUsed())).isTrue();
    }

    @Test
    public void validateListQueryUpdatesLastUsedTest() {
        CalculationHistoryRecord calculationHistoryRecord = new CalculationHistoryRecord(calculationResponseMock);
        LocalDateTime creationTime = calculationHistoryRecord.getLastUsed();

        waitExecution();
        Assertions.assertThat(calculationHistoryRecord.getCalculationHistory().size()).isEqualTo(1);
        waitExecution();

        LocalDateTime latestUsed = calculationHistoryRecord.getLastUsed();
         Assertions.assertThat(creationTime.isBefore(latestUsed)).isTrue();
         Assertions.assertThat(LocalDateTime.now().isAfter(latestUsed)).isTrue();
    }

    @Test
    public void validateListAppendUpdatesLastUsedTest() {

        CalculationHistoryRecord calculationHistoryRecord = new CalculationHistoryRecord(calculationResponseMock);
        LocalDateTime creationTime = calculationHistoryRecord.getLastUsed();

        waitExecution();
        calculationHistoryRecord.append(calculationResponseMock);
        waitExecution();

        LocalDateTime latestUsed = calculationHistoryRecord.getLastUsed();
        Assertions.assertThat(creationTime.isBefore(latestUsed)).isTrue();
        Assertions.assertThat(LocalDateTime.now().isAfter(latestUsed)).isTrue();
    }

}
