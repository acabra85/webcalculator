package com.acabra.resources;


import com.acabra.domain.response.SimpleResponse;

import javax.ws.rs.core.Response;

/**
 * @author acabra
 * @created 2016-09-27
 */
public interface AppResource {

    /**
     * Builds a Http response based on a status code (HTTP) and a message
     * @param status An HTTP status response code
     * @param message A message to show to the user
     * @param body the body containing the answer
     * @return a well formed http response
     */
    Response getResponse(Response.Status status, String message, SimpleResponse body);
}
