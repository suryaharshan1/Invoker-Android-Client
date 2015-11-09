package com.windroilla.invoker.api.requestclasses;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public class RequestRegistration {
    public final String phone_number;
    public final String mobile_id;

    public RequestRegistration(String phone_number, String mobile_id) {
        this.phone_number = phone_number;
        this.mobile_id = mobile_id;
    }
}
