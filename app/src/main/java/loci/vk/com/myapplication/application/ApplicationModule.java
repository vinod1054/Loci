package loci.vk.com.myapplication.application;

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import loci.vk.com.myapplication.LocationsList.LocationListAdapter;
import loci.vk.com.myapplication.SignInActivity;
import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.addnewlocation.AddNewLocationActivity;
import loci.vk.com.myapplication.addnewlocation.AddNewLocationPresenterImpl;
import loci.vk.com.myapplication.locationdetails.LocationDetailsPresenterImpl;
import loci.vk.com.myapplication.services.LocationTrackerService;
import loci.vk.com.myapplication.utils.LocationUtils;
import loci.vk.com.myapplication.utils.SharedPreferencesUtil;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by vinod on 15/2/16.
 */
@Module(injects = {
        SignInActivity.class,
        LocationTrackerService.class,
        LocationUtils.class,
        LocationListAdapter.class,
        AddNewLocationPresenterImpl.class,
        LocationDetailsPresenterImpl.class,
        AddNewLocationActivity.class}, library = true)
public class ApplicationModule {

    private final LociApplication application;

    public ApplicationModule(LociApplication application) {
        this.application = application;
    }

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    RealmService provideRealmService(final Realm realm) {
        return new RealmService(realm);
    }
    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @ForApplication} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton
  //  @ForApplication
    Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }

    @Provides @Singleton
    SharedPreferencesUtil provideSharedPreferencesUtil(Context context) {
        return new SharedPreferencesUtil(context);
    }

    @Provides @Singleton
    LocationUtils provideLocationsUtil(Context context,LocationManager locationManager) {
        return new LocationUtils(context,locationManager);
    }

}
