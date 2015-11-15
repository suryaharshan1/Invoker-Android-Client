package com.windroilla.invoker;

import com.windroilla.invoker.api.ApiModule;
import com.windroilla.invoker.gcm.RegistrationIntentService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
@Singleton
@Component(modules = {ApiModule.class})

public interface InvokerGraph {
    void inject(RegisterActivity registerActivity);

    void inject(InstituteActivity instituteActivity);

    void inject(CourseActivity courseActivity);

    void inject(RegistrationIntentService registrationIntentService);
}
