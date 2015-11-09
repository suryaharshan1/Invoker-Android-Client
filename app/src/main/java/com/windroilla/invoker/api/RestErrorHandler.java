package com.windroilla.invoker.api;

import android.util.Log;

import com.windroilla.invoker.api.responseclasses.InvokerApiErrorResponse;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Surya Harsha Nunnaguppala on 9/11/15.
 */
public class RestErrorHandler implements ErrorHandler {
    private static final String TAG = RestErrorHandler.class.getSimpleName();

    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        if (cause.getKind().equals(RetrofitError.Kind.NETWORK)) {
            //TODO Handle network errors
        }
        if (r != null) {
            switch (r.getStatus()) {
                case 401:
                    Log.d(TAG, "Unauthorized Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 403:
                    Log.d(TAG, "Forbidden Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 404:
                    Log.d(TAG, "Not Found Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 400:
                    Log.d(TAG, "Bad Request Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 409:
                    Log.d(TAG, "Conflict Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 500:
                    Log.d(TAG, "Internal Server Error Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 503:
                    Log.d(TAG, "Service Unavailable Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
                case 504:
                    Log.d(TAG, "Gateway Timeout Exception: " + ((InvokerApiErrorResponse) cause.getBodyAs(InvokerApiErrorResponse.class)).message);
                    break;
            }
        }
        return cause;
    }
}
