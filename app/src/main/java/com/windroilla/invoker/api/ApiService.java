package com.windroilla.invoker.api;

import com.windroilla.invoker.api.requestclasses.RequestBlockTimes;
import com.windroilla.invoker.api.requestclasses.RequestRegistration;
import com.windroilla.invoker.api.requestclasses.RequestSetRegistrationToken;
import com.windroilla.invoker.api.requestclasses.RequestSetUserCourseList;
import com.windroilla.invoker.api.responseclasses.BlockTimeList;
import com.windroilla.invoker.api.responseclasses.Course;
import com.windroilla.invoker.api.responseclasses.Institute;
import com.windroilla.invoker.api.responseclasses.UserProfile;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;
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

    @Headers({"Content-Type:application/json", "Accept:application/json", "Accept-Language:en-US"})
    @GET("/iresp/invoker-api/v1/institutes/courses")
    Observable<List<Course>> getInstituteCourseList(@Query("id") int id);

    @Headers({"Content-Type:application/json", "Accept:application/json", "Accept-Language:en-US"})
    @POST("/iresp/invoker-api/v1/users/setcourselist")
    Observable<BlockTimeList> setUserCourseList(@Body RequestSetUserCourseList body);

    @Headers({"Content-Type:application/json", "Accept:application/json", "Accept-Language:en-US"})
    @POST("/iresp/invoker-api/v1/users/setregtoken")
    Observable<UserProfile> setRegToken(@Body RequestSetRegistrationToken body);

    @Headers({"Content-Type:application/json", "Accept:application/json", "Accept-Language:en-US"})
    @POST("/iresp/invoker-api/v1/users/times")
    Observable<BlockTimeList> getBlockTimes(@Body RequestBlockTimes body);
}
