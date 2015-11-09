package com.windroilla.invoker.api.responseclasses;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public class UserProfile {
    public int id;
    public String mobile_id;
    public String phone_number;
    public String access_time;

    public String getAccess_time() {
        return access_time;
    }

    public int getId() {

        return id;
    }

    public String getMobile_id() {
        return mobile_id;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
