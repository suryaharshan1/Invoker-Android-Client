package com.windroilla.invoker.api;

import com.windroilla.invoker.api.requestclasses.RequestRegistration;
import com.windroilla.invoker.api.responseclasses.UserProfile;

import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public interface ApiService {

    @Headers({"Content-Type:application/json"})
    @POST("/users")
    Observable<UserProfile> registerNewUser(@Body RequestRegistration body);
}
