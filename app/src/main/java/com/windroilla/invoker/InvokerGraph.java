package com.windroilla.invoker;

import com.windroilla.invoker.api.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
@Singleton
@Component(modules = {ApiModule.class})

public interface InvokerGraph {
    void inject(RegisterActivity registerActivity);
}
