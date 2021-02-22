package org.sashabrava.shopapp;

import android.app.Application;

import timber.log.Timber;

public class ShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

    }
}
