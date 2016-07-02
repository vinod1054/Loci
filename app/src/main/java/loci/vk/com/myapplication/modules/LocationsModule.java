package loci.vk.com.myapplication.modules;

import dagger.Module;
import dagger.Provides;
import loci.vk.com.myapplication.LocationsList.LocationListActivity;
import loci.vk.com.myapplication.LocationsList.LocationListAdapter;
import loci.vk.com.myapplication.LocationsList.LocationPresenter;
import loci.vk.com.myapplication.LocationsList.LocationPresenterImpl;
import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.application.ApplicationModule;

/**
 * Created by vinod on 18/2/16.
 */
@Module(injects = LocationListActivity.class, addsTo = ApplicationModule.class)
public class LocationsModule {

    @Provides
    LocationPresenter provideMyListPresenter(final RealmService realmService) {
        return new LocationPresenterImpl(realmService);
    }

    @Provides
    LocationListAdapter provideMyListAdapter(final RealmService realmService,final  LocationPresenter presenter) {
        return new LocationListAdapter(realmService,presenter);
    }
}
