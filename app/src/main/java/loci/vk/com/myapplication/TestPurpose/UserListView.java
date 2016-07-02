package loci.vk.com.myapplication.TestPurpose;

import io.realm.RealmResults;
import loci.vk.com.myapplication.model.User;

/**
 * Created by vinod on 15/2/16.
 */
public interface UserListView {

    void showUsers(RealmResults<User> users);
    void showUserDetailView(int id);
    void showAddNewUserView();

    class EmptyMyListView implements UserListView {

        @Override
        public void showUsers(final RealmResults<User> users) {

        }

        @Override
        public void showUserDetailView(final int id) {

        }

        @Override
        public void showAddNewUserView() {

        }
    }
}

