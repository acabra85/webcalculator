package com.acabra.calculator.job;

import com.acabra.calculator.WebCalculatorManager;
import com.acabra.calculator.view.RenderType;
import com.acabra.calculator.view.WebCalculatorRenderFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Agustin on 10/18/2016.
 */
public class WebCalculatorJobManagerTest {


    private WebCalculatorManager webCalculatorManagerStub;
    private WebCalculatorHistoryCleanerPolicy policyCleanerStub;

    @Before
    public void setup() {
        webCalculatorManagerStub = new WebCalculatorManager(WebCalculatorRenderFactory.createRenderer(RenderType.HTML));
        policyCleanerStub = new WebCalculatorHistoryCleanerPolicy(ChronoUnit.MINUTES, 10);
    }

    @Test
    public void startStopTest() {
        WebCalculatorJobManager jobManager = new WebCalculatorJobManager(webCalculatorManagerStub, policyCleanerStub);
        assertFalse(jobManager.isStarted());
        jobManager.start();
        assertTrue(jobManager.isStarted());
        jobManager.shutDown();
        assertFalse(jobManager.isStarted());
    }

    @Test(expected = IllegalStateException.class)
    public void failureStopTest() {
        WebCalculatorJobManager jobManager = new WebCalculatorJobManager(webCalculatorManagerStub, policyCleanerStub);
        assertFalse(jobManager.isStarted());
        jobManager.shutDown();
    }
}
