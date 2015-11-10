package com.windroilla.invoker.api;

import retrofit.RequestInterceptor;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public class ApiHeaders implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/json");
        request.addHeader("Accept-Language","en-US");
    }
}
