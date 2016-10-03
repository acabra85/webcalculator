package com.acabra.calculator.response;

import java.util.UUID;

/**
 * Created by Agustin on 10/3/2016.
 */
public class WebCalculatorFactoryResponse {

    public static SimpleResponse createTokenResponse(long id) {
        return new TokenResponse(id, UUID.randomUUID().toString());
    }

    public static SimpleResponse createTableResponse(long id, String table) {
        return new TableHistoryResponse(id, table);
    }
}
