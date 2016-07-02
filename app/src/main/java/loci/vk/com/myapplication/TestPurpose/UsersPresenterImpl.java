package loci.vk.com.myapplication.TestPurpose;

/**
 * Created by vinod on 15/2/16.
 */
public class
        UsersPresenterImpl implements UsersPresenter {

    private final RealmService mRealmService;
    private UserListView mMyListView = new UserListView.EmptyMyListView();

    private boolean usersWereShown = false;

    public UsersPresenterImpl(final RealmService realmService) {
        mRealmService = realmService;
        for(int i=0;i<10;i++)
            mRealmService.addUserAsync(null);
    }

    @Override
    public void setView(final Object view) {
        mMyListView = (UserListView) view;
        showUsersIfNeeded();
    }

    private void showUsersIfNeeded() {
        if(!usersWereShown) {
            mMyListView.showUsers(mRealmService.getAllUsers());
            mRealmService.deleteUserAsync(null);
            usersWereShown = true;
        }
    }

    @Override
    public void clearView() {
        mMyListView = new UserListView.EmptyMyListView();
    }

    @Override
    public void closeRealm() {
        mRealmService.closeRealm();
    }

    @Override
    public void onUserClick(final int id) {
        mMyListView.showUserDetailView(id);
    }

    @Override
    public void onAddNewUserClick() {
        mMyListView.showAddNewUserView();
    }
}

