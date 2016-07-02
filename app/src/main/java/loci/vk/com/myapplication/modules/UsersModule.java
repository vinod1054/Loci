package loci.vk.com.myapplication.modules;

import dagger.Module;
import dagger.Provides;
import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.TestPurpose.UserListAdapter;
import loci.vk.com.myapplication.TestPurpose.UsersListActivity;
import loci.vk.com.myapplication.TestPurpose.UsersPresenter;
import loci.vk.com.myapplication.TestPurpose.UsersPresenterImpl;
import loci.vk.com.myapplication.application.ApplicationModule;

/**
 * Created by vinod on 15/2/16.
 */
@Module(injects = UsersListActivity.class, addsTo = ApplicationModule.class)
public class UsersModule {

    @Provides
    UsersPresenter provideMyListPresenter(final RealmService realmService) {
        return new UsersPresenterImpl(realmService);
    }

    @Provides
    UserListAdapter provideMyListAdapter() {
        return new UserListAdapter();
    }
}
