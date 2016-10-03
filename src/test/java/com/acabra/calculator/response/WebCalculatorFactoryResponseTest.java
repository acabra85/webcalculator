package com.acabra.calculator.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Agustin on 10/3/2016.
 */
public class WebCalculatorFactoryResponseTest {

    @Test
    public void createTokenResponse1Test() {
        long id = 0L;
        TokenResponse tokenResponse = (TokenResponse) WebCalculatorFactoryResponse.createTokenResponse(id);
        assertEquals(id, tokenResponse.getId());
        assertNotNull(tokenResponse.getToken());
    }

    @Test
    public void createTableResponseTest() {
        long id = 0L;
        String tablehtml = "tablehtml";
        TableHistoryResponse tableResponse = (TableHistoryResponse) WebCalculatorFactoryResponse.createTableResponse(id, tablehtml);
        assertEquals(id, tableResponse.getId());
        assertEquals(tablehtml, tableResponse.getTableHTML());
    }
}
