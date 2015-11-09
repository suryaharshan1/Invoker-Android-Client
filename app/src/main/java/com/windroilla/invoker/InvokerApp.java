package com.windroilla.invoker;

import android.app.Application;

import com.windroilla.invoker.api.ApiModule;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public class InvokerApp extends Application {
    private static InvokerApp sInstance;
    private InvokerGraph invokerGraph;

    public static InvokerApp getsInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        invokerGraph = DaggerInvokerGraph.builder()
                .apiModule(new ApiModule(this))
                .build();
    }

    public InvokerGraph graph() {
        return invokerGraph;
    }
}
