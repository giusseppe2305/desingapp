package com.optic.projectofinal.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private boolean firstTime=false;
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        firstTime=true;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations && new AuthenticationProvider().existSession()) {
            // App enters foreground
            new UserDatabaseProvider().updateOnline(true);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations&& new AuthenticationProvider().existSession()) {
            // App enters background
            new UserDatabaseProvider().updateOnline(false);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

        if (!firstTime && !isActivityChangingConfigurations&& new AuthenticationProvider().existSession()) {
            new UserDatabaseProvider().updateOnline(false);

            // App enters background
        }
        if(firstTime){
            firstTime=false;
        }
    }
}
