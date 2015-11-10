package com.windroilla.invoker.api;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
@Module
public final class ApiModule {
    public static final String PRODUCTION_API_URL = "http://athena.nitc.ac.in/";

    private Context context;

    public ApiModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    String provideDeviceID() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return (new OkHttpClient());
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(PRODUCTION_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return (Application) context;
    }

}
