package com.windroilla.invoker.api;

import com.windroilla.invoker.api.requestclasses.RequestRegistration;
import com.windroilla.invoker.api.responseclasses.Institute;
import com.windroilla.invoker.api.responseclasses.UserProfile;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public interface ApiService {

    @Headers({"Content-Type:application/json", "Accept:application/json", "Accept-Language:en-US"})
    @POST("/iresp/invoker-api/v1/users")
    Observable<UserProfile> registerNewUser(@Body RequestRegistration body);

    @Headers({"Content-Type:application/json", "Accept:application/json", "Accept-Language:en-US"})
    @GET("/iresp/invoker-api/v1/institutes")
    Observable<List<Institute>> getInstituteList();
}