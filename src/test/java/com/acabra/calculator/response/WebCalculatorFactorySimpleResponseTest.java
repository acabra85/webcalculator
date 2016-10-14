package com.acabra.calculator.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Agustin on 10/3/2016.
 */
public class WebCalculatorFactorySimpleResponseTest {

    @Test
    public void createTokenResponse1Test() {
        long id = 0L;
        TokenResponse tokenResponse = (TokenResponse) WebCalculatorFactorySimpleResponse.createTokenResponse(id);
        assertEquals(id, tokenResponse.getId());
        assertNotNull(tokenResponse.getToken());
    }

    @Test
    public void createTableResponseTest() {
        long id = 0L;
        String tablehtml = "tablehtml";
        RenderedHistoryResponse tableResponse = (RenderedHistoryResponse) WebCalculatorFactorySimpleResponse.createTableResponse(id, tablehtml);
        assertEquals(id, tableResponse.getId());
        assertEquals(tablehtml, tableResponse.getRenderedTable());
    }
}
