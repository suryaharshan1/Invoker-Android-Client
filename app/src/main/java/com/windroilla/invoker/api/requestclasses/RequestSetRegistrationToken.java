package com.windroilla.invoker.api.requestclasses;

/**
 * Created by vishnu on 15/11/15.
 */
public class RequestSetRegistrationToken {
    public final String mobile_id;
    public final String reg_token;


    public RequestSetRegistrationToken(String mobile_id, String reg_token) {
        this.mobile_id = mobile_id;
        this.reg_token = reg_token;
    }
}
