package com.piyushrai.upcovid19.Activity;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.ApiExceptionMapper;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;

public class SettingsClient extends GoogleApi<Api.ApiOptions.NoOptions> {
    public SettingsClient(@NonNull Context var1) {
        super(var1, LocationServices.API, (Api.ApiOptions.NoOptions) null, new ApiExceptionMapper());
    }

    public SettingsClient(@NonNull Activity var1) {
        super(var1, LocationServices.API, (Api.ApiOptions.NoOptions) null, new ApiExceptionMapper());
    }

    public Task<LocationSettingsResponse> checkLocationSettings(LocationSettingsRequest var1) {
        return PendingResultUtil.toResponseTask(LocationServices.SettingsApi.checkLocationSettings(this.asGoogleApiClient(), var1), new LocationSettingsResponse());
    }
}
