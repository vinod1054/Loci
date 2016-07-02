package loci.vk.com.myapplication.TestPurpose;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.application.BaseActivity;
import loci.vk.com.myapplication.model.User;
import loci.vk.com.myapplication.modules.UsersModule;

public class UsersListActivity extends BaseActivity implements UserListView, UserListAdapter.OnUserClickListener {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Inject
    UsersPresenter mUsersPresenter;

    @Inject
    UserListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users_list);
        ButterKnife.bind(this);

        initToolbar();
        initList();
    }
    @Override
    protected Object getModule() {
        return new UsersModule();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initList() {
        mAdapter.setOnUserClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUsersPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUsersPresenter.clearView();
    }

    @Override
    protected void closeRealm() {
        mUsersPresenter.closeRealm();
    }

    @Override
    public void showUsers(final RealmResults<User> users) {
        mAdapter.setUsers(users);
    }

    @Override
    public void onUserClick(final int id) {
        mUsersPresenter.onUserClick(id);
    }

    @OnClick(R.id.fab)
    public void onAddNewUserClick() {
        mUsersPresenter.onAddNewUserClick();
    }

    @Override
    public void showUserDetailView(final int id) {
        Toast.makeText(getApplicationContext(),"showUserDetailView",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAddNewUserView() {
        Toast.makeText(getApplicationContext(),"showAddNewUserView",Toast.LENGTH_LONG).show();
    }
}