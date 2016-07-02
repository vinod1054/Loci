package loci.vk.com.myapplication.application;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import dagger.ObjectGraph;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import loci.vk.com.myapplication.modules.LocationsModule;
import loci.vk.com.myapplication.modules.UsersModule;

/**
 * Created by vinod on 15/2/16.
 */
public class LociApplication extends Application {

    public static LociApplication sInstance;
    private ObjectGraph mApplicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        sInstance = this;
        initRealmConfiguration();
        initApplicationGraph();
    }

    private void initRealmConfiguration() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initApplicationGraph() {
        mApplicationGraph = ObjectGraph.create(
                new ApplicationModule(this),
                new LocationsModule(),
                new UsersModule());
    }

    public  static void injectModules(@NonNull final Object object/* final Object... modules*/) {

        sInstance.mApplicationGraph.inject(object);
    }

}
