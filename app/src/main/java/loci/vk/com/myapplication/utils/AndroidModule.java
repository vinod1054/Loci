package loci.vk.com.myapplication.utils;

/**
 * Created by vinod on 17/2/16.
 */

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import loci.vk.com.myapplication.application.LociApplication;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(library = true)
public class AndroidModule {
    private final LociApplication application;

    public AndroidModule(LociApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    SharedPreferencesUtil provideSharedPreferencesUtil() {
        return new SharedPreferencesUtil(application);
    }

    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }
}
